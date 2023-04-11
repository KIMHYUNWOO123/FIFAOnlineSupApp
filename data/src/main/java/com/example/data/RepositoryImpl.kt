package com.example.data

import com.example.domain.Repository
import com.example.domain.entity.BestRankEntity
import com.example.domain.entity.MatchTypeList
import com.example.domain.entity.UserEntity
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val metaApiService: MetaApiService
) : Repository {
    override suspend fun getUserData(nickname: String): UserEntity {
        return apiService.getUser(nickname = nickname)
    }

    override suspend fun getBestRank(accessId: String): List<BestRankEntity> {
        return apiService.getBestRank(accessId)
    }


    // MetaData
    override suspend fun getMatchTypeList(): List<MatchTypeList> {
        return metaApiService.getMatchTypeJson()
    }
}