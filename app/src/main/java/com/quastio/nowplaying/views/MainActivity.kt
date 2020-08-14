package com.quastio.nowplaying.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.quastio.nowplaying.R
import com.quastio.nowplaying.adapters.MovieAdapter
import com.quastio.nowplaying.model.Movie
import com.quastio.nowplaying.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), MovieAdapter.Interaction {
   private lateinit var movieViewModel: MovieViewModel
   private lateinit var movieAdapter: MovieAdapter
   private lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieAdapter= MovieAdapter(this)
        layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recycler_view.layoutManager=layoutManager
        recycler_view.adapter=movieAdapter

        movieViewModel=ViewModelProvider(this).get(MovieViewModel::class.java)
        lifecycleScope.launch {
            movieViewModel._movie_data.collect{
                if (this@MainActivity::movieAdapter.isInitialized){
                        movieAdapter.submitData(it)

                }
            }

        }
    }

    override fun onItemSelected(position: Int, item: Movie) {
        Log.e("main",item.title)
    }
}