package com.example.domain.entity

data class DisplayMatchData(
    val matchId: String,
    val nickname1: String,
    val nickname2: String,
    val goal1: String,
    val goal2: String,
    val isWin: Boolean,
    val date: String,
)
