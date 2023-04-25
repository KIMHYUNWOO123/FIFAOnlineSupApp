package com.example.domain.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TransactionRecordEntity(
    val tradeDate: String,
    val saleSn: String,
    @SerializedName("spid")
    val spId: Int,
    val grade: Int,
    val value: Int,
)