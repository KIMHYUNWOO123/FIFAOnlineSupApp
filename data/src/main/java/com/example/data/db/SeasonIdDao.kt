package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entity.SeasonIdData

@Dao
interface SeasonIdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeasonId(list: List<SeasonIdData>)

    @Query("SELECT * FROM seasonId")
    fun getSeasonId(): List<SeasonIdData>
}