package com.example.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.map.Mapper
import com.example.domain.entity.MatchDetailData
import retrofit2.HttpException
import java.io.IOException

class PagingSource(
    private val apiService: ApiService,
    private val accessId: String,
    private val matchType: Int
) : PagingSource<Int, MatchDetailData>() {
    private val mapper = Mapper()
    override fun getRefreshKey(state: PagingState<Int, MatchDetailData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(30)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(30)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchDetailData> {
        try {
            val nextPageNumber = params.key ?: STARTING_PAGE_INDEX
            val response =
                apiService.getMatchRecord1(accessId = accessId, matchType = matchType, offset = if (nextPageNumber == STARTING_PAGE_INDEX) 0 else nextPageNumber * PAGING_SIZE, PAGING_SIZE)
            val prevKey = if (nextPageNumber == STARTING_PAGE_INDEX) null else nextPageNumber - 1
            val nextKey = if (response.size != 30) null else nextPageNumber + (params.loadSize / PAGING_SIZE)
            val detailData = mutableListOf<MatchDetailData>()
            for ((i, item) in response.withIndex()) {
                val result = apiService.getDetailMatchRecord(item)
                val map = mapper.detailDataMap(accessId, result)
                detailData.add(i, map)
            }
            val list = detailData.toList()
            return LoadResult.Page(
                data = list,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
        const val PAGING_SIZE = 30
    }
}