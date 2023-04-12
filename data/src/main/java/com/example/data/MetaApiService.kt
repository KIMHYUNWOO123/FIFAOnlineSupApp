package com.example.data

import com.example.domain.entity.MatchTypeEntity
import retrofit2.http.GET

interface MetaApiService {

    @GET("matchtype.json")
    suspend fun getMatchTypeJson(): List<MatchTypeEntity>
}