package com.hmn.movies.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.Window


import android.view.WindowManager.LayoutParams.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hmn.movies.R
import com.hmn.movies.adapter.OnboardingAdapter
import com.hmn.movies.network.model.IntroList


@Suppress("DEPRECATION")
class OnBoardActivity : AppCompatActivity() {
    lateinit var screenPager: ViewPager
    lateinit var pagerAdapter: OnboardingAdapter
    lateinit var indicator: TabLayout
    lateinit var nextButton: Button
    lateinit var bunGetStarted: Button
    lateinit var btnAnim: Animation
    lateinit var tvSkip: TextView
    private var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )



        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        }

        setContentView(R.layout.activity_on_board)


        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )

        if (!hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }


        nextButton = findViewById(R.id.btn_next)
        bunGetStarted = findViewById(R.id.btn_get_started)
        indicator = findViewById(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(this, R.anim.button_animation)
        tvSkip = findViewById(R.id.tv_skip)


        val list = ArrayList<IntroList>()
        list.add(IntroList("First Page", "This is first Description", R.drawable.ronaldo))
        list.add(IntroList("Second Page", "This is Second Description", R.drawable.eight))
        list.add(IntroList("Third Page", "This is Third Description", R.drawable.eur))

        screenPager = findViewById(R.id.screen_viewpager)
        pagerAdapter = OnboardingAdapter(this, list)
        screenPager.adapter = pagerAdapter
        indicator.setupWithViewPager(screenPager)

        nextButton.setOnClickListener {
            position = screenPager.currentItem
            if (position < list.size) {
                position++
                screenPager.currentItem = position
            }

            if (position == list.size - 1) {
                loadLatestScreen()
            }
        }

        indicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == list.size - 1) {
                    loadLatestScreen()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        bunGetStarted.setOnClickListener {
            val mainActivity = Intent(applicationContext, MainActivity::class.java)
            startActivity(mainActivity)
            savePrefsData()
            finish()
        }


        tvSkip.setOnClickListener {
            screenPager.currentItem = list.size
        }
    }

    private fun loadLatestScreen() {
        nextButton.visibility = View.INVISIBLE
        bunGetStarted.visibility = View.VISIBLE
        tvSkip.visibility = View.INVISIBLE
        indicator.visibility = View.INVISIBLE
        bunGetStarted.animation = btnAnim
    }


    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        return pref.getBoolean("isIntroOpnend", false)
    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.apply()
    }


    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}