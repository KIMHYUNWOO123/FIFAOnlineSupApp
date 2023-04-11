package com.example.domain.entity

import androidx.annotation.Keep
import androidx.room.Entity

@Keep
@Entity
data class MatchType(
    val matchType: Int,
    val desc: String
)