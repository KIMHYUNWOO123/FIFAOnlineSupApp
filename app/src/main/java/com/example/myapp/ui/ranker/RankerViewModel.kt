package com.example.myapp.ui.ranker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.SearchRankerData
import com.example.domain.usecase.MetaDataUseCase
import com.example.myapp.map.Mapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class RankerViewModel @Inject constructor(
    private val metaDataUseCase: MetaDataUseCase,
) : ViewModel() {
    private val mapper = Mapper()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData("")
    private val _spIdData = MutableLiveData<List<SearchRankerData>>()
    val spIdData: LiveData<List<SearchRankerData>> = _spIdData
    private var myJob: Job? = null


    fun searchSpId(name: String) {
        myJob?.cancel()
        isLoading.postValue(true)
        myJob = viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            error.postValue(t.message.toString())
            isLoading.postValue(false)
        }) {
            withContext(Dispatchers.IO) {
                val result = metaDataUseCase.searchSpId("%$name%")
                val seasonIdData = metaDataUseCase.getSeasonId()
                result.let {
                    _spIdData.postValue(mapper.searchRankerDataMap(it, seasonIdData))
                }
            }
        }
    }

    fun setSpIdList() {
        _spIdData.postValue(emptyList())
    }

    override fun onCleared() {
        super.onCleared()
        myJob?.cancel()
    }
}