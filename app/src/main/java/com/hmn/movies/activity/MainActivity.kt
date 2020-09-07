package com.hmn.movies.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem

import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.ViewModelProvider

import com.google.android.material.navigation.NavigationView
import com.hmn.movies.R
import com.hmn.movies.fragment.AboutFragment
import com.hmn.movies.fragment.DownloadedVideoFrag
import com.hmn.movies.fragment.FavouriteFragment
import com.hmn.movies.fragment.MainFragment
import com.hmn.movies.util.Constant
import com.hmn.movies.util.DarkTheme
import com.hmn.movies.viewmodel.ViewModelImplement
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var mViewModel: ViewModelImplement
    lateinit var container: FrameLayout
    lateinit var switch: SwitchCompat
    private  var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // mViewModel = ViewModelProviders.of(this).get(ViewModelImplement::class.java)
        mViewModel = ViewModelProvider(this)[ViewModelImplement::class.java]

        container = findViewById(R.id.fragment_container)




        val navigationView = findViewById<NavigationView>(R.id.nav_view_main)
        navigationView.setNavigationItemSelectedListener(this@MainActivity)

        val menu = navigationView.menu
        val menuItem = menu.findItem(R.id.nav_switch)
        val actionView = MenuItemCompat.getActionView(menuItem)

        switch = actionView.findViewById(R.id.switch_id)
        switch.setOnClickListener {
            if (switch.isChecked) {
                setDark()
                DarkTheme.apply(true)
                main_drawer_layout.closeDrawer(GravityCompat.START)


            } else {
                setDark()
                DarkTheme.apply(false)
                main_drawer_layout.closeDrawer(GravityCompat.START)

            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment())
            .commit()

//

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_menu -> {
                loadFragment(MainFragment())

            }
            R.id.favourite_menu -> {

                loadFragment(FavouriteFragment())
            }

            R.id.download -> {
                if (!hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constant.READ_PERMISSION
                    )
                }

                if (hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    loadFragment(DownloadedVideoFrag())
                } else {
                    Toast.makeText(
                        this,
                        "Storage Permission Required to music videos",
                        Toast.LENGTH_LONG
                    ).show()
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constant.READ_PERMISSION
                    )
                }


            }
            R.id.nav_switch -> {
                if (switch.isChecked) {
                    item.setIcon(R.drawable.moon)
                } else {
                    item.setIcon(R.drawable.ic_round_day)
                }

            }


            R.id.about -> {
                loadFragment(AboutFragment())
            }
        }
        main_drawer_layout.closeDrawer(GravityCompat.START)
        return false
    }


    private fun setDark() {
        DarkTheme.isEnabled(this)
    }

    fun loadFragment(fragment: Fragment) {
        val fmgr = supportFragmentManager.beginTransaction()
        fmgr.replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainFragment())
            .commit()
        return true
    }


    fun openCloseNavigationDrawer() {
        if (main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            main_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            main_drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {

        if (main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            main_drawer_layout.closeDrawer(GravityCompat.START)
        } else {

            val fragmentId = supportFragmentManager.findFragmentById(R.id.fragment_container)
            val homeFragment = MainFragment()

            if (fragmentId is MainFragment){

                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Exit")
                builder.setMessage("Are you want to Exit?")
                builder.setCancelable(false)
                builder.setPositiveButton("Yes") { _, _ ->
                    finish()
                }
                builder.setNegativeButton("No") { _, _ ->


                }
                val dialog = builder.create()
                dialog.show()
            }
            else{
                super.onBackPressed()

            }

        }

    }









    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    fun restartfragment(frag: DownloadedVideoFrag) {
        supportFragmentManager.beginTransaction().detach(frag).attach(frag).commit()

    }




}