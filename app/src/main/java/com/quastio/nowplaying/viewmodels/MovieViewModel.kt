package com.quastio.nowplaying.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.quastio.nowplaying.model.MovieDataSource

class MovieViewModel:ViewModel() {
     val _movie_data=Pager(PagingConfig(20),pagingSourceFactory = {MovieDataSource()}).flow
}