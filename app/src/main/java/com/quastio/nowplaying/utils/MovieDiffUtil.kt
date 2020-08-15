package com.quastio.nowplaying.utils

import androidx.recyclerview.widget.DiffUtil
import com.quastio.nowplaying.model.Movie

object MovieDiffUtil {
    val DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> by lazy {
        object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}