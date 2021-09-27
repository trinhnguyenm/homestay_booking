package com.ctr.homestaybooking.ui.setupplace

import com.ctr.homestaybooking.data.source.request.PlaceBody
import com.ctr.homestaybooking.data.source.response.*
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/12/03
 */
interface PlaceSetupVMContract {

    fun getPlaceBody(): PlaceBody

    fun getPlaceDetail(): PlaceDetail?

    fun getPlaceDetail(id: Int): Single<PlaceDetailResponse>

    fun editPlace(): Single<PlaceDetailResponse>

    fun getProgressObservable(): BehaviorSubject<Boolean>
    fun getPlaceTypes(): Single<PlaceTypeResponse>
    fun getProvinces(): Single<ProvinceResponse>
    fun getProvinceById(id: Int): Single<ProvinceDetailResponse>
    fun getProvinceDetail(): ProvinceDetail?
    fun getExportCalendar(): Single<CalendarResponse>
    fun deletePlace(): Single<PlaceDetailResponse>
    fun reversePlaceStatusByID(): Single<PlaceDetailResponse>
}
