package com.example.myapp.ui.transaction

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.MetaDataUseCase
import com.example.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val useCase: TransactionUseCase,
) : ViewModel() {
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: MutableLiveData<String> = MutableLiveData("")

    fun getTransactionRecord(accessId: String, tradeType: String) {
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
            Log.d("getTransactionRecord", "Exception: $t")
            isLoading.postValue(false)
            error.postValue(t.message)
        }) {
            val result = useCase.getTransactionRecord(accessId, tradeType)
            result.let {
                // TODO
            }
        }
    }
}