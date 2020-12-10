package com.ctr.homestaybooking.ui.home.places

import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.source.response.Place
import com.ctr.homestaybooking.data.source.response.PlaceResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface HomeVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getPlaces(): MutableList<Place>

    fun getPlacesFromServer(): Single<PlaceResponse>
    fun insert(items: List<Favorite>): Completable
    fun getFavorites(): Flowable<List<Favorite>>
    fun deleteAll(items: List<Favorite>): Completable
}
