package com.hmn.movies.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hmn.movies.network.model.File
import com.hmn.movies.repository.RepoImplement
import com.hmn.movies.room.BannerEntity

class ViewModelImplement(application: Application) : AndroidViewModel(application),
    ViewModelInterface {

    val repository = RepoImplement(application)


    private lateinit var bannerVideoList: MutableLiveData<List<BannerEntity>>






    override fun getAllBanner(): List<BannerEntity> {
       return repository.getAllBanner()
    }


//    override fun getBannerFromRetrofit(): LiveData<List<BannerEntity>> {
//        bannerVideoList = repository.getBannerModel()
//        return bannerVideoList
//    }



    override fun insertBannerVideoAll(entity: List<BannerEntity>) {
        repository.insertBannerAll(entity)
    }



    override fun deleteBannerVideo(entity: BannerEntity) {
        repository.deleteBanner(entity)
    }


    override fun getAllDataBasedOnCategory(category: String): LiveData<List<BannerEntity>> {
        return repository.getDataFromCategory(category)
    }

    override fun deleteAllBannerVideo() {
        repository.deleteAllBanner()
    }

    override fun getFile(): MutableLiveData<List<File>> {
        return repository.getFiles()
    }


}