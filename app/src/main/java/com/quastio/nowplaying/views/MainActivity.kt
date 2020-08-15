package com.quastio.nowplaying.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.quastio.nowplaying.R
import com.quastio.nowplaying.adapters.MovieAdapter
import com.quastio.nowplaying.model.Movie
import com.quastio.nowplaying.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MovieAdapter.Interaction {
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var layoutManager: GridLayoutManager

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieAdapter = MovieAdapter(this)
        layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)  // TODO: 15-08-2020   should set the span counnt as per device width
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = movieAdapter

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        lifecycleScope.launch {
            movieViewModel.movieData.collect {
                if (this@MainActivity::movieAdapter.isInitialized) {
                    movieAdapter.submitData(it)
                }
            }
        }
    }

    override fun onItemSelected(position: Int, item: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie", item)
        startActivity(intent)
    }
}