package com.ctr.homestaybooking.data.source.datasource

import com.ctr.homestaybooking.data.model.Favorite
import io.reactivex.Completable
import io.reactivex.Flowable

interface FavoriteLocalDataSource {
    fun insert(items: List<Favorite>): Completable

    fun getFavorites(): Flowable<List<Favorite>>

    fun deleteAll(items: List<Favorite>): Completable
}
