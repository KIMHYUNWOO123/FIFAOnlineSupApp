package com.example.myapp.ui.transaction

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val useCase: TransactionUseCase,
) : ViewModel() {
    fun getTransactionRecord(accessId: String, tradeType: String) {

    }
}