package com.example.data

import com.example.data.db.Database
import com.example.domain.DaoRepository
import com.example.domain.entity.DivisionData
import com.example.domain.entity.MatchTypeData
import com.example.domain.entity.SeasonIdData
import com.example.domain.entity.SpIdData
import javax.inject.Inject

class DaoRepositoryImpl @Inject constructor(
    private val db: Database,
) : DaoRepository {
    override suspend fun insertTypeMatch(list: List<MatchTypeData>) {
        db.matchTypeDao().insertMatchType(list)
    }

    override suspend fun getTypeMatch(): List<MatchTypeData> {
        return db.matchTypeDao().getMatchTypeList()
    }

    override suspend fun deleteMatch() {
        db.matchTypeDao().deleteMatchType()
    }

    override suspend fun insertDivision(list: List<DivisionData>) {
        db.divisionDao().insertDivision(list)
    }

    override suspend fun getDivision(): List<DivisionData> {
        return db.divisionDao().getDivision()
    }

    override suspend fun insertSpId(list: List<SpIdData>) {
        db.SpIdDao().insertSpId(list)
    }

    override suspend fun getSpId(): List<SpIdData> {
        return db.SpIdDao().getSpId()
    }

    override suspend fun insertSeasonId(list: List<SeasonIdData>) {
        db.seasonIdDao().insertSeasonId(list)
    }

    override suspend fun getSeasonId(): List<SeasonIdData> {
        return db.seasonIdDao().getSeasonId()
    }


}