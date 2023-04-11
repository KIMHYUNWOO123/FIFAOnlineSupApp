package com.example.domain

import com.example.domain.entity.BestRankEntity
import com.example.domain.entity.MatchTypeList
import com.example.domain.entity.UserEntity

interface Repository {
    suspend fun getUserData(nickname: String): UserEntity
    suspend fun getBestRank(accessId: String): List<BestRankEntity>




    // MetaData
    suspend fun getMatchTypeList(): List<MatchTypeList>
}