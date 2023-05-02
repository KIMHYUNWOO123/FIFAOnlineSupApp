package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.domain.entity.*

@Database(
    entities = [DivisionData::class, MatchTypeData::class, SeasonIdData::class, SpIdData::class, SpPositionData::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun divisionDao(): DivisionDao

    abstract fun matchTypeDao(): MatchTypeDao

    abstract fun seasonIdDao(): SeasonIdDao

    abstract fun spIdDao(): SpIdDao

    abstract fun spPositionDao(): SpPositionDao
}