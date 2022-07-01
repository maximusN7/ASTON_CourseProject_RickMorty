package com.example.aston_courseproject_rickmorty.model.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.database.EpisodeCharacterJoin
import com.example.aston_courseproject_rickmorty.model.database.EpisodeDb
import com.example.aston_courseproject_rickmorty.model.database.EpisodeRemoteKey
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto
import com.example.aston_courseproject_rickmorty.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.utils.Converters
import com.example.aston_courseproject_rickmorty.utils.Separators
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class EpisodeRemoteMediator(
    private val mServices: RetrofitServices,
    private val db: ItemsDatabase
) : RemoteMediator<Int, EpisodeForListDto>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeForListDto>
    ): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = mServices.getEpisodePagingList(page)
            val isEndOfList = response.info.next == null
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getEpisodeCharacterJoinDao().deleteAll()
                    db.getEpisodeDao().deleteAll()
                    db.getEpisodeKeysDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    EpisodeRemoteKey(
                        it.id.toString(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                db.getEpisodeKeysDao().insertAll(keys)
                db.getEpisodeDao().insertAll(EpisodeDb.episodeToDb(response.results))
                val listOfCharacterToEpisodes = Converters.convertToECJoin(response.results)
                db.getEpisodeCharacterJoinDao().insertAll(listOfCharacterToEpisodes)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, EpisodeForListDto>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, EpisodeForListDto>): EpisodeRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.getEpisodeKeysDao().remoteKeysEpisodeId(repoId.toString())
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, EpisodeForListDto>): EpisodeRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { episode -> db.getEpisodeKeysDao().remoteKeysEpisodeId(episode.id.toString()) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, EpisodeForListDto>): EpisodeRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { episode -> db.getEpisodeKeysDao().remoteKeysEpisodeId(episode.id.toString()) }
    }
}