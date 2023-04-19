package com.example.myapp.map

import com.example.domain.entity.*

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
}