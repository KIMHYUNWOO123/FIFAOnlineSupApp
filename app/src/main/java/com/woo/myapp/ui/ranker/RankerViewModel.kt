package com.woo.myapp.ui.ranker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.RankerPlayerData
import com.example.domain.entity.SearchRankerData
import com.example.domain.usecase.MetaDataUseCase
import com.example.domain.usecase.RankerUseCase
import com.woo.myapp.map.Mapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class RankerViewModel @Inject constructor(
    private val metaDataUseCase: MetaDataUseCase,
    private val rankerUseCase: RankerUseCase,
) : ViewModel() {
    private val mapper = Mapper()
    val isSearchLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRankerLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData("")

    private val _spIdData = MutableLiveData<List<SearchRankerData>>()
    val spIdData: LiveData<List<SearchRankerData>> = _spIdData
    private val _rankerData = MutableLiveData<List<RankerPlayerData>>()
    val rankerData: LiveData<List<RankerPlayerData>> = _rankerData

    private var myJob: Job? = null
    private var myApiJob: Job? = null


    fun searchSpId(name: String) {
        myJob?.cancel()
        error.postValue("")
        isSearchLoading.postValue(true)
        myJob = viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            error.postValue(t.message.toString())
            Log.d("searchSpId", "Exception: ${t.message}")
            isSearchLoading.postValue(false)
        }) {
            withContext(Dispatchers.IO) {
                val result = metaDataUseCase.searchSpId("%$name%")
                val seasonIdData = metaDataUseCase.getSeasonId()
                result.let {
                    _spIdData.postValue(mapper.searchRankerDataMap(it, seasonIdData))
                }
            }
        }
        myJob?.invokeOnCompletion {
            isSearchLoading.postValue(false)
        }
    }

    fun setSpIdList() {
        _spIdData.postValue(emptyList())
    }

    fun getPlayerData(matchType: Int, spId: Int) {
        myApiJob?.cancel()
        error.postValue("")
        isRankerLoading.postValue(true)
        myApiJob = viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            error.postValue(t.message)
            isRankerLoading.postValue(false)
            Log.d("getPlayerData", "Exception: ${t.message} ")
        }) {
            withContext(Dispatchers.IO) {
                val positionData = metaDataUseCase.getSpPosition()
                val result = rankerUseCase.getRankerPlayerData(matchType, mapper.toJsonPlayer(spId, positionData))
                result.let { list ->
                    val rankerList = mutableListOf<RankerPlayerData>()
                    for ((i, item) in list.withIndex()) {
                        rankerList.add(i, mapper.getRankerPlayerDataMap(item, positionData))
                    }
                    _rankerData.postValue(rankerList.filter { it.status.matchCount != 0 }.sortedByDescending { it.status.matchCount }.toList())
                }
            }
        }
        myApiJob?.invokeOnCompletion {
            isRankerLoading.postValue(false)
        }
    }

    fun setRankerList() {
        _rankerData.postValue(emptyList())
    }

    override fun onCleared() {
        super.onCleared()
        myJob?.cancel()
    }
}
