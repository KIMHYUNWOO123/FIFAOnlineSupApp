package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class BestRankEntity(
    val matchType: Int,
    val division: Int,
    val achievementDate: String
)
