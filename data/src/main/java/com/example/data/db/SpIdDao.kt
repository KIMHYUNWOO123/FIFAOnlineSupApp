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

    @Query("SELECT * FROM spId WHERE name LIKE :name ")
    fun searchSpId(name: String): List<SpIdData>

    @Query("SELECT name FROM spId WHERE id = :id")
    fun searchName(id: Int): String
}