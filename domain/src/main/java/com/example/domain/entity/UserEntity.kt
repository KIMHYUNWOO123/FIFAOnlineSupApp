package com.example.domain.entity

import androidx.annotation.Keep


@Keep
data class UserEntity(
    val accessId: String,
    val nickname: String,
    val level: Int
)
