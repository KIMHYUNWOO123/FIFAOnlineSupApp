package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.domain.entity.MatchTypeData

@Database(
    entities = [MatchTypeData::class],
    version = 1,
    exportSchema = false
)
abstract class MatchTypeDatabase : RoomDatabase() {
    abstract fun matchTypeDao(): MatchTypeDao
}