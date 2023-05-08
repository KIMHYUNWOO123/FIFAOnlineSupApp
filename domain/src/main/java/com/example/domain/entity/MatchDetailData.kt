package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class MatchDetailData(
    val nickname1: String = "-",
    val nickname2: String = "-",
    val displayGoal1: String = "-",
    val displayGoal2: String = "-",
    val date: String = "-",
    val result1: String = "-",
    val result2: String = "-",
    val foul1: String = "-",
    val foul2: String = "-",
    val injury1: String = "-",
    val injury2: String = "-",
    val yellowCards1: String = "-",
    val yellowCards2: String = "-",
    val redCards1: String = "-",
    val redCards2: String = "-",
    val possession1: String = "-",
    val possession2: String = "-",
    val offsideCount1: String = "-",
    val offsideCount2: String = "-",
    val averageRating1: String = "-",
    val averageRating2: String = "-",
    val totalShoot1: String = "-",
    val totalShoot2: String = "-",
    val validShoot1: String = "-",
    val validShoot2: String = "-",
    val shootRating1: String = "-",
    val shootRating2: String = "-",
    val goal1: String = "-",
    val goal2: String = "-",
    val validPass1: String = "-",
    val validPass2: String = "-",
    val validDefence1: String = "-",
    val validDefence2: String = "-",
    val validTackle1: String = "-",
    val validTackle2: String = "-",
    val mvpPlayerSpId1: String = "-",
    val mvpPlayerSpId2: String = "-"
)
