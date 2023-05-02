package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class SeasonIdEntity(
    val seasonId: Int,
    val className: String,
    val seasonImg: String,
)
