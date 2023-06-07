package com.woo.myapp.ui.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.entity.*
import com.example.domain.usecase.MatchUseCase
import com.example.domain.usecase.MetaDataUseCase
import com.woo.myapp.map.Mapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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

    private val _detailMapDataList = MutableLiveData<List<List<DetailMapData>>>()
    val detailMapDataList: LiveData<List<List<DetailMapData>>> = _detailMapDataList

    private val _displayMatchData = MutableLiveData<List<DisplayMatchData>>()
    val displayMatchData: LiveData<List<DisplayMatchData>> = _displayMatchData

    private val _squadData = MutableLiveData<SquadData>()
    val squadData: LiveData<SquadData> = _squadData

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isMatchRecordLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isDetailLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData("")
    private var myJob: Job? = null
    private var myDetailJob: Job? = null
    private var myMappingJob: Job? = null
    fun getMatchTypeList() {
        isLoading.postValue(true)
        myJob?.cancel()
        myJob = viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, t ->
            isLoading.postValue(false)
            Log.d("MatchViewModel", "Exception: $t")
            error.postValue(t.message.toString())
        }) {
            val result = metaDataUseCase.getMatch(60)
            result.let { _matchTypeList.postValue(it) }
        }
        myJob?.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }

//    fun getMatchRecord(accessId: String, matchType: Int) {
//        isMatchRecordLoading.postValue(true)
//        myJob?.cancel()
//        myJob = viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
//            isMatchRecordLoading.postValue(false)
//            error.postValue(t.message.toString())
//        }) {
//            val result = matchUseCase.getMatchRecord(accessId, matchType)
//            result.let {
//                _matchRecordList.postValue(it)
//                getDetailMatchRecord(accessId, result)
//            }
//        }
//    }

//    private fun getDetailMatchRecord(accessId: String, list: List<String>) {
//        myDetailJob?.cancel()
//        isMatchRecordLoading.postValue(true)
//        myDetailJob = viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
//            isMatchRecordLoading.postValue(false)
//            Log.d("MatchViewModel", "Exception: $t")
//            error.postValue(t.message.toString())
//        }) {
//            withContext(Dispatchers.IO) {
//                val detailMatchList = mutableListOf<DetailMatchRecordEntity>()
//                for ((i, item) in list.withIndex()) {
//                    val result = matchUseCase.getDetailMatchRecord(item)
//                    result.let {
//                        detailMatchList.add(i, it)
//                    }
//                }
//                val result = mapper.displayMatchDataMap(accessId, detailMatchList)
//                result.let {
//                    _displayMatchData.postValue(it)
//                    getDetailDataList(it)
//                }
//            }
//        }
//        myDetailJob?.invokeOnCompletion { t ->
//            if (t == null) {
//                isMatchRecordLoading.postValue(false)
//            }
//        }
//    }

//    private fun getDetailDataList(data: List<DisplayMatchData>) {
//        myMappingJob?.cancel()
//        isDetailLoading.postValue(true)
//        myMappingJob = viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
//            Log.d("MatchViewModel", "Exception: $t")
//            isDetailLoading.postValue(false)
//            error.postValue(t.message.toString())
//        }) {
//            withContext(Dispatchers.IO) {
//                val list = mutableListOf<List<DetailMapData>>()
//                for ((i, item) in data.withIndex()) {
//                    val detailData = matchUseCase.getDetailMatchRecord(item.matchId)
//                    list.add(i, mapper.detailDataMap(item.isFirst, detailData))
//                }
//                _detailMapDataList.postValue(list)
//
//            }
//            myMappingJob?.invokeOnCompletion { t ->
//                if (t == null) {
//                    isDetailLoading.postValue(false)
//                }
//            }
//        }
//    }

    private val _matchRecordPagingData = MutableStateFlow<PagingData<MatchDetailData>>(PagingData.empty())
    val matchRecordPagingData: StateFlow<PagingData<MatchDetailData>> = _matchRecordPagingData.asStateFlow()
    fun getMatchRecordPagingData(accessId: String, matchType: Int) {
        myJob?.cancel()
        myJob = viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            error.postValue(t.message)
        }) {
            matchUseCase.getMatchRecordPagingData(accessId, matchType).cachedIn(viewModelScope).collectLatest {
                    _matchRecordPagingData.value = it
                }
        }
    }

    fun mapSquadList(player1: List<Player>, mvp1: String, player2: List<Player>, mvp2: String, nickname1: String, nickname2: String) {
        _squadData.postValue(null)
        myMappingJob?.cancel()
        isLoading.postValue(true)
        myMappingJob = viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, t ->
            error.postValue(t.message)
            isLoading.postValue(false)
        }) {
            val spData = metaDataUseCase.getSpId()
            val positionData = metaDataUseCase.getSpPosition()
            val seasonData = metaDataUseCase.getSeasonId()
            val result = mapper.getSquadData(
                player1 = player1.sortedBy { it.spPosition },
                player2 = player2.sortedBy { it.spPosition },
                mvp1 = mvp1,
                mvp2 = mvp2,
                spIdData = spData,
                positionData = positionData,
                seasonIdData = seasonData,
                nickname1 = nickname1,
                nickname2 = nickname2
            )
            result.let {
                _squadData.postValue(it)
            }
        }
        myMappingJob?.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        myJob?.cancel()
        myDetailJob?.cancel()
        myMappingJob?.cancel()
    }
}