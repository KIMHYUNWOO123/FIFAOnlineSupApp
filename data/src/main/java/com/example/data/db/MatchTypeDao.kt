package com.example.data.db

import androidx.room.*
import com.example.domain.entity.MatchTypeList

@Dao
interface MatchTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchType(list: MatchTypeList)
    @Query("SELECT * FROM matchtypelist")
    @Delete
    suspend fun deleteMatchType()
}