package com.hmn.movies.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmn.movies.activity.MainActivity
import com.hmn.movies.R
import com.hmn.movies.adapter.FavouriteAdp
import com.hmn.movies.room.MDatabase


class FavouriteFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.back_toolbar)
        toolbar.setNavigationIcon(R.drawable.back_arrow)
        toolbar.title = "Favourite Songs"
        toolbar.setNavigationOnClickListener {
            val activity = activity as MainActivity
            activity.onSupportNavigateUp()
        }


        val recyclerView = view.findViewById<RecyclerView>(R.id.favourite_recycler)
        val list =MDatabase.getDatabase(requireContext()).bannerDao().getFv(true)
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        recyclerView.adapter = FavouriteAdp(requireContext(),list)

    }

}