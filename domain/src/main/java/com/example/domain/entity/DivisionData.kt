package com.example.domain.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "division")
data class DivisionData(
    @PrimaryKey(autoGenerate = false)
    val divisionId: Int,
    val divisionName: String
)
