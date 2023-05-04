package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.domain.Repository
import com.example.domain.entity.DetailMatchRecordEntity
import com.example.domain.entity.MatchDetailData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MatchUseCase @Inject constructor(
    private val repository: Repository,
) {
    suspend fun getMatchRecord(accessId: String, matchType: Int): List<String> {
        return repository.getMatchRecord(accessId = accessId, matchType = matchType)
    }

    suspend fun getDetailMatchRecord(matchId: String): DetailMatchRecordEntity {
        return repository.getDetailMatchRecord(matchId)
    }

    suspend fun getMatchRecordPagingData(accessId: String, matchType: Int): Flow<PagingData<MatchDetailData>> {
        return repository.getMatchRecordPagingData(accessId, matchType)
    }
}