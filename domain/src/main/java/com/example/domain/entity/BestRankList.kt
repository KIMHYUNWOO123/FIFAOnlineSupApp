package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class BestRankList(
    val matchType: String,
    val division: String,
    val date: String,
)