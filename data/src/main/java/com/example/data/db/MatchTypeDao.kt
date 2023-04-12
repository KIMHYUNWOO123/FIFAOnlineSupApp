package com.example.data.db

import androidx.room.*
import com.example.domain.entity.MatchTypeData
import com.example.domain.entity.MatchTypeEntity


@Dao
interface MatchTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchType(list: List<MatchTypeData>)

    @Query("SELECT * FROM match")
    suspend fun getMatchTypeList(): List<MatchTypeData>

    @Query("DELETE FROM match")
    suspend fun deleteMatchType()
}