package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class MatchDetailData(
    val nickname1: String = "-",
    val nickname2: String = "-",
    val displayGoal1: String = "0",
    val displayGoal2: String = "0",
    val date: String = "-",
    val result1: String,
    val result2: String,
    val foul1: String = "0",
    val foul2: String = "0",
    val injury1: String = "0",
    val injury2: String = "0",
    val yellowCards1: String = "0",
    val yellowCards2: String = "0",
    val redCards1: String = "0",
    val redCards2: String = "0",
    val possession1: String = "0",
    val possession2: String = "0",
    val offsideCount1: String = "0",
    val offsideCount2: String = "0",
    val averageRating1: String = "0",
    val averageRating2: String = "0",
    val totalShoot1: String = "0",
    val totalShoot2: String = "0",
    val validShoot1: String = "0",
    val validShoot2: String = "0",
    val shootRating1: String = "0",
    val shootRating2: String = "0",
    val goal1: String = "0",
    val goal2: String = "0",
    val validPass1: String = "0",
    val validPass2: String = "0",
    val validDefence1: String = "0",
    val validDefence2: String = "0",
    val validTackle1: String = "0",
    val validTackle2: String = "0",
    val mvpPlayerSpId1: String = "0",
    val mvpPlayerSpId2: String = "0"
)
