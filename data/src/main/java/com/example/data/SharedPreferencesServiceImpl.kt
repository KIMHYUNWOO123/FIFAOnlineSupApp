package com.example.data

import android.content.SharedPreferences
import com.example.domain.SharedPreferencesService
import javax.inject.Inject

class SharedPreferencesServiceImpl @Inject constructor(
    private val pref: SharedPreferences
) : SharedPreferencesService {
    override fun setMatchSaveTime(time: String) {
        pref.edit().putString(PREF_MATCH_SAVE_TIME, time).apply()
    }

    override fun getMatchSaveTime(): String {
        return pref.getString(PREF_MATCH_SAVE_TIME, "").toString()
    }

    companion object {
        const val PREF_MATCH_SAVE_TIME = "PREF_MATCH_SAVE_TIME"
    }
}