package com.ctr.homestaybooking.ui.home.host.place

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.datasource.LocalDataSource
import com.ctr.homestaybooking.data.source.response.HostPlaceResponse
import com.ctr.homestaybooking.data.source.response.PlaceDetail
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class HostPlaceViewModel(
    private val localRepository: LocalDataSource,
    private val placeRepository: PlaceRepository
) : HostPlaceVMContract, BaseViewModel() {

    private val places = mutableListOf<PlaceDetail>()

    override fun getPlaces(): MutableList<PlaceDetail> = places

    override fun getPlacesFromServer(): Single<HostPlaceResponse> {
        return placeRepository.getPlacesByHostId(localRepository.getUserId())
            .addProgressLoading()
            .doOnSuccess { response ->
                getPlaces().apply {
                    clear()
                    addAll(response.places)
                }
            }
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogObservable
}
