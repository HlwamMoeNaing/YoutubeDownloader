@file:Suppress("DEPRECATION")

package com.hmn.movies.repository

import android.annotation.SuppressLint
import android.app.Application
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.os.StrictMode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hmn.movies.network.model.File
import com.hmn.movies.room.BannerDao
import com.hmn.movies.room.BannerEntity
import com.hmn.movies.room.MDatabase

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RepoImplement(application: Application) : Repository {

    private var datab = MDatabase.getDatabase(application)


    private val bannerDao = datab.bannerDao()


    private val bannerVideo = bannerDao.getAllBanner()


//    override fun getBannerModel(): MutableLiveData<List<BannerEntity>> {
//
//        return MutableLiveData()
//    }

    @SuppressLint("SetWorldReadable")
    override fun getFiles(): MutableLiveData<List<File>> {
        val fil =  MutableLiveData<List<File>>()

        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
        val file =
            java.io.File(Environment.getExternalStorageDirectory().toString(), "Youtube Video")
        val files = file.listFiles()
        val list = ArrayList<File>()
        for (i in files.indices) {

            val sas = files[i].path
            val uri = Uri.fromFile(java.io.File(sas))
            val tit = files[i].name
            val length = files[i].length()
            val number = length.toInt() / 1000000.00
            val size = String.format("%.2f", number)
            val deletePath = files[i].path
            val path = files[i].path
            var timeFormat = ""
            if (length <0){
                val mme = MediaMetadataRetriever()
                mme.setDataSource(path)
                val durationStr = mme.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val millSecond = durationStr!!.toInt()
                val minutes = (millSecond % (1000 * 60 * 60)) / (1000 * 60)
                val seconds = (millSecond % (1000 * 60 * 60) % (1000 * 60) / 1000)
                timeFormat = "$minutes:$seconds"

            }

            list.add(File(uri, tit,size,deletePath,timeFormat))

            fil.value = list
        }

        return fil
    }




    fun getAllBanner(): List<BannerEntity> = bannerVideo
    fun insertBannerAll(bannerEntity: List<BannerEntity>) {
        InsertBannerAll(bannerDao).execute(bannerEntity)
    }
    fun deleteBanner(bannerEntity: BannerEntity) {
        DeleteBanner(bannerDao).execute(bannerEntity)
    }


    fun deleteAllBanner() {
        DeleteAllBanner(bannerDao).execute()
    }




    fun getDataFromCategory(category: String) : LiveData<List<BannerEntity>>{
        return bannerDao.getDataFromCategory(category)
    }













    class InsertBannerAll(dao: BannerDao) : AsyncTask<List<BannerEntity>, Void, Void>() {
        private val videoDao: BannerDao = dao

        override fun doInBackground(vararg p0: List<BannerEntity>?): Void?
        {
            videoDao.insertBannerAll(p0[0]!!)
            return null
        }
    }

    class DeleteBanner(dao: BannerDao) : AsyncTask<BannerEntity, Void, Void>() {
        private val videoDao: BannerDao = dao
        override fun doInBackground(vararg p0: BannerEntity?): Void? {
            videoDao.deleteBanner(p0[0]!!)
            return null
        }

    }

    private class DeleteAllBanner(dao: BannerDao) : AsyncTask<Void, Void, Void>() {
        private val videoDao: BannerDao = dao
        override fun doInBackground(vararg params: Void?): Void? {
            videoDao.deleteAllBanner()
            return null
        }
    }

















}



