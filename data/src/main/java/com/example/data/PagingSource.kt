package com.example.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.map.Mapper
import com.example.domain.entity.MatchDetailData
import retrofit2.HttpException
import java.io.IOException

class PagingSource(
    private val apiService: ApiService, private val accessId: String, private val matchType: Int
) : PagingSource<Int, MatchDetailData>() {
    private val mapper = Mapper()
    override fun getRefreshKey(state: PagingState<Int, MatchDetailData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchDetailData> {
        try {
            val nextPageNumber = params.key ?: 0
            val response =
                apiService.getMatchRecord1(accessId = accessId, matchType = matchType, offset = if (nextPageNumber == 0) 0 else nextPageNumber * PAGING_SIZE, PAGING_SIZE)
            val prevKey = if (nextPageNumber == 0) null else nextPageNumber - 1
            val nextKey = if (response.size < 30) null else (nextPageNumber + 1)
            val detailData = mutableListOf<MatchDetailData>()
            for ((i, item) in response.withIndex()) {
                val result = apiService.getDetailMatchRecord(item)
                val map = mapper.detailDataMap(accessId, result)
                detailData.add(i, map)
            }
            val list = detailData.toList()
            return LoadResult.Page(
                data = list, prevKey = prevKey, nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        const val PAGING_SIZE = 30
    }
}