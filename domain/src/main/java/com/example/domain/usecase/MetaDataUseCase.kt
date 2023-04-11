package com.example.domain.usecase

import com.example.domain.Repository
import com.example.domain.entity.MatchTypeList
import javax.inject.Inject

class MetaDataUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun getMatchTypeList(): List<MatchTypeList> {
        return repository.getMatchTypeList()
    }
}