package com.quastio.nowplaying.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities =[Movie::class,RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDb:RoomDatabase() {
    abstract fun movieDao():MovieDao
    abstract fun remoteKeysDao(): RemoteKeyDao



    companion object {

        @Volatile private var instance: MovieDb? = null
        private val LOCK = Any()


        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            MovieDb::class.java, "movie_list.db"
        ).build()
    }


}