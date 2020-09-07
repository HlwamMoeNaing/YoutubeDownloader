package com.hmn.movies.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmn.movies.R
import com.hmn.movies.activity.MainActivity
import com.hmn.movies.activity.YoutubePlayerActivity
import com.hmn.movies.adapter.MultiRecyclerAdapter
import com.hmn.movies.callback.RecyclerViewClickInterface
import com.hmn.movies.network.model.Category
import com.hmn.movies.room.BannerEntity
import com.hmn.movies.room.MDatabase
import com.hmn.movies.util.Constant
import com.hmn.movies.viewmodel.ViewModelImplement
import java.util.*


@Suppress("CAST_NEVER_SUCCEEDS")
class MainFragment : Fragment(), RecyclerViewClickInterface {
    private lateinit var mViewModel: ViewModelImplement
    private lateinit var recycler: RecyclerView
    private lateinit var adapter:MultiRecyclerAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_home)
        toolbar.title = "Home"

        val menuImg = view.findViewById<ImageView>(R.id.menu_open)
        menuImg.setOnClickListener {
            (activity as MainActivity) .openCloseNavigationDrawer()
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = view.findViewById(R.id.fragment_main_recycler)
        start()

    }
    private fun start(){
        mViewModel = ViewModelProvider(this)[ViewModelImplement::class.java]

        val entity = mViewModel.repository.getAllBanner()
        val bannerList = entity.toMutableList()
        val topList = entity.toMutableList()
        val popularList = entity.toMutableList()
        val latestList = entity.toMutableList()

        bannerList.removeIf{s-> !s.category.contains("Banner")}
        topList.removeIf{s-> !s.category.contains("Top Videos")}
        popularList.removeIf{s-> !s.category.contains("Popular Videos")}
        latestList.removeIf{s-> !s.category.contains("Latest Videos")}


        val catList = ArrayList<Category>()
        catList.add(Category(Constant.BAN,"Banner Videos",bannerList))
        catList.add(Category(Constant.TOP,"Top Videos",topList))
        catList.add(Category(Constant.POP,"Popular Videos",popularList))
        catList.add(Category(Constant.LAT,"Latest Videos",latestList))




        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.setHasFixedSize(true)
        adapter= MultiRecyclerAdapter(requireContext(),catList,this)
        recycler.adapter = adapter

    }


//    override fun onItemClick(position: Int, view: ImageView, bannerEntity: BannerEntity) {
//        bannerEntity.isFav = !bannerEntity.isFav
//        MDatabase.getDatabase(requireContext()).bannerDao().updateBanner(bannerEntity)
//
//
//
//    }

    override fun onItemClick(position: Int, bannerEntity: BannerEntity) {
        val category = bannerEntity.category
        val i = Intent(requireActivity(), YoutubePlayerActivity::class.java)
        i.putExtra(Constant.CATEGORY_TITLE, category)
        i.putExtra(Constant.URL, bannerEntity.id)
        i.putExtra(Constant.TITLE, bannerEntity.title)
        startActivity(i)
    }

    override fun onItemCategoryClick(position: Int, bannerEntity: BannerEntity) {

    }


}
