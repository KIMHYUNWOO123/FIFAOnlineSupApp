package com.example.domain

import com.example.domain.entity.*

interface DaoRepository {
    suspend fun insertTypeMatch(list: List<MatchTypeData>)
    suspend fun getTypeMatch(limit: Int = 234): List<MatchTypeData>
    suspend fun deleteMatch()
    suspend fun insertDivision(list: List<DivisionData>)
    suspend fun getDivision(): List<DivisionData>
    suspend fun insertSpId(list: List<SpIdData>)
    suspend fun getSpId(): List<SpIdData>
    suspend fun searchSpId(name: String): List<SpIdData>
    suspend fun insertSeasonId(list: List<SeasonIdData>)
    suspend fun getSeasonId(): List<SeasonIdData>
    suspend fun insertSpPosition(list: List<SpPositionData>)
    suspend fun getSpPosition(): List<SpPositionData>
    suspend fun getFindDesc(spPosition: Int): String
}