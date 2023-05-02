package com.example.data

import com.example.domain.entity.*
import retrofit2.http.GET

interface MetaApiService {

    @GET("matchtype.json")
    suspend fun getMatchTypeJson(): List<MatchTypeEntity>

    @GET("division.json")
    suspend fun getDivisionJson(): List<DivisionEntity>

    @GET("spid.json")
    suspend fun getSpIdJson(): List<SpIdEntity>

    @GET("seasonid.json")
    suspend fun getSeasonIdJson(): List<SeasonIdEntity>

    @GET("spposition.json")
    suspend fun getSpPositionJson(): List<SpPositionEntity>
}