package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entity.SpIdData

@Dao
interface SpIdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSpId(list: List<SpIdData>)

    @Query("SELECT * FROM spId")
    fun getSpId(): List<SpIdData>
}