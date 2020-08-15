package com.quastio.nowplaying.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.quastio.nowplaying.R
import com.quastio.nowplaying.model.Movie
import com.quastio.nowplaying.restclients.RestClient
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var movie: Movie
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        if (intent!=null){
            movie=intent.getSerializableExtra("movie") as Movie
        }

      if (  this::movie.isInitialized){
          val stringUrl=RestClient.ORIGINAL_IMG_BASE_URL+movie.posterPath
          Glide.with(this)
              .load(stringUrl)
              .into(image_view)
          title_tv.text=movie.title
          description_tv.text=movie.overview
      }
    }
}