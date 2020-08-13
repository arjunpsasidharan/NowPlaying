package com.quastio.nowplaying.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("results")
    @Expose
    var results: List<Movie>,
    @SerializedName("page")
    @Expose
    var page: Int,
    @SerializedName("total_results")
    @Expose
    var totalResults: Int,
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int

) {
}