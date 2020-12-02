package com.ctr.homestaybooking.ui.home.host.place

import com.ctr.homestaybooking.data.source.response.HostPlaceResponse
import com.ctr.homestaybooking.data.source.response.PlaceDetail
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface HostPlaceVMContract {
    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getPlaces(): MutableList<PlaceDetail>

    fun getPlacesFromServer(): Single<HostPlaceResponse>
}
