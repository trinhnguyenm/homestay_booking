package com.ctr.homestaybooking.ui.home.favotite

import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.source.response.Place
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by at-trinhnguyen2 on 2020/12/07
 */
interface FavoriteVMContract {

    fun insert(items: List<Favorite>): Completable
    fun getFavorites(): Flowable<List<Favorite>>
    fun deleteAll(items: List<Favorite>): Completable
    fun getPlaces(): MutableList<Place>
}
