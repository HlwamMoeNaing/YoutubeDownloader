package com.hmn.movies.fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hmn.movies.R
import com.hmn.movies.activity.MainActivity
import com.hmn.movies.adapter.DownloadedFileAdapter
import com.hmn.movies.network.model.File
import com.hmn.movies.viewmodel.ViewModelImplement


class DownloadedVideoFrag : Fragment() {
    private lateinit var mViewModel: ViewModelImplement
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:DownloadedFileAdapter


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_downloaded_video2, container, false)
        //mViewModel = ViewModelProviders.of(this).get(ViewModelImplement::class.java)
        mViewModel = ViewModelProvider(this)[ViewModelImplement::class.java]
        val toolbar = view.findViewById<Toolbar>(R.id.back_toolbar)
        toolbar.setNavigationIcon(R.drawable.back_arrow)
        toolbar.title = "Downloaded Songs"
        toolbar.setNavigationOnClickListener {
            val activity = activity as MainActivity
            activity.onSupportNavigateUp()
        }



//       swiper = view.findViewById<SwipeRefreshLayout>(R.id.swipe_layout)
        recyclerView =view.findViewById(R.id.rc_test)


        val  fileList =mViewModel.repository.getFiles()

        fileList.observe(viewLifecycleOwner, {
            val fl = it as ArrayList
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter = DownloadedFileAdapter(requireContext(), fl)
            recyclerView.adapter = adapter


        })


        return view
    }

}