package com.example.domain.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "spPosition")
data class SpPositionData(
    @PrimaryKey
    val spPosition: Int,
    val desc: String
)
