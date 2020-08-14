package com.quastio.nowplaying.model

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.quastio.nowplaying.restclients.RestClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class MovieDataSource: PageKeyedDataSource<Int, Movie>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        CoroutineScope(IO).launch {
            try {
                val result=RestClient.movieApiService.getMovies(page = 1)
                result?.let {result->
                    callback.onResult(result.results,null,2)
                }

            }catch (throwable:Throwable){
                when(throwable){
                    is IOException->{
                        Log.e("data source","error  io ")
                    }
                    is HttpException ->{
                        Log.e("data source","error http ${throwable.code()}")

                    }
                    else->{
                        Log.e("data source","error")

                    }
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        CoroutineScope(IO).launch {
            try {
                val result=RestClient.movieApiService.getMovies(page = 1)
                result?.let {result->
                    callback.onResult(result.results,params.key+1)
                }

            }catch (throwable:Throwable){
                when(throwable){
                    is IOException->{
                        Log.e("data source","error  io ")
                    }
                    is HttpException ->{
                        Log.e("data source","error http ${throwable.code()}")

                    }
                    else->{
                        Log.e("data source","error")

                    }
                }
            }
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

}