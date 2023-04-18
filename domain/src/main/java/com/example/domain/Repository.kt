package com.example.domain

import com.example.domain.entity.*

interface Repository {
    suspend fun getUserData(nickname: String): UserEntity
    suspend fun getBestRank(accessId: String): List<BestRankEntity>

    suspend fun getMatchData(): List<MatchTypeEntity>
    suspend fun getDivisionData(): List<DivisionEntity>

    suspend fun getMatchRecord(accessId: String, matchType: Int): List<String>

    suspend fun getDetailMatchRecord(matchId: String): DetailMatchRecordEntity
}