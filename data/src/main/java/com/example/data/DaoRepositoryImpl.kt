package com.example.data

import com.example.data.db.Database
import com.example.domain.DaoRepository
import com.example.domain.entity.*
import javax.inject.Inject

class DaoRepositoryImpl @Inject constructor(
    private val db: Database,
) : DaoRepository {
    override suspend fun insertTypeMatch(list: List<MatchTypeData>) {
        db.matchTypeDao().insertMatchType(list)
    }

    override suspend fun getTypeMatch(limit: Int): List<MatchTypeData> {
        return db.matchTypeDao().getMatchTypeList(limit)
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
        db.spIdDao().insertSpId(list)
    }

    override suspend fun getSpId(): List<SpIdData> {
        return db.spIdDao().getSpId()
    }

    override suspend fun getName(id: Int): String {
        return db.spIdDao().searchName(id)
    }

    override suspend fun searchSpId(name: String): List<SpIdData> {
        return db.spIdDao().searchSpId(name)
    }


    override suspend fun insertSeasonId(list: List<SeasonIdData>) {
        db.seasonIdDao().insertSeasonId(list)
    }

    override suspend fun getSeasonId(): List<SeasonIdData> {
        return db.seasonIdDao().getSeasonId()
    }

    override suspend fun insertSpPosition(list: List<SpPositionData>) {
        db.spPositionDao().insertSpPosition(list)
    }

    override suspend fun getSpPosition(): List<SpPositionData> {
        return db.spPositionDao().getSpPosition()
    }

    override suspend fun getFindDesc(spPosition: Int): String {
        return db.spPositionDao().getFindDesc(spPosition)
    }


}