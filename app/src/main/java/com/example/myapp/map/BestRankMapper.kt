package com.example.myapp.map

import com.example.domain.entity.BestRankEntity
import com.example.domain.entity.BestRankList
import com.example.domain.entity.DivisionData
import com.example.domain.entity.MatchTypeData

class BestRankMapper {
    fun map(data: List<BestRankEntity>, matchTypeDataList: List<MatchTypeData>, divisionDataList: List<DivisionData>): List<BestRankList> {
        val list = mutableListOf<BestRankList>()
        for ((i, item) in data.withIndex()) {
            val matchType = matchTypeDataList.filter { item.matchType == it.matchType }.map { it.desc }
            val division = divisionDataList.filter { item.division == it.divisionId }.map { it.divisionName }

            list.add(
                i,
                BestRankList(
                    matchType = matchType.joinToString(),
                    division = division.joinToString(),
                    date = item.achievementDate
                )
            )
        }
        return list.toList()
    }
}