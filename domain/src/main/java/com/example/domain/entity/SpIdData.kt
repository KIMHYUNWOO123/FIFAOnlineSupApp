package com.example.domain.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "spId")
data class SpIdData(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
)
