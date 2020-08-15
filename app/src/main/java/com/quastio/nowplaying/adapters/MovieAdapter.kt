package com.quastio.nowplaying.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import com.quastio.nowplaying.R
import com.quastio.nowplaying.model.Movie
import com.quastio.nowplaying.restclients.RestClient
import com.quastio.nowplaying.utils.MovieDiffUtil

class MovieAdapter(private val interaction: Interaction? = null) :
    PagingDataAdapter<Movie,RecyclerView.ViewHolder>(MovieDiffUtil.DIFF_CALLBACK) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_list_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                getItem(position)?.let { holder.bind(it) }
            }
        }
    }



    class MovieViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        private val imageView:ImageView=itemView.findViewById(R.id.image_view)
        private val titleTV:TextView=itemView.findViewById(R.id.title_tv)

        fun bind(item: Movie) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(layoutPosition, item)
            }
            val url=RestClient.ORIGINAL_IMG_BASE_URL+item.posterPath
            Glide.with(itemView.context)
                .load(url)
                .into(imageView)

            titleTV.text=item.title
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Movie)
    }
}

