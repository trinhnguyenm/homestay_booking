package com.ctr.homestaybooking.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ctr.homestaybooking.data.model.LocalResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


/**
 * Created by at-trinhnguyen2 on 2020/10/05
 */
@Dao
interface LocalResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(localResponses: List<LocalResponse>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(LocalResponse: LocalResponse): Completable

    @Query("SELECT * FROM local_responses")
    fun getLocalResponses(): Flowable<List<LocalResponse>>

    @Query("SELECT * FROM local_responses where url=:requestUrl")
    fun getLocalResponse(requestUrl: String): Single<LocalResponse>

    @Query("DELETE FROM local_responses")
    fun deleteAll(): Completable
}
