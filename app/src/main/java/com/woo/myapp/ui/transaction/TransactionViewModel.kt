package com.woo.myapp.ui.transaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.TransactionData
import com.example.domain.usecase.MetaDataUseCase
import com.example.domain.usecase.TransactionUseCase
import com.woo.myapp.map.Mapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val useCase: TransactionUseCase,
    private val metaDataUseCase: MetaDataUseCase,
) : ViewModel() {
    private val mapper = Mapper()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData("")

    private val _transactionData = MutableLiveData<List<TransactionData>>()
    val transactionData: LiveData<List<TransactionData>> = _transactionData
    fun getTransactionRecord(accessId: String, tradeType: String) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            Log.d("getTransactionRecord", "Exception: $t")
            isLoading.postValue(false)
            error.postValue(t.message)
        }) {
            val result = useCase.getTransactionRecord(accessId, tradeType)
            withContext(Dispatchers.IO) {
                result.let {
                    val seasonIdData = metaDataUseCase.getSeasonId()
                    val spIdData = metaDataUseCase.getSpId()
                    _transactionData.postValue(mapper.transactionMap(it, seasonIdData, spIdData))
                }
            }
        }.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }
}