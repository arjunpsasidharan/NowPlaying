package com.quastio.nowplaying.model

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.quastio.nowplaying.restclients.RestClient
import com.quastio.nowplaying.utils.MyApplication
import java.io.InvalidObjectException
import java.lang.Exception

@ExperimentalPagingApi
class MovieRemoteMediator : RemoteMediator<Int, Movie>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {// getting data closest to anchor position
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    if (remoteKeys == null) {
                        throw InvalidObjectException("Remote key and the prevKey should not be null")
                    }
                    val prevKey = remoteKeys.prevKey
                    if (prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKeys.prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    if (remoteKeys == null || remoteKeys.nextKey == null) {
                        throw InvalidObjectException("Remote key should not be null for $loadType")
                    }
                    remoteKeys.nextKey
                }
            }

            val response = page?.let { RestClient.movieApiService.getMovies(page = it) }// api call for getting data
            if (response != null && response.results != null && response.results.isNotEmpty()) {
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (page == response.totalPages) null else page + 1
                val keys = response.results.map { movie ->
                    RemoteKeys(movie.id, prevKey, nextKey)
                }
                MovieDb.invoke(MyApplication.context).remoteKeysDao().insertAll(keys) // inserting key for pagination

                MovieDb.invoke(MyApplication.context).movieDao().insertMovies(response.results) // saving movies to db
                MediatorResult.Success(endOfPaginationReached = false) // may contain next page
            } else {
                MediatorResult.Success(endOfPaginationReached = true)  // end page reached
            }

        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e) // sending back error occurance
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                // Get the remote keys of the last item retrieved
                MovieDb.invoke(MyApplication.context).remoteKeysDao().remoteKeysMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                // Get the remote keys of the first items retrieved
                MovieDb.invoke(MyApplication.context).remoteKeysDao().remoteKeysMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Movie>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                MovieDb.invoke(MyApplication.context).remoteKeysDao().remoteKeysMovieId(movieId)
            }
        }
    }
}