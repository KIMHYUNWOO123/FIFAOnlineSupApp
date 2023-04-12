package com.example.domain.usecase

import android.content.SharedPreferences
import com.example.domain.Repository
import com.example.domain.SharedPreferencesService
import com.example.domain.entity.BestRankEntity
import com.example.domain.entity.UserEntity
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend fun getUserData(nickname: String): UserEntity {
        return repository.getUserData(nickname)
    }

    suspend fun getBestRank(accessId: String): List<BestRankEntity> {
        return repository.getBestRank(accessId)
    }
}