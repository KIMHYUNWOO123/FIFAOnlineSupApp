package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.domain.entity.DivisionData

@Database(
    entities = [DivisionData::class],
    version = 1,
    exportSchema = false
)
abstract class DivisionDatabase : RoomDatabase() {
    abstract fun divisionDao(): DivisionDao
}