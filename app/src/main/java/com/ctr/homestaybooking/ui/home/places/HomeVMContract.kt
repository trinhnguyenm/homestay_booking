package com.ctr.homestaybooking.ui.home.places

import com.ctr.homestaybooking.data.source.response.Place
import com.ctr.homestaybooking.data.source.response.PlaceResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface HomeVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getPlaces(): MutableList<Place>

    fun getPlacesFromServer(): Single<PlaceResponse>
}
