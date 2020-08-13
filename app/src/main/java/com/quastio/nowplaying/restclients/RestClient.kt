package com.quastio.nowplaying.restclients

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit

object RestClient {


    private val defaultHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(defaultHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create()))
    }

    val movieApiService:MovieApiService by lazy {
        retrofitBuilder.build().create(MovieApiService::class.java)
    }


    interface MovieApiService{
        @GET(NOW_PLAYING)
       suspend fun getMovies(@Query("api_key") key:String="52a9e88fc9d879e48bce96bfd7633921",@Query("page")page:Int):Date

    }

    private const val BASE_URL="https://api.themoviedb.org/3/movie/"
    private const val NOW_PLAYING="now_playing"
    const val ORIGINAL_IMG_BASE_URL="https://image.tmdb.org/t/p/original/"

}