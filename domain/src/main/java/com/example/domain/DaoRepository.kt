package com.example.domain

import com.example.domain.entity.DivisionData
import com.example.domain.entity.MatchTypeData

interface DaoRepository {
    suspend fun insertTypeMatch(list: List<MatchTypeData>)
    suspend fun getTypeMatch(): List<MatchTypeData>

    suspend fun deleteMatch()

    suspend fun insertDivision(list: List<DivisionData>)
    suspend fun getDivision(): List<DivisionData>

}