package com.example.data

import com.example.data.db.DivisionDatabase
import com.example.data.db.MatchTypeDatabase
import com.example.domain.DaoRepository
import com.example.domain.entity.DivisionData
import com.example.domain.entity.MatchTypeData
import javax.inject.Inject

class DaoRepositoryImpl @Inject constructor(
    private val mDb: MatchTypeDatabase,
    private val dDb: DivisionDatabase,
) : DaoRepository {
    override suspend fun insertTypeMatch(list: List<MatchTypeData>) {
        mDb.matchTypeDao().insertMatchType(list)
    }

    override suspend fun getTypeMatch(): List<MatchTypeData> {
        return mDb.matchTypeDao().getMatchTypeList()
    }

    override suspend fun deleteMatch() {
        mDb.matchTypeDao().deleteMatchType()
    }

    override suspend fun insertDivision(list: List<DivisionData>) {
        dDb.divisionDao().insertDivision(list)
    }

    override suspend fun getDivision(): List<DivisionData> {
        return dDb.divisionDao().getDivision()
    }

}