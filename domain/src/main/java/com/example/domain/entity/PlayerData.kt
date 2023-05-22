package com.example.domain.entity

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
@Keep
data class PlayerData(
    val id: Int,
    val po: Int,
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}