package com.hmn.movies.repository

import androidx.lifecycle.MutableLiveData
import com.hmn.movies.room.BannerEntity

interface Repository {

//    fun getBannerModel(): MutableLiveData<List<BannerEntity>>

    fun getFiles():MutableLiveData<List<com.hmn.movies.network.model.File>>




}