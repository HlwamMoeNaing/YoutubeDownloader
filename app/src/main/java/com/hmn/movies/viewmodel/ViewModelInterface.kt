package com.hmn.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hmn.movies.network.model.File
import com.hmn.movies.room.BannerEntity


interface ViewModelInterface {





    fun getAllDataBasedOnCategory(category: String): LiveData<List<BannerEntity>>

    fun getAllBanner(): List<BannerEntity>

//    fun getBannerFromRetrofit(): LiveData<List<BannerEntity>>

    fun insertBannerVideoAll(entity: List<BannerEntity>)

    fun deleteBannerVideo(entity: BannerEntity)

    fun deleteAllBannerVideo()

    fun getFile():MutableLiveData<List<File>>


}