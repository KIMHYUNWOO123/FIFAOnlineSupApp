package com.example.domain.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "seasonId")
data class SeasonIdData(
    @PrimaryKey(autoGenerate = false)
    val seasonId: Int,
    val className: String,
    val seasonImg: String,
)
