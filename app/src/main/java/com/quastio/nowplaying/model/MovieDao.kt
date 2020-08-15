package com.quastio.nowplaying.model

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movieList: List<Movie>): List<Long>

    @Query("SELECT * FROM movies")
    fun getAllMoviesPaged(): PagingSource<Int, Movie>


    @Query("DELETE FROM movies")
    suspend fun clearAll(): Int
}