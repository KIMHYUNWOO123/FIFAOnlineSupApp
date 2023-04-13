package com.example.data

import com.example.domain.Repository
import com.example.domain.entity.BestRankEntity
import com.example.domain.entity.DivisionEntity
import com.example.domain.entity.MatchTypeEntity
import com.example.domain.entity.UserEntity
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

    override suspend fun getMatchData(): List<MatchTypeEntity> {
        return metaApiService.getMatchTypeJson()
    }

    override suspend fun getDivisionData(): List<DivisionEntity> {
        return metaApiService.getDivisionJson()
    }
}