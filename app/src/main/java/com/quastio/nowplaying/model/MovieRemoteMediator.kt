package com.quastio.nowplaying.model

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.quastio.nowplaying.restclients.RestClient
import com.quastio.nowplaying.utils.MyApplication
import java.lang.Exception

@ExperimentalPagingApi
class MovieRemoteMediator:RemoteMediator<Int,Movie>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
    return    try {
        val page=when(loadType){
            LoadType.REFRESH->1
            LoadType.PREPEND ->{
                Log.e("remote mediator","perpend")
//                val response = RestClient.movieApiService.getMovies(page= 1)
//                if (response != null) {
//                    MovieDb.invoke(MyApplication.context).movieDao().insertMovies(response.results)
//                }
                return MediatorResult.Success(endOfPaginationReached = true)

            }

            LoadType.APPEND->{
                Log.e("remote mediator","append")

                val lastItem = state.pages.size
                Log.e("remote mediator","last item  ==$lastItem")


                // You must explicitly check if the last item is null when
                // appending, since passing null to networkService is only
                // valid for initial load. If lastItem is null it means no
                // items were loaded after the initial REFRESH and there are
                // no more items to load.
                if (lastItem == null) {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                lastItem+1
            }
        }
        Log.e("remote mediator","page  $page")

        val response = page?.let { RestClient.movieApiService.getMovies(page= it) }
        if (response != null&&response.results!=null&&response.results.isNotEmpty()) {
            Log.e("remote mediator","success  ${response.results}")

            MovieDb.invoke(MyApplication.context).movieDao().insertMovies(response.results)
            MediatorResult.Success(
                endOfPaginationReached = false
            )
        }else {
            MediatorResult.Success(
                endOfPaginationReached = true
            )
        }

        }catch (e:Exception){
        e.printStackTrace()
        MediatorResult.Error(e)
        }
    }


}