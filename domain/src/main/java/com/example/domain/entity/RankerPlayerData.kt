package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class RankerPlayerData(
    val spId: Int = 0,
    val spPositionInt: Int = 0,
    val spPosition: String = "",
    val status: RankerPlayerStatusData,
    val createDate: String,
)

@Keep
data class RankerPlayerStatusData(
    val shoot: Float = 0f,
    val effectiveShoot: Float = 0f,
    val validShootPercentage: Int = 0,
    val assist: Float = 0f,
    val goal: Float = 0f,
    val dribble: Float = 0f,
    val dribbleTry: Float = 0f,
    val dribbleSuccess: Float = 0f,
    val validDribblePercentage: Int = 0,
    val passTry: Float = 0f,
    val passSuccess: Float = 0f,
    val validPassPercentage: Int = 0,
    val block: Float = 0f,
    val tackle: Float = 0f,
    val matchCount: Int = 0,
)