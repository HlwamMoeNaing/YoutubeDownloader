package com.hmn.movies.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hmn.movies.R
import com.hmn.movies.activity.MainActivity
import com.hmn.movies.activity.PlayerActivity
import com.hmn.movies.fragment.DownloadedVideoFrag
import com.hmn.movies.network.model.File


class DownloadedFileAdapter(val context: Context, val list: ArrayList<File>) :
    RecyclerView.Adapter<DownloadedFileAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoView: AppCompatImageView = view.findViewById(R.id.video_player)
        val title: TextView = view.findViewById(R.id.video_title)
        val duration: TextView = view.findViewById(R.id.tv_duration)
        val videoSize: TextView = view.findViewById(R.id.tv_video_size)
        val delete: AppCompatImageView = view.findViewById(R.id.delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.video_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].name
        val uri = list[position].uri
        holder.videoSize.text = list[position].size + " MB"
        Glide.with(context).asBitmap().load(uri).apply(RequestOptions().override(180, 100))
            .into(holder.videoView)
        holder.duration.text = list[position].time
        val mediaController = MediaController(context)
        mediaController.setAnchorView(holder.videoView)
        holder.delete.visibility = View.VISIBLE

        holder.delete.setOnClickListener {
            val fileToDelete = java.io.File(list[position].deletePath)


            if (fileToDelete.exists()) {
                if (fileToDelete.delete()) {
                    if (fileToDelete.exists()) {
                        fileToDelete.canonicalFile.delete()
                        if (fileToDelete.exists()) {
                            context.deleteFile(fileToDelete.name)
                        }
                    }

                }
            }

            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_LONG)
                .show()
            val ll = list.get(position)
            list.remove(ll)
            notifyDataSetChanged()


        }




        holder.itemView.setOnClickListener {
            val i = Intent(context, PlayerActivity::class.java)
            i.putExtra("video_uri", list[position].uri.toString())
            i.putExtra("video_title", list[position].name)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}