package com.example.aston_courseproject_rickmorty.model

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.aston_courseproject_rickmorty.model.dto.CharacterDto
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices

class CharacterPagingSource(private val mService: RetrofitServices) : PagingSource<Int, CharacterForListDto>() {

    override fun getRefreshKey(state: PagingState<Int, CharacterForListDto>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterForListDto> {
        return try {
            val page: Int = params.key ?: 1 // ?: First page
            val response = mService.getCharacterPagingList(page)
            val returnResult = CharacterForListDto.characterToForListDto(response.results, true)
            val prevPageNumber: Int? = if (page == 1) null else page - 1
            val nextPageNumber: Int? = if (response.info.next != null) {
                val uriNext = Uri.parse(response.info.next)
                val nextPageQuery = uriNext.getQueryParameter("page")
                nextPageQuery?.toInt()
            } else {
                null
            }

            LoadResult.Page(returnResult, prevPageNumber, nextPageNumber)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}