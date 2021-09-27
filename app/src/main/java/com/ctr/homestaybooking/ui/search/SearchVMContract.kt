package com.ctr.homestaybooking.ui.search

import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.model.SearchBody
import com.ctr.homestaybooking.data.source.response.Place
import com.ctr.homestaybooking.data.source.response.PlaceResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/12/21
 */

interface SearchVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getPlaces(): MutableList<Place>

    fun insert(items: List<Favorite>): Completable
    fun getFavorites(): Flowable<List<Favorite>>
    fun deleteAll(items: List<Favorite>): Completable
    fun getSearchBody(): SearchBody
    fun searchPlaces(): Single<PlaceResponse>
    fun getSearchBodyEdit(): SearchBody
    fun copy(from: SearchBody, to: SearchBody)
}
