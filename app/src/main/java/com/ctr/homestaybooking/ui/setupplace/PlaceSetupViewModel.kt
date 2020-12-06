package com.ctr.homestaybooking.ui.setupplace

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.source.LocalRepository
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.request.PlaceBody
import com.ctr.homestaybooking.data.source.response.PlaceDetail
import com.ctr.homestaybooking.data.source.response.ProvinceDetail
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/12/03
 */
class PlaceSetupViewModel(
    private val placeRepository: PlaceRepository,
    localRepository: LocalRepository
) : PlaceSetupVMContract, BaseViewModel() {

    private var placeBody = PlaceBody(userId = localRepository.getUserId())

    private var placeDetail: PlaceDetail? = null

    private var provinceDetail: ProvinceDetail? = null

    override fun getPlaceBody() = placeBody

    override fun getPlaceDetail() = placeDetail

    override fun getProvinceDetail() = provinceDetail

    override fun getPlaceDetail(id: Int) =
        placeRepository.getPlaceDetail(id).addProgressLoading().doOnSuccess {
            placeDetail = it.placeDetail
            placeBody = it.placeDetail.toPlaceBody()
        }

    override fun getPlaceTypes() =
        placeRepository.getPlaceTypes().addProgressLoading()

    override fun getProvinces() =
        placeRepository.getProvinces().addProgressLoading()

    override fun getProvinceById(id: Int) =
        placeRepository.getProvinceById(id).addProgressLoading().doOnSuccess {
            provinceDetail = it.provinceDetail
        }

    override fun editPlace() =
        placeRepository.editPlace(getPlaceBody()).addProgressLoading().doOnSuccess {
            placeDetail = it.placeDetail
            placeBody = it.placeDetail.toPlaceBody()
        }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogObservable
}
