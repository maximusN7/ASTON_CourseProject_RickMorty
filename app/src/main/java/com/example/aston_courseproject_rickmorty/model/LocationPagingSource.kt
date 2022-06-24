package com.example.aston_courseproject_rickmorty.model

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.aston_courseproject_rickmorty.retrofit.Common
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices

class LocationPagingSource : PagingSource<Int, Location>() {

    override fun getRefreshKey(state: PagingState<Int, Location>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Location> {
        val mService: RetrofitServices = Common.retrofitService
        return try {
            val page: Int = params.key ?: FIRST_PAGE
            val response = mService.getLocationPagingList(page)
            val prevPageNumber: Int? = if (page == 1) null else page - 1
            val nextPageNumber: Int? = if (response.info.next != null) {
                val uriNext = Uri.parse(response.info.next)
                val nextPageQuery = uriNext.getQueryParameter("page")
                nextPageQuery?.toInt()
            } else {
                null
            }

            LoadResult.Page(response.results, prevPageNumber, nextPageNumber)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    companion object {
        private const val FIRST_PAGE = 1
    }
}