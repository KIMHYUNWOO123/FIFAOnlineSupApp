package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entity.DivisionData

@Dao
interface DivisionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDivision(list: List<DivisionData>)

    @Query("SELECT * FROM division")
    fun getDivision(): List<DivisionData>
}