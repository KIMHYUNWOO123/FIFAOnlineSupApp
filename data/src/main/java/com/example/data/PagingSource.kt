package com.example.data

import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class PagingSource(
    private val apiService: ApiService,
    private val accessId: String,
    private val matchType: Int
) : PagingSource<Int, String>() {
    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        try {
            val nextPageNumber = params.key ?: STARTING_PAGE_INDEX
            val response = apiService.getMatchRecord1(accessId = accessId, matchType = matchType, offset = PAGING_SIZE, limit = nextPageNumber)
            val prevKey = if (nextPageNumber == STARTING_PAGE_INDEX) null else nextPageNumber - 1
            val nextKey = if (!endOfPaginationReached) {
                null
            } else {
                nextPageNumber + (params.loadSize / PAGING_SIZE)
            }
            return LoadResult.Page(
                data = response,
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
        const val PAGING_SIZE = 10
    }
}