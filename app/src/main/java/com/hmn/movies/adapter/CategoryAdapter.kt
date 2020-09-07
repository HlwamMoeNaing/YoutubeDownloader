package com.hmn.movies.adapter

import android.app.Activity
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.hmn.movies.R
import com.hmn.movies.activity.YoutubePlayerActivity
import com.hmn.movies.callback.RecyclerViewClickInterface
import com.hmn.movies.room.BannerEntity
import com.hmn.movies.room.MDatabase
import com.hmn.movies.util.Constant
import com.squareup.picasso.Picasso

class CategoryAdapter(
    val context: Context,
    val list: MutableList<BannerEntity>,
    val size:Int,
    val callbacks: RecyclerViewClickInterface
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val categoryImage: AppCompatImageView = view.findViewById(R.id.image)
        val favImage: AppCompatImageView = view.findViewById(R.id.img_add_fav)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.image_item, parent, false)
        )


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bannerEntity = list[position]
        val url = "https://img.youtube.com/vi/" + list[position].id + "/mqdefault.jpg"
        Picasso.get().load(url).into(holder.categoryImage)

        if (bannerEntity.isFav) {
            holder.favImage.setImageResource(R.drawable.favourite_full)
        } else {
            holder.favImage.setImageResource(R.drawable.favourite_boarder)
        }

        holder.itemView.setOnClickListener {

            callbacks.onItemClick(position,list[position])
        }


        holder.favImage.setOnClickListener {
            bannerEntity.isFav = !bannerEntity.isFav
            list[position] = bannerEntity
            MDatabase.getDatabase(context).bannerDao().updateBanner(bannerEntity)
            notifyItemChanged(position,bannerEntity)
        }

//        holder.favImage.setOnClickListener {
//            recyclerViewClickInterface.onItemClick(position, holder.favImage,list[position])
//        }




    }

    override fun getItemCount(): Int {
        return size
    }

}