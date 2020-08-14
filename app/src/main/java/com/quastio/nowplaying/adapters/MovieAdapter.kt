package com.quastio.nowplaying.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import com.bumptech.glide.Glide
import com.quastio.nowplaying.R
import com.quastio.nowplaying.model.Movie
import com.quastio.nowplaying.restclients.RestClient
import com.quastio.nowplaying.utils.MovieDiffUtil

class MovieAdapter(private val interaction: Interaction? = null) :
    PagedListAdapter<Movie,RecyclerView.ViewHolder>(MovieDiffUtil.DIFF_CALLBACK) {




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

        fun bind(item: Movie) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            val url=RestClient.ORIGINAL_IMG_BASE_URL+item.posterPath
            Glide.with(itemView.context)
                .load(url)
                .into(imageView)

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Movie)
    }
}
