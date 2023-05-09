package com.example.domain.usecase

import com.example.domain.entity.RankerPlayerEntity
import com.example.domain.Repository
import javax.inject.Inject

class RankerUseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend fun getRankerPlayerData(matchType: Int, player: String): List<RankerPlayerEntity> {
        return repository.getRankerPlayerData(matchType, player)
    }
}