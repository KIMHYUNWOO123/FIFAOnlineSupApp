package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class RankerPlayerEntity(
    val spId: Int = 0,
    val spPosition: Int = 0,
    val status: RankerPlayerStatus,
    val createDate: String,
)

@Keep
data class RankerPlayerStatus(
    val shoot: Float = 0f,
    val effectiveShoot: Float = 0f,
    val assist: Float = 0f,
    val goal: Float = 0f,
    val dribble: Float = 0f,
    val dribbleTry: Float = 0f,
    val dribbleSuccess: Float = 0f,
    val passTry: Float = 0f,
    val passSuccess: Float = 0f,
    val block: Float = 0f,
    val tackle: Float = 0f,
    val matchCount: Int = 0,
)
