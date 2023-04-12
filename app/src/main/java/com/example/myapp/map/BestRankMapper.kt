package com.example.myapp.map

import com.example.domain.entity.BestRankEntity
import com.example.domain.entity.BestRankList
import com.example.domain.entity.MatchTypeData

class BestRankMapper {
    fun map(data: List<BestRankEntity>, matchTypeDataList: List<MatchTypeData>): List<BestRankList> {
        val list = mutableListOf<BestRankList>()

        data.forEachIndexed { i, data ->
            matchTypeDataList.forEach { match ->
                if (data.matchType == match.matchType) {
                    list.add(
                        i, BestRankList(
                            matchType = match.desc, division = "", date = data.achievementDate
                        )
                    )
                }
            }
        }

        return list.toList()
    }
}