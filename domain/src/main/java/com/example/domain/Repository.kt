package com.example.domain

import com.example.domain.entity.BestRankEntity
import com.example.domain.entity.DivisionEntity
import com.example.domain.entity.MatchTypeEntity
import com.example.domain.entity.UserEntity

interface Repository {
    suspend fun getUserData(nickname: String): UserEntity
    suspend fun getBestRank(accessId: String): List<BestRankEntity>

    suspend fun getMatchData(): List<MatchTypeEntity>
    suspend fun getDivisionData(): List<DivisionEntity>
}