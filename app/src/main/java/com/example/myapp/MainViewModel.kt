package com.example.myapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.DivisionData
import com.example.domain.entity.DivisionEntity
import com.example.domain.entity.MatchTypeData
import com.example.domain.entity.MatchTypeEntity
import com.example.domain.usecase.MetaDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
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
                withContext(Dispatchers.IO) {
                    matchResult.let {
                        useCase.insertMatch(matchMap(it))
                    }
                    divisionResult.let {
                        useCase.insertDivision(divisionMap(it))
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

    private fun isValuedSaveTime(): Boolean {
        val currentTime = System.currentTimeMillis()
        val saveTime = useCase.getMatchSaveTime().toLong()

        val time = currentTime - saveTime
        val dataFormat = SimpleDateFormat("dd")
        val day = dataFormat.format(time).toInt()
        return day < 30
    }

}