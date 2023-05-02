package com.example.data

import com.example.domain.entity.DivisionEntity
import com.example.domain.entity.MatchTypeEntity
import com.example.domain.entity.SeasonIdEntity
import com.example.domain.entity.SpIdEntity
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
}