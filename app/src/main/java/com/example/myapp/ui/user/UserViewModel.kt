package com.example.myapp.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.BestRankEntity
import com.example.domain.entity.BestRankList
import com.example.domain.entity.MatchTypeList
import com.example.domain.entity.UserEntity
import com.example.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCase: UserUseCase
) : ViewModel() {

    private val _userData: MutableLiveData<String> = MutableLiveData("")
    val userData: LiveData<String> = _userData

    private val _userLevel: MutableLiveData<String> = MutableLiveData("")
    val userLevel: LiveData<String> = _userLevel

    private val _userAccessId: MutableLiveData<String> = MutableLiveData("")
    val userAccessId: LiveData<String> = _userAccessId

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData()

    fun getUserData(nickName: String) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            Log.d("Exception", "getUserData: $t")
            isLoading.postValue(false)
        }) {
            val result = useCase.getUserData(nickName)
            result.let {
                Log.d("UserViewModel", "getUserData: $it")
                _userData.postValue(it.nickname)
                _userLevel.postValue(it.level.toString())
                _userAccessId.postValue(it.accessId)
            }
        }.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }

    fun getBestRank(accessId: String) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            Log.d("Exception", "getBestRank: $t")
            isLoading.postValue(false)
        }) {
            val result = useCase.getBestRank(accessId)
        }.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }
}