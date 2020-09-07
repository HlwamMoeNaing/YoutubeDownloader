package com.hmn.movies.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import com.hmn.movies.R
import com.hmn.movies.network.model.IntroList

class OnboardingAdapter(val context: Context, val list: List<IntroList>) : PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }



    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen = layoutInflater.inflate(R.layout.intro_screen_layout, null)

        val slideImage = layoutScreen.findViewById<AppCompatImageView>(R.id.intro_img)
        val title = layoutScreen.findViewById<TextView>(R.id.intro_title)
        val description = layoutScreen.findViewById<TextView>(R.id.intro_description)


        title.text = list[position].title
        description.text = list[position].descption
        slideImage.setImageResource(list[position].introImage)

        container.addView(layoutScreen)
        return layoutScreen


    }
    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

}