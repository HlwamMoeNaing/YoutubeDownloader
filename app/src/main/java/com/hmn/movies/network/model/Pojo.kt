package com.hmn.movies.network.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.hmn.movies.room.BannerEntity
import kotlinx.android.parcel.Parcelize

data class Pojo(
    @field:SerializedName("banners")
    val banners: List<Video>,

    @field:SerializedName("latest_video")
    val latest_video: List<Video>,

    @field:SerializedName("popular_video")
    val popular_video: List<Video>,

    @field:SerializedName("top_video")
    val top_video: List<Video>
)


@Parcelize
data class Video(
    val desc: String,
    val id: String,
    val thumbnail: Thumbnail,
    val title: String
):Parcelable

@Parcelize
data class Thumbnail(
    @SerializedName("@height")
    @Expose
    val height: String,
    @SerializedName("@url")
    @Expose
    val url: String,
    @SerializedName("@width")
    @Expose
    val width: String
):Parcelable



data class Category(
    val typ:Int,
    val tit:String,
    val vlist: MutableList<BannerEntity>

)




