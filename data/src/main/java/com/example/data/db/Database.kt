package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.domain.entity.DivisionData
import com.example.domain.entity.MatchTypeData
import com.example.domain.entity.SeasonIdData
import com.example.domain.entity.SpIdData

@Database(
    entities = [DivisionData::class, MatchTypeData::class, SeasonIdData::class, SpIdData::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun divisionDao(): DivisionDao

    abstract fun matchTypeDao(): MatchTypeDao

    abstract fun seasonIdDao(): SeasonIdDao

    abstract fun SpIdDao(): SpIdDao
}