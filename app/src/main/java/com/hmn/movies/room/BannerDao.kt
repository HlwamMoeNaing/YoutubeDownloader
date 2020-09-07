package com.hmn.movies.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface BannerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBannerAll(entity: List<BannerEntity>)

    @Update
    fun updateBanner(entity: BannerEntity)

    @Delete
    fun deleteBanner(entity: BannerEntity)

    @Query("Delete From banner_table")
    fun deleteAllBanner()

    @Query("SELECT * From banner_table")
    fun getAllBanner(): List<BannerEntity>

    @Query("SELECT * From banner_table Where category LIKE :mCategory")
    fun getDataFromCategory(mCategory: String): LiveData<List<BannerEntity>>


    @Query("SELECT COUNT(*) FROM banner_table")
    fun getCount(): Int

    @Query("SELECT * FROM banner_table WHERE isFav =:isFav")
    fun getFv(isFav: Boolean):List<BannerEntity>

}