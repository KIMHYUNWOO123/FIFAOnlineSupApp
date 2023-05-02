package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class TransactionData(
    val name: String,
    val className: String,
    val img: String,
    val tradeDate: String,
    val saleSn: String,
    val spId: Int,
    val grade: Int,
    val value: String
)
