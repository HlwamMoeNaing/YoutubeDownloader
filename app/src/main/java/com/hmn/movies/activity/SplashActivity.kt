@file:Suppress("DEPRECATION")

package com.hmn.movies.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.hmn.movies.R
import com.hmn.movies.network.Api
import com.hmn.movies.network.model.Pojo
import com.hmn.movies.network.RetrofitHelper
import com.hmn.movies.room.BannerEntity
import com.hmn.movies.viewmodel.ViewModelImplement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    lateinit var mViewModel: ViewModelImplement
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mViewModel = ViewModelProviders.of(this).get(ViewModelImplement::class.java)
       // mViewModel = ViewModelProvider(this)[ViewModelImplement::class.java]


        val list = mViewModel.getAllBanner()


            networkCall()


        if (!list.isEmpty()){
            startActivity()
        }
    }
    override fun onRestart() {
        super.onRestart()
        if (isConnectingToInternet(this)){
           // networkCall()
            startActivity()
        }
    }
    override fun onResume() {
        super.onResume()
        if (isConnectingToInternet(this)){
            //networkCall()
            startActivity()
        }
    }


    private fun networkCall(){
        val list = mViewModel.getAllBanner()
        if (isConnectingToInternet(this)) {
            if (list.isEmpty())  {


                RetrofitHelper.getRetrofit<Api>().getModel().enqueue(object : Callback<Pojo> {
                    override fun onResponse(call: Call<Pojo>, response: Response<Pojo>) {
                        if (response.isSuccessful) {

                            val catList = ArrayList<BannerEntity>()

                            val body = response.body()!!

                            val ban = body.banners
                            val top = body.top_video
                            val pop = body.popular_video
                            val lat = body.latest_video

                            for (banner in ban) {
                                val model = BannerEntity(
                                    0,
                                    banner.id,
                                    banner.title,
                                    banner.desc,
                                    "Banner",
                                    false
                                )
                                catList.add(model)
                            }

                            for (banner in top) {
                                val model = BannerEntity(
                                    0,
                                    banner.id,
                                    banner.title,
                                    banner.desc,
                                    "Top Videos",
                                    false
                                )
                                catList.add(model)
                            }

                            for (banner in pop) {
                                val model = BannerEntity(
                                    0,
                                    banner.id,
                                    banner.title,
                                    banner.desc,
                                    "Popular Videos",
                                    false
                                )
                                catList.add(model)
                            }

                            for (banner in lat) {
                                val model = BannerEntity(
                                    0,
                                    banner.id,
                                    banner.title,
                                    banner.desc,
                                    "Latest Videos",
                                    false
                                )
                                catList.add(model)
                            }
                            mViewModel.repository.insertBannerAll(catList)
                        }

                    }

                    override fun onFailure(call: Call<Pojo>, t: Throwable) {

                    }

                })

            }
        }
        else{
            Toast.makeText(this,"Connection Error..",Toast.LENGTH_LONG).show()
        }
    }

    fun startActivity(){
        Handler().postDelayed(
            {
                val i= Intent(this, OnBoardActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()

            },
            2000
        )
    }

   private fun isConnectingToInternet(context: Context): Boolean {
        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info)
                    if (i.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
        }
        return false
    }

}