package com.quastio.nowplaying.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(tableName = "movies")
data class Movie(
    @SerializedName("popularity")
    @Expose
    var popularity: Double,
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int,
    @SerializedName("video")
    @Expose
    var video: Boolean,
    @SerializedName("poster_path")
    @Expose
    var posterPath: String?="",
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("adult")
    @Expose
    var adult: Boolean,
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String?="",
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String,
    @SerializedName("original_title")
    @Expose
    var originalTitle: String,
    @SerializedName("title")
    @Expose
    var title: String,
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double,
    @SerializedName("overview")
    @Expose
    var overview: String,
    @SerializedName("release_date")
    @Expose
    var releaseDate: String
) {
    @PrimaryKey(autoGenerate = true)
  var  movieId:Int=0

}