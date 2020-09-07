package com.hmn.movies.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banner_table")
data class BannerEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var _id: Int,

    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    var title: String= "",

    @ColumnInfo(name = "desc")
    var desc: String = "",

    @ColumnInfo(name = "category")
    var category: String = "",

    @ColumnInfo(name = "isFav")
    var isFav: Boolean = false,




)