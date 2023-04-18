package com.example.myapp.ui.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.DetailMatchRecordEntity
import com.example.domain.entity.DisplayMatchData
import com.example.domain.entity.MatchTypeData
import com.example.domain.usecase.MatchUseCase
import com.example.domain.usecase.MetaDataUseCase
import com.example.myapp.map.Mapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val metaDataUseCase: MetaDataUseCase,
    private val matchUseCase: MatchUseCase,
) : ViewModel() {
    private val mapper: Mapper = Mapper()

    private val _matchTypeList = MutableLiveData<List<MatchTypeData>>()
    val matchTypeList: LiveData<List<MatchTypeData>> = _matchTypeList

    private val _matchRecordList = MutableLiveData<List<String>>()
    val matchRecordList: LiveData<List<String>> = _matchRecordList

    private val _detailMatchRecordList = MutableLiveData<DetailMatchRecordEntity>()
    val detailMatchRecordList: LiveData<DetailMatchRecordEntity> = _detailMatchRecordList

    private val _displayMatchData = MutableLiveData<List<DisplayMatchData>>()
    val displayMatchData: LiveData<List<DisplayMatchData>> = _displayMatchData

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData("")

    fun getMatchTypeList() {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, t ->
            isLoading.postValue(false)
            Log.d("MatchViewModel", "Exception: $t")
            error.postValue(t.message.toString())
        }) {
            val result = metaDataUseCase.getMatch()
            result.let { _matchTypeList.postValue(it) }
        }.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }

    fun getMatchRecord(accessId: String, matchType: Int) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            isLoading.postValue(false)
            Log.d("MatchViewModel", "Exception: $t")
            error.postValue(t.message.toString())
        }) {
            val result = matchUseCase.getMatchRecord(accessId, matchType)
            result.let {
                _matchRecordList.postValue(it)
                getDetailMatchRecord(accessId, result)
            }
        }.invokeOnCompletion {
        }
    }

    private fun getDetailMatchRecord(accessId: String, list: List<String>) {
        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            isLoading.postValue(false)
            Log.d("MatchViewModel", "Exception: $t")
            error.postValue(t.message.toString())
        }) {
            val detailMatchList = mutableListOf<DetailMatchRecordEntity>()
            for ((i, item) in list.withIndex()) {
                val result = matchUseCase.getDetailMatchRecord(item)
                result.let { detailMatchList.add(i, it) }
            }
            val result = mapper.displayMatchDataMap(accessId, detailMatchList)
            result.let {
                _displayMatchData.postValue(it)
                Log.d("###", "getDetailMatchRecord: $it")
            }
        }.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }

    fun getDetailData(matchId: String) {
        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            isLoading.postValue(false)
            Log.d("MatchViewModel", "Exception: $t")
            error.postValue(t.message.toString())
        }) {
            val result = matchUseCase.getDetailMatchRecord(matchId)
            result.let {
                _detailMatchRecordList.postValue(it)
            }
        }.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }
}