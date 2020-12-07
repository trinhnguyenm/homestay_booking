package com.ctr.homestaybooking.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.model.LocalResponse


/**
 * Created by at-trinhnguyen2 on 2020/10/05
 */

@Database(entities = [Favorite::class, LocalResponse::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
    abstract fun localResponseDao(): LocalResponseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(AppDatabase::class.java) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "homestay-booking.db"
            ).build()
    }
}
