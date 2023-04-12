package com.example.domain

import com.example.domain.entity.MatchTypeData
import com.example.domain.entity.MatchTypeEntity

interface DaoRepository {
    suspend fun insertTypeMatch(list: List<MatchTypeData>)
    suspend fun getTypeMatch(): List<MatchTypeData>

    suspend fun deleteMatch()
}