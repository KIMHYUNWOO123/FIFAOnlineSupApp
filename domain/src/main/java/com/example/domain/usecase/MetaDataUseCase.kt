package com.example.domain.usecase

import com.example.domain.DaoRepository
import com.example.domain.Repository
import com.example.domain.SharedPreferencesService
import com.example.domain.entity.MatchTypeData
import com.example.domain.entity.MatchTypeEntity
import javax.inject.Inject

class MetaDataUseCase @Inject constructor(
    private val daoRepository: DaoRepository,
    private val repository: Repository,
    private val pref: SharedPreferencesService
) {
    suspend fun insertMatch(list: List<MatchTypeData>) {
        daoRepository.insertTypeMatch(list)
    }

    suspend fun getMatch(): List<MatchTypeData> {
        return daoRepository.getTypeMatch()
    }

    suspend fun deleteMatch() {
        daoRepository.deleteMatch()
    }

    suspend fun apiMatchData(): List<MatchTypeEntity> {
        return repository.getMatchData()
    }

    fun setMatchSaveTime(time: String) {
        pref.setMatchSaveTime(time)
    }

    fun getMatchSaveTime(): String {
        return pref.getMatchSaveTime()
    }
}