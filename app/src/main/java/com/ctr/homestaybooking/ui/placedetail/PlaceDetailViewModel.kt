package com.ctr.homestaybooking.ui.placedetail

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.response.PlaceDetail
import com.ctr.homestaybooking.data.source.response.PlaceDetailResponse
import com.ctr.homestaybooking.data.source.response.Promo
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/24
 */
class PlaceDetailViewModel(
    private val placeRepository: PlaceRepository
) : PlaceDetailVMContract, BaseViewModel() {
    private val promos = mutableListOf<Promo>()

    private val amenities = mutableListOf<Int>()

    private var placeDetail: PlaceDetail? = null

    override fun getPromos() = promos

    override fun getAmenities() = amenities

    override fun getPlaceDetail() = placeDetail

    override fun getPlaceDetail(placeId: Int): Single<PlaceDetailResponse> {
        return placeRepository.getPlaceDetail(placeId)
            .addProgressLoading()
            .doOnSuccess {
                placeDetail = it.placeDetail
                if (it.length > 0) {
                    it.placeDetail.promos?.let {
                        getPromos().apply {
                            clear()
                            addAll(it)
                        }
                    }
                    it.placeDetail.amenities?.let {
                        getAmenities().apply {
                            clear()
                            addAll(it)
                        }
                    }
                }
            }
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogObservable
}
