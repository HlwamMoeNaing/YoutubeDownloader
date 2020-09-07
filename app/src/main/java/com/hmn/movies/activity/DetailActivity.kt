package com.hmn.movies.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmn.movies.R
import com.hmn.movies.adapter.CategoryAdapter
import com.hmn.movies.callback.RecyclerViewClickInterface
import com.hmn.movies.adapter.DetailAdapter
import com.hmn.movies.room.BannerEntity
import com.hmn.movies.util.Constant
import com.hmn.movies.viewmodel.ViewModelImplement

class DetailActivity : AppCompatActivity(),RecyclerViewClickInterface {
    private lateinit var mViewModel: ViewModelImplement
    private lateinit var categoryTitle: String
    private lateinit var recycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        recycler = findViewById(R.id.rv_detail_activity)
        mViewModel = ViewModelProvider(this)[ViewModelImplement::class.java]
        categoryTitle = intent.getStringExtra("category_title")!!
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.back_toolbar)
        toolbar.title = categoryTitle
        setSupportActionBar(toolbar)

        toolbar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        initRecycler()
    }


    private fun initRecycler(){
        val entity =  mViewModel.repository.getAllBanner()

        val bannerList = entity.toMutableList()
        val topList = entity.toMutableList()
        val popularList = entity.toMutableList()
        val latestList = entity.toMutableList()

        bannerList.removeIf { s -> !s.category.contains("Banner") }

        topList.removeIf { s -> !s.category.contains("Top Videos") }

        popularList.removeIf { s -> !s.category.contains("Popular Videos") }

        latestList.removeIf { s -> !s.category.contains("Latest Videos") }




        if (categoryTitle == "Top Videos") {
            recycler.adapter = CategoryAdapter(this, topList,topList.size,this)
        }

        if (categoryTitle == "Popular Videos") {
            recycler.adapter = CategoryAdapter(this, popularList,popularList.size,this)
        }
        if (categoryTitle == "Latest Videos") {
            recycler.adapter = CategoryAdapter(this, latestList,latestList.size,this)
        }


        val numOfColumn = resources.getInteger(R.integer.gallery_columns)
        recycler.layoutManager = GridLayoutManager(this, numOfColumn)

    }

    override fun onItemClick(position: Int, bannerEntity: BannerEntity) {
        val category = bannerEntity.category
        val i = Intent(applicationContext, YoutubePlayerActivity::class.java)
        i.putExtra(Constant.CATEGORY_TITLE, category)
        i.putExtra(Constant.URL, bannerEntity.id)
        i.putExtra(Constant.TITLE, bannerEntity.title)
        startActivity(i)
    }

    override fun onItemCategoryClick(position: Int, bannerEntity: BannerEntity) {

    }

}