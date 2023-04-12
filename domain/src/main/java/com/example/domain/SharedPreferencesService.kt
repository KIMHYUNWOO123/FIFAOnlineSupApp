package com.example.domain

interface SharedPreferencesService {
    fun setMatchSaveTime(time: String)
    fun getMatchSaveTime(): String

}