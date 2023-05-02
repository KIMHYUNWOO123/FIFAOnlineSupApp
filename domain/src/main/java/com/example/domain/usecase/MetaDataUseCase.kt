package com.example.domain.usecase

import com.example.domain.DaoRepository
import com.example.domain.Repository
import com.example.domain.SharedPreferencesService
import com.example.domain.entity.*
import javax.inject.Inject

class MetaDataUseCase @Inject constructor(
    private val daoRepository: DaoRepository,
    private val repository: Repository,
    private val pref: SharedPreferencesService
) {

    // Dao
    suspend fun insertMatch(list: List<MatchTypeData>) {
        daoRepository.insertTypeMatch(list)
    }

    suspend fun getMatch(): List<MatchTypeData> {
        return daoRepository.getTypeMatch()
    }

    suspend fun deleteMatch() {
        daoRepository.deleteMatch()
    }

    suspend fun insertDivision(list: List<DivisionData>) {
        daoRepository.insertDivision(list)
    }

    suspend fun getDivision(): List<DivisionData> {
        return daoRepository.getDivision()
    }

    suspend fun insertSpId(list: List<SpIdData>) {
        daoRepository.insertSpId(list)
    }

    suspend fun getSpId(): List<SpIdData> {
        return daoRepository.getSpId()
    }

    suspend fun insertSeasonId(list: List<SeasonIdData>) {
        daoRepository.insertSeasonId(list)
    }

    suspend fun getSeasonId(): List<SeasonIdData> {
        return daoRepository.getSeasonId()
    }

    suspend fun insertSpPosition(list: List<SpPositionData>) {
        daoRepository.insertSpPosition(list)
    }

    suspend fun getSpPosition(): List<SpPositionData> {
        return daoRepository.getSpPosition()
    }

    suspend fun getFindDesc(spPosition: Int): String {
        return daoRepository.getFindDesc(spPosition)
    }

    // api
    suspend fun apiMatchData(): List<MatchTypeEntity> {
        return repository.getMatchData()
    }

    suspend fun apiDivisionData(): List<DivisionEntity> {
        return repository.getDivisionData()
    }

    suspend fun apiSpIdData(): List<SpIdEntity> {
        return repository.getSpIdData()
    }

    suspend fun apiSeasonIdData(): List<SeasonIdEntity> {
        return repository.getSeasonIdData()
    }

    suspend fun apiSpPositionData(): List<SpPositionEntity> {
        return repository.getSpPositionData()
    }

    // pref
    fun setMatchSaveTime(time: String) {
        pref.setMatchSaveTime(time)
    }

    fun getMatchSaveTime(): String {
        return pref.getMatchSaveTime()
    }

}