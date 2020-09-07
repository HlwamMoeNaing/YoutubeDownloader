package com.hmn.movies.callback

import android.widget.ImageView
import com.hmn.movies.room.BannerEntity

interface RecyclerViewClickInterface {
    fun onItemClick(position:Int,bannerEntity: BannerEntity)
    fun onItemCategoryClick(position:Int,bannerEntity: BannerEntity)
}