package com.example.myapp.ui.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.MatchTypeData
import com.example.domain.usecase.MetaDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val metaDataUseCase: MetaDataUseCase,
) : ViewModel() {
    private val _matchTypeList = MutableLiveData<List<MatchTypeData>>()
    val matchTypeList: LiveData<List<MatchTypeData>> = _matchTypeList

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData("")

    fun getMatchTypeList() {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, t ->
            isLoading.postValue(false)
            error.postValue(t.message.toString())
        }) {
            _matchTypeList.postValue(metaDataUseCase.getMatch())
        }.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }
}