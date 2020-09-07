package com.hmn.movies.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hmn.movies.util.Constant

@Database(
    entities = [BannerEntity::class] ,version = 1, exportSchema = false
)
abstract class MDatabase : RoomDatabase() {

    abstract fun bannerDao(): BannerDao


    companion object {
        private var INSTANCR: MDatabase? = null

        fun getDatabase(context: Context): MDatabase {
            if (INSTANCR == null) {
                INSTANCR = Room.databaseBuilder(context, MDatabase::class.java, Constant.DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCR!!
        }
    }
}