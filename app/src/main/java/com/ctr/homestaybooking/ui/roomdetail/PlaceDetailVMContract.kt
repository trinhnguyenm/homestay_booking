package com.ctr.homestaybooking.ui.roomdetail

import com.ctr.homestaybooking.data.source.response.PlaceDetailResponse
import com.ctr.homestaybooking.data.source.response.Promo
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface PlaceDetailVMContract {

    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun getPlaceDetail(placeId: Int): Single<PlaceDetailResponse>

    fun getPromos(): MutableList<Promo>

    fun getAmenities(): MutableList<Int>
}
