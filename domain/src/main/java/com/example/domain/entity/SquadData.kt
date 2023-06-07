package com.example.domain.entity

import androidx.annotation.Keep


@Keep
data class SquadData(
    val nickname1: String = "-",
    val nickname2: String = "-",
    val squad1: List<PlayerInfo>,
    val squad2: List<PlayerInfo>,
)

@Keep
data class PlayerInfo(
    val name: String = "-",
    val position: String = "-",
    val positionInt: Int = 0,
    val grade: Int = 0,
    val seasonImg: String = "",
    val isMvp: Boolean = false,
    val pid: Int = 0,
)
