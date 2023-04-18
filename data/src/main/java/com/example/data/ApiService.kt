package com.example.data

import com.example.domain.entity.BestRankEntity
import com.example.domain.entity.DetailMatchRecordEntity
import com.example.domain.entity.UserEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/")
    suspend fun getUser(
        @Query("nickname") nickname: String
    ): UserEntity

    @GET("users/{accessid}/maxdivision")
    suspend fun getBestRank(
        @Path("accessid", encoded = true) accessId: String
    ): List<BestRankEntity>

    @GET("users/{accessid}/matches")
    suspend fun getMatchRecord(
        @Path("accessid", encoded = true) accessId: String,
        @Query("matchtype") matchType: Int,
    ): List<String>

    @GET("matches/{matchid}")
    suspend fun getDetailMatchRecord(
        @Path("matchid", encoded = true) matchId: String
    ): DetailMatchRecordEntity
}