package com.woo.myapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.*
import com.example.domain.usecase.MetaDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MetaDataUseCase,
) : ViewModel() {

    fun insertData() {
        val isSave = useCase.getMatchSaveTime()
        if (isSave.isBlank() || !isValuedSaveTime()) {
            viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { _, t ->
                Log.d("MainViewModel", "Exception : $t")
            }) {
                val matchResult = useCase.apiMatchData()
                val divisionResult = useCase.apiDivisionData()
                val spIdResult = useCase.apiSpIdData()
                val seasonResult = useCase.apiSeasonIdData()
                val spPositionResult = useCase.apiSpPositionData()
                withContext(Dispatchers.IO) {
                    matchResult.let {
                        useCase.insertMatch(matchMap(it))
                    }
                    divisionResult.let {
                        useCase.insertDivision(divisionMap(it))
                    }
                    spIdResult.let {
                        useCase.insertSpId(spIdMap(it))
                    }
                    seasonResult.let {
                        useCase.insertSeasonId(seasonIdMap(it))
                    }
                    spPositionResult.let {
                        useCase.insertSpPosition(spPositionMap(it))
                    }
                }
            }.invokeOnCompletion {
                val time = System.currentTimeMillis().toString()
                useCase.setMatchSaveTime(time)
            }
        }
    }

    private fun matchMap(data: List<MatchTypeEntity>): List<MatchTypeData> {
        val list = mutableListOf<MatchTypeData>()
        data.forEachIndexed { i, item ->
            list.add(
                i, MatchTypeData(
                    matchType = item.matchType,
                    desc = item.desc
                )
            )
        }
        return list.toList()
    }

    private fun divisionMap(data: List<DivisionEntity>): List<DivisionData> {
        val list = mutableListOf<DivisionData>()
        data.forEachIndexed { i, item ->
            list.add(
                i, DivisionData(
                    divisionId = item.divisionId,
                    divisionName = item.divisionName
                )
            )
        }
        return list.toList()
    }

    private fun spIdMap(data: List<SpIdEntity>): List<SpIdData> {
        val list = mutableListOf<SpIdData>()
        data.forEachIndexed { i, item ->
            list.add(
                i, SpIdData(
                    id = item.id,
                    name = item.name
                )
            )
        }
        return list.toList()
    }

    private fun seasonIdMap(data: List<SeasonIdEntity>): List<SeasonIdData> {
        val list = mutableListOf<SeasonIdData>()
        data.forEachIndexed { i, item ->
            list.add(
                i, SeasonIdData(
                    seasonId = item.seasonId,
                    className = item.className,
                    seasonImg = item.seasonImg
                )
            )
        }
        return list.toList()
    }

    private fun spPositionMap(data: List<SpPositionEntity>): List<SpPositionData> {
        val list = mutableListOf<SpPositionData>()
        data.forEachIndexed { i, item ->
            list.add(
                i, SpPositionData(
                    spPosition = item.spPosition,
                    desc = item.desc
                )
            )
        }
        return list.toList()
    }

    private fun isValuedSaveTime(): Boolean {
        val currentTime = System.currentTimeMillis()
        val saveTime = useCase.getMatchSaveTime().toLong()
        val milliseconds = currentTime - saveTime
        val days = milliseconds / (24 * 60 * 60 * 1000)
        return days < 30
    }

}