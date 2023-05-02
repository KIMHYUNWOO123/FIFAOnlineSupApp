package com.example.domain.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SpPositionEntity(
    @SerializedName("spposition")
    val spPosition: Int,
    val desc: String
)
