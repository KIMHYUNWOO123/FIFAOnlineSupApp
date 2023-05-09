package com.example.data

import com.example.domain.entity.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/")
    suspend fun getUser(
        @Query("nickname") nickname: String,
    ): UserEntity

    @GET("users/{accessid}/maxdivision")
    suspend fun getBestRank(
        @Path("accessid", encoded = true) accessId: String,
    ): List<BestRankEntity>

    @GET("users/{accessid}/matches")
    suspend fun getMatchRecord(
        @Path("accessid", encoded = true) accessId: String,
        @Query("matchtype") matchType: Int,
    ): List<String>

    @GET("matches/{matchid}")
    suspend fun getDetailMatchRecord(
        @Path("matchid", encoded = true) matchId: String,
    ): DetailMatchRecordEntity

    @GET("users/{accessid}/markets")
    suspend fun getTransactionRecord(
        @Path("accessid", encoded = true) accessId: String,
        @Query("tradetype") tradeType: String,
    ): List<TransactionRecordEntity>

    @GET("users/{accessid}/matches")
    suspend fun getMatchRecord1(
        @Path("accessid", encoded = true) accessId: String,
        @Query("matchtype") matchType: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): List<String>

    @GET("rankers/status")
    suspend fun getRankerUsedPlayer(
        @Query("matchtype") matchType: Int,
        @Query("players") player: String,
    ): List<RankerPlayerEntity>
}