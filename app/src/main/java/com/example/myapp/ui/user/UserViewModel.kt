package com.example.myapp.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.BestRankList
import com.example.domain.usecase.MetaDataUseCase
import com.example.domain.usecase.UserUseCase
import com.example.myapp.map.Mapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCase: UserUseCase,
    private val metaDataUseCase: MetaDataUseCase,
) : ViewModel() {
    private val mapper: Mapper = Mapper()
    private val _userData: MutableLiveData<String> = MutableLiveData("")
    val userData: LiveData<String> = _userData

    private val _userLevel: MutableLiveData<String> = MutableLiveData("")
    val userLevel: LiveData<String> = _userLevel

    private val _userAccessId: MutableLiveData<String> = MutableLiveData("")
    val userAccessId: LiveData<String> = _userAccessId

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData("")

    private val _bestRankList: MutableLiveData<List<BestRankList>> = MutableLiveData()
    val bestRankList: MutableLiveData<List<BestRankList>> = _bestRankList

    fun getUserData(nickname: String) {
        val nickName = nickname.replace(" ", "")
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            Log.d("UserViewModel", "Exception: $t")
            isLoading.postValue(false)
            error.value = if (t.message!!.contains("400")) "사용자 정보가 없습니다" else t.message
        }) {
            _userAccessId.value = ""
            error.value = ""
            val result = useCase.getUserData(nickName)
            result.let {
                Log.d("UserViewModel", "getUserData: $it")
                _userData.postValue(it.nickname)
                _userLevel.postValue(it.level.toString())
                _userAccessId.postValue(it.accessId)
                getBestRank(it.accessId)
            }
        }.invokeOnCompletion {
        }
    }

    private fun getBestRank(accessId: String) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            Log.d("Exception", "getBestRank: $t")
            isLoading.postValue(false)
        }) {
            error.value = ""
            val result = useCase.getBestRank(accessId)
            val matchTypeDataList = async(Dispatchers.IO) {
                metaDataUseCase.getMatch()
            }

            val divisionDataList = async(Dispatchers.IO) {
                metaDataUseCase.getDivision()
            }
            val mapResult = mapper.bestRankMap(result, matchTypeDataList.await(), divisionDataList.await())
            mapResult.let { _bestRankList.postValue(it) }
        }.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }
}