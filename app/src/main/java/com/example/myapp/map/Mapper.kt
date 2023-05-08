package com.example.myapp.map

import com.example.domain.entity.*
import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt

class Mapper {
    fun bestRankMap(data: List<BestRankEntity>, matchTypeDataList: List<MatchTypeData>, divisionDataList: List<DivisionData>): List<BestRankList> {
        val list = mutableListOf<BestRankList>()
        for ((i, item) in data.withIndex()) {
            val matchType = matchTypeDataList.filter { item.matchType == it.matchType }.map { it.desc }
            val division = divisionDataList.filter { item.division == it.divisionId }.map { it.divisionName }
            val date = item.achievementDate
            val removedDate = date.removeRange(0, 2)
            val slicedDate = removedDate.split("T")
            val resultDate = "${slicedDate[0]}  ${slicedDate[1]}"
            list.add(
                i,
                BestRankList(
                    matchType = matchType.joinToString(),
                    division = division.joinToString(),
                    date = resultDate
                )
            )

        }
        return list.toList()
    }

    fun displayMatchDataMap(accessId: String, list: List<DetailMatchRecordEntity>): List<DisplayMatchData> {
        val displayList = mutableListOf<DisplayMatchData>()
        for ((i, it) in list.withIndex()) {
            val isFirst = it.matchInfo[0].accessId == accessId
            val matchId = it.matchId
            val nickname1 = if (isFirst) it.matchInfo[0].nickname else it.matchInfo[1].nickname
            val nickname2 = if (isFirst) it.matchInfo[1].nickname else it.matchInfo[0].nickname
            val goal1 = if (isFirst) it.matchInfo[0].shoot.goalTotalDisplay else it.matchInfo[1].shoot.goalTotalDisplay
            val goal2 = if (isFirst) it.matchInfo[1].shoot.goalTotalDisplay else it.matchInfo[0].shoot.goalTotalDisplay
            val result = if (isFirst) it.matchInfo[0].matchDetail.matchResult else it.matchInfo[1].matchDetail.matchResult
            val date = it.matchDate

            displayList.add(
                i, DisplayMatchData(
                    isFirst = isFirst,
                    matchId = matchId,
                    nickname1 = nickname1,
                    nickname2 = nickname2,
                    goal1 = goal1.toString(),
                    goal2 = goal2.toString(),
                    result = result,
                    date = date
                )
            )
        }
        return displayList.toList()
    }

    fun detailDataMap(isFirst: Boolean, list: DetailMatchRecordEntity): List<DetailMapData> {
        val returnList = mutableListOf<DetailMapData>()
        for (i in 0..1) {
            list.matchInfo[i].let {
                val result = when (it.matchDetail.matchEndType) { // 0: 정상종료, 1: 몰수승 ,2:몰수패
                    0 -> it.matchDetail.matchResult
                    1 -> "몰수승"
                    2 -> "몰수패"
                    else -> it.matchDetail.matchResult
                }
                if (it.player.isNotEmpty() && it.shootDetail.isNotEmpty()) {
                    val foul = it.matchDetail.foul
                    val injury = it.matchDetail.injury
                    val yellowCards = it.matchDetail.yellowCards
                    val redCards = it.matchDetail.redCards
                    val possession = it.matchDetail.possession
                    val offsideCount = it.matchDetail.offsideCount
                    val averageRating = it.matchDetail.averageRating
                    val average = String.format("%.2f", averageRating).toDouble()
                    val totalShoot = it.shoot.shootTotal
                    val validShoot = it.shoot.effectiveShootTotal
                    val shootRating = if (it.shoot.goalTotal != 0) ((it.shoot.goalTotal.toFloat() / it.shoot.shootTotal.toFloat()) * 100.0f).roundToInt() else 0
                    val goal = it.shoot.goalTotal
                    val validPass = if (it.pass.passSuccess != 0) {
                        ((it.pass.passSuccess.toFloat() / it.pass.passTry.toFloat()) * 100.0f).roundToInt()
                    } else 0
                    val validDefence = if (it.defence.blockSuccess != 0) ((it.defence.blockSuccess.toFloat() / it.defence.blockTry.toFloat()) * 100.0f).roundToInt() else 0
                    val validTackle = if (it.defence.tackleSuccess != 0) ((it.defence.tackleSuccess.toFloat() / it.defence.tackleTry.toFloat()) * 100.0f).roundToInt() else 0
                    var max = 0.0f
                    var maxIndex = 0
                    for ((i, item) in it.player.withIndex()) {
                        val rating = item.status.spRating
                        if (rating > max) {
                            max = rating
                            maxIndex = i
                        }
                    }
                    val mvpPlayerSpId = it.player[maxIndex].spId
                    returnList.add(
                        i, DetailMapData(
                            result = result,
                            foul = foul.toString(),
                            injury = injury.toString(),
                            yellowCards = yellowCards.toString(),
                            redCards = redCards.toString(),
                            possession = possession.toString(),
                            offsideCount = offsideCount.toString(),
                            averageRating = average.toString(),
                            totalShoot = totalShoot.toString(),
                            validShoot = validShoot.toString(),
                            goal = goal.toString(),
                            shootRating = shootRating.toString(),
                            validPass = validPass.toString(),
                            validDefence = validDefence.toString(),
                            validTackle = validTackle.toString(),
                            mvpPlayerSpId = mvpPlayerSpId.toString()
                        )
                    )
                } else {
                    returnList.add(
                        i, DetailMapData(
                            result = result
                        )
                    )
                }
            }
        }
        if (!isFirst) {
            Collections.swap(returnList, 0, 1)
        }
        return returnList.toList()
    }

    fun transactionMap(transactionRecord: List<TransactionRecordEntity>, seasonIdData: List<SeasonIdData>, spIdData: List<SpIdData>): List<TransactionData> {
        val list = mutableListOf<TransactionData>()
        for ((i, item) in transactionRecord.withIndex()) {
            val name = spIdData.filter { it.id == item.spId }.map { it.name }
            val data = seasonIdData.filter { it.seasonId == item.spId.toString().substring(0 until 3).toInt() }.map { it }
            val date = item.tradeDate.replace("T", " ")
            val format = DecimalFormat("#,###")
            val value = format.format(item.value)
            list.add(
                i, TransactionData(
                    name = name.joinToString(),
                    className = data[0].className,
                    img = data[0].seasonImg,
                    tradeDate = date,
                    saleSn = item.saleSn,
                    spId = item.spId,
                    grade = item.grade,
                    value = value.toString()
                )
            )
        }
        return list.toList()
    }

    fun searchRankerDataMap(spIdData: List<SpIdData>, seasonIdData: List<SeasonIdData>): List<SearchRankerData> {
        val list = mutableListOf<SearchRankerData>()
        for ((i, item) in spIdData.withIndex()) {
            val image = seasonIdData.filter { it.seasonId == item.id.toString().substring(0 until 3).toInt() }.map { it.seasonImg }
            list.add(
                i, SearchRankerData(
                    name = item.name,
                    image = image.joinToString(),
                    id = item.id
                )
            )
        }
        return list.toList()
    }
}