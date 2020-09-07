package com.hmn.movies.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.hmn.movies.R
import com.hmn.movies.activity.YoutubePlayerActivity
import com.hmn.movies.room.BannerEntity
import com.hmn.movies.util.Constant
import com.squareup.picasso.Picasso

class DetailAdapter(val context: Context,val list:List<BannerEntity>):RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val image: AppCompatImageView = view.findViewById(R.id.detail_image_item)
        val favImage: AppCompatImageView = view.findViewById(R.id.img_add_fav)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.detail_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bannerEntity = list[position]

        val url = "https://img.youtube.com/vi/" + list[position].id + "/mqdefault.jpg"
        Picasso.get().load(url).into(holder.image)

        if (bannerEntity.isFav) {
            holder.favImage.setImageResource(R.drawable.favourite_full)
        } else {
            holder.favImage.setImageResource(R.drawable.favourite_boarder)
        }

        holder.itemView.setOnClickListener {
            val category = list[position].category
            val i = Intent(context, YoutubePlayerActivity::class.java)
            i.putExtra(Constant.CATEGORY_TITLE, category)
            i.putExtra(Constant.URL, list[position].id)
            i.putExtra(Constant.TITLE, list[position].title)
            context.startActivity(i)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}