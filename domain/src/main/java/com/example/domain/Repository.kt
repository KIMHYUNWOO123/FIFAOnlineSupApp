package com.example.domain

import com.example.domain.entity.*

interface Repository {
    suspend fun getUserData(nickname: String): UserEntity
    suspend fun getBestRank(accessId: String): List<BestRankEntity>
    suspend fun getMatchRecord(accessId: String, matchType: Int): List<String>

    suspend fun getDetailMatchRecord(matchId: String): DetailMatchRecordEntity

    suspend fun getTransactionRecord(accessId: String, tradeType: String): List<TransactionRecordEntity>

    // Meta
    suspend fun getMatchData(): List<MatchTypeEntity>
    suspend fun getDivisionData(): List<DivisionEntity>

    suspend fun getSpIdData(): List<SpIdEntity>

    suspend fun getSeasonIdData(): List<SeasonIdEntity>

    suspend fun getSpPositionData(): List<SpPositionEntity>
}