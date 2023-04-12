package com.example.domain.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "match")
data class MatchTypeData(
    @PrimaryKey
    val matchType: Int,
    val desc: String
)
