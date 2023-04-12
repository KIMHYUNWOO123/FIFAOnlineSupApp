package com.example.data

import com.example.data.db.MatchTypeDatabase
import com.example.domain.DaoRepository
import com.example.domain.entity.MatchTypeData
import com.example.domain.entity.MatchTypeEntity
import javax.inject.Inject

class DaoRepositoryImpl @Inject constructor(
    private val db: MatchTypeDatabase
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

}