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
class MovieRemoteMediator:RemoteMediator<Int,Movie>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
    return    try {

        val page=when(loadType){
            LoadType.REFRESH->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND ->{
                Log.e("remote mediator","perpend")
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                // If the previous key is null, then we can't request more data
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey


            }

            LoadType.APPEND->{
                Log.e("remote mediator","append")
//
//                val lastItem = state.pages.size
//                Log.e("remote mediator","last item  ==$lastItem")
                val remoteKeys = getRemoteKeyForLastItem(state)
                 if (remoteKeys == null || remoteKeys.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }
        Log.e("remote mediator","page  $page")

        val response = page?.let { RestClient.movieApiService.getMovies(page= it) }
        if (response != null&&response.results!=null&&response.results.isNotEmpty()) {
            Log.e("remote mediator","success  ${response.results}")
//            if (loadType == LoadType.REFRESH) {
//                MovieDb.invoke(MyApplication.context).remoteKeysDao().clearRemoteKeys()
//            }
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (page==response.totalPages) null else page + 1
            val keys=response.results.map {movie->
                RemoteKeys(movie.id,prevKey,nextKey)

            }
            MovieDb.invoke(MyApplication.context).remoteKeysDao().insertAll(keys)

            MovieDb.invoke(MyApplication.context).movieDao().insertMovies(response.results)
            MediatorResult.Success(endOfPaginationReached = false)
        }else {
            MediatorResult.Success(endOfPaginationReached = true)
        }

        }catch (e:Exception){
        e.printStackTrace()
        MediatorResult.Error(e)
        }
    }
     suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
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
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                MovieDb.invoke(MyApplication.context).remoteKeysDao().remoteKeysMovieId(movieId)
            }
        }
    }
}