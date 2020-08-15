package com.quastio.nowplaying.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.quastio.nowplaying.model.MovieDb
import com.quastio.nowplaying.model.MovieRemoteMediator
import com.quastio.nowplaying.utils.MyApplication

class MovieViewModel : ViewModel() {
    @ExperimentalPagingApi
    val movieData = Pager(PagingConfig(20), remoteMediator = MovieRemoteMediator()) {
        MovieDb.invoke(MyApplication.context).movieDao().getAllMoviesPaged()
    }.flow
}