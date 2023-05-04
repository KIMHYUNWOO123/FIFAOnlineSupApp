package com.example.data.map

import com.example.domain.entity.DetailMatchRecordEntity
import com.example.domain.entity.MatchDetailData
import java.util.*
import kotlin.math.roundToInt

class Mapper {
    fun detailDataMap(accessId: String, list: DetailMatchRecordEntity): MatchDetailData {
        val isFirst = list.matchInfo[0].accessId == accessId
        val dataList = list.matchInfo
        if (!isFirst) {
            Collections.swap(dataList, 0, 1)
        }
        val matchDate = list.matchDate
        val nickname1 = dataList[0].nickname
        val nickname2 = dataList[1].nickname
        val displayGoal1 = dataList[0].shoot.goalTotalDisplay
        val displayGoal2 = dataList[1].shoot.goalTotalDisplay
        val result1 = when (dataList[0].matchDetail.matchEndType) { // 0: 정상종료, 1: 몰수승 ,2:몰수패
            0 -> dataList[0].matchDetail.matchResult
            1 -> "몰수승"
            2 -> "몰수패"
            else -> dataList[0].matchDetail.matchResult
        }
        val foul1 = dataList[0].matchDetail.foul
        val injury1 = dataList[0].matchDetail.injury
        val yellowCards1 = dataList[0].matchDetail.yellowCards
        val redCards1 = dataList[0].matchDetail.redCards
        val possession1 = dataList[0].matchDetail.possession
        val offsideCount1 = dataList[0].matchDetail.offsideCount
        val averageRating1 = dataList[0].matchDetail.averageRating
        val average1 = String.format("%.2f", averageRating1).toDouble()
        val totalShoot1 = dataList[0].shoot.shootTotal
        val validShoot1 = dataList[0].shoot.effectiveShootTotal
        val shootRating1 = if (dataList[0].shoot.goalTotal != 0) ((dataList[0].shoot.goalTotal.toFloat() / dataList[0].shoot.shootTotal.toFloat()) * 100.0f).roundToInt() else 0
        val goal1 = dataList[0].shoot.goalTotal
        val validPass1 = if (dataList[0].pass.passSuccess != 0) {
            ((dataList[0].pass.passSuccess.toFloat() / dataList[0].pass.passTry.toFloat()) * 100.0f).roundToInt()
        } else 0
        val validDefence1 = if (dataList[0].defence.blockSuccess != 0) ((dataList[0].defence.blockSuccess.toFloat() / dataList[0].defence.blockTry.toFloat()) * 100.0f).roundToInt() else 0
        val validTackle1 = if (dataList[0].defence.tackleSuccess != 0) ((dataList[0].defence.tackleSuccess.toFloat() / dataList[0].defence.tackleTry.toFloat()) * 100.0f).roundToInt() else 0
        var max = 0.0f
        var maxIndex = 0
        for ((i, item) in dataList[0].player.withIndex()) {
            val rating = item.status.spRating
            if (rating > max) {
                max = rating
                maxIndex = i
            }
        }
//        val mvpPlayerSpId1 = dataList[0].player[maxIndex].spId
        val result2 = when (dataList[1].matchDetail.matchEndType) { // 0: 정상종료, 1: 몰수승 ,2:몰수패
            0 -> dataList[1].matchDetail.matchResult
            1 -> "몰수승"
            2 -> "몰수패"
            else -> dataList[1].matchDetail.matchResult
        }
        val foul2 = dataList[1].matchDetail.foul
        val injury2 = dataList[1].matchDetail.injury
        val yellowCards2 = dataList[1].matchDetail.yellowCards
        val redCards2 = dataList[1].matchDetail.redCards
        val possession2 = dataList[1].matchDetail.possession
        val offsideCount2 = dataList[1].matchDetail.offsideCount
        val averageRating2 = dataList[1].matchDetail.averageRating
        val average2 = String.format("%.2f", averageRating2).toDouble()
        val totalShoot2 = dataList[1].shoot.shootTotal
        val validShoot2 = dataList[1].shoot.effectiveShootTotal
        val shootRating2 = if (dataList[1].shoot.goalTotal != 0) ((dataList[1].shoot.goalTotal.toFloat() / dataList[1].shoot.shootTotal.toFloat()) * 100.0f).roundToInt() else 0
        val goal2 = dataList[1].shoot.goalTotal
        val validPass2 = if (dataList[1].pass.passSuccess != 0) {
            ((dataList[1].pass.passSuccess.toFloat() / dataList[1].pass.passTry.toFloat()) * 100.0f).roundToInt()
        } else 0
        val validDefence2 = if (dataList[1].defence.blockSuccess != 0) ((dataList[1].defence.blockSuccess.toFloat() / dataList[1].defence.blockTry.toFloat()) * 100.0f).roundToInt() else 0
        val validTackle2 = if (dataList[1].defence.tackleSuccess != 0) ((dataList[1].defence.tackleSuccess.toFloat() / dataList[1].defence.tackleTry.toFloat()) * 100.0f).roundToInt() else 0
        var max2 = 0.0f
        var maxIndex2 = 0
        for ((i, item) in dataList[1].player.withIndex()) {
            val rating = item.status.spRating
            if (rating > max) {
                max = rating
                maxIndex = i
            }
        }
//        val mvpPlayerSpId2 = dataList[1].player[maxIndex2].spId
        return MatchDetailData(
            nickname1 = nickname1,
            nickname2 = nickname2,
            displayGoal1 = displayGoal1.toString(),
            displayGoal2 = displayGoal2.toString(),
            date = matchDate,
            result1 = result1,
            result2 = result2,
            foul1 = foul1.toString(),
            foul2 = foul2.toString(),
            injury1 = injury1.toString(),
            injury2 = injury2.toString(),
            yellowCards1 = yellowCards1.toString(),
            yellowCards2 = yellowCards2.toString(),
            redCards1 = redCards1.toString(),
            redCards2 = redCards2.toString(),
            possession1 = possession1.toString(),
            possession2 = possession2.toString(),
            offsideCount1 = offsideCount1.toString(),
            offsideCount2 = offsideCount2.toString(),
            averageRating1 = average1.toString(),
            averageRating2 = average2.toString(),
            totalShoot1 = totalShoot1.toString(),
            totalShoot2 = totalShoot2.toString(),
            shootRating1 = shootRating1.toString(),
            shootRating2 = shootRating2.toString(),
            goal1 = goal1.toString(),
            goal2 = goal2.toString(),
            validPass1 = validPass1.toString(),
            validPass2 = validPass2.toString(),
            validDefence1 = validDefence1.toString(),
            validDefence2 = validDefence2.toString(),
            validTackle1 = validTackle1.toString(),
            validShoot2 = validTackle2.toString(),
            mvpPlayerSpId1 = "0",
            mvpPlayerSpId2 = "0"
        )
    }
}