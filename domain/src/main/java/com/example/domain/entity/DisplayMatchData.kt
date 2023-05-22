package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class DisplayMatchData(
    val isFirst : Boolean,
    val matchId: String,
    val nickname1: String,
    val nickname2: String,
    val goal1: String,
    val goal2: String,
    val result : String,
    val date: String,
)
