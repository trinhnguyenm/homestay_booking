package com.ctr.homestaybooking.data.source.local.db

import androidx.room.*
import com.ctr.homestaybooking.data.model.Favorite
import io.reactivex.Completable
import io.reactivex.Flowable


/**
 * Created by at-trinhnguyen2 on 2020/10/05
 */
@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<Favorite>): Completable

    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flowable<List<Favorite>>

    @Delete
    fun deleteAll(notices: List<Favorite>): Completable
}
