package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class DetailMapData(
    val result: String,
    val foul: String = "0",
    val injury: String = "0",
    val yellowCards: String = "0",
    val redCards: String = "0",
    val possession: String = "0",
    val offsideCount: String = "0",
    val averageRating: String = "0",
    val totalShoot: String = "0",
    val validShoot: String = "0",
    val goal: String = "0",
    val validPass: String = "0",
    val validDefence: String = "0",
    val validTackle: String = "0",
    val mvpPlayerSpId: String = "0"
)
