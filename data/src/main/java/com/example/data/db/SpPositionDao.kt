package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entity.SpPositionData

@Dao
interface SpPositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSpPosition(list: List<SpPositionData>)

    @Query("SELECT * FROM spPosition")
    fun getSpPosition(): List<SpPositionData>

    @Query("SELECT desc FROM spPosition WHERE spPosition = :spPosition")
    fun getFindDesc(spPosition: Int): String
}