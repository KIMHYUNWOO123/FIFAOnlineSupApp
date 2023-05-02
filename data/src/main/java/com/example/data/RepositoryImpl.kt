package com.example.data

import com.example.domain.Repository
import com.example.domain.entity.*
import javax.inject.Inject
import javax.inject.Named

class RepositoryImpl @Inject constructor(
    @Named("api") private val apiService: ApiService,
    @Named("meta") private val metaApiService: MetaApiService
) : Repository {
    override suspend fun getUserData(nickname: String): UserEntity {
        return apiService.getUser(nickname = nickname)
    }

    override suspend fun getBestRank(accessId: String): List<BestRankEntity> {
        return apiService.getBestRank(accessId)
    }

    override suspend fun getMatchRecord(accessId: String, matchType: Int): List<String> {
        return apiService.getMatchRecord(accessId = accessId, matchType = matchType)
    }

    override suspend fun getDetailMatchRecord(matchId: String): DetailMatchRecordEntity {
        return apiService.getDetailMatchRecord(matchId)
    }

    override suspend fun getTransactionRecord(accessId: String, tradeType: String): List<TransactionRecordEntity> {
        return apiService.getTransactionRecord(accessId = accessId, tradeType = tradeType)
    }

    // Meta
    override suspend fun getMatchData(): List<MatchTypeEntity> {
        return metaApiService.getMatchTypeJson()
    }

    override suspend fun getDivisionData(): List<DivisionEntity> {
        return metaApiService.getDivisionJson()
    }

    override suspend fun getSpIdData(): List<SpIdEntity> {
        return metaApiService.getSpIdJson()
    }

    override suspend fun getSeasonIdData(): List<SeasonIdEntity> {
        return metaApiService.getSeasonIdJson()
    }


}