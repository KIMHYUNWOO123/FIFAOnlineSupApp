package com.example.domain.usecase

import com.example.domain.Repository
import com.example.domain.entity.TransactionRecordEntity
import javax.inject.Inject

class TransactionUseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend fun getTransactionRecord(accessId: String, tradeType: String): List<TransactionRecordEntity> {
        return repository.getTransactionRecord(accessId, tradeType)
    }
}