package com.ctr.hotelreservations.ui.booking

import com.ctr.hotelreservations.base.BaseViewModel
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.LocalRepository
import com.ctr.hotelreservations.data.source.UserRepository
import com.ctr.hotelreservations.data.source.request.RoomsReservationBody
import com.ctr.hotelreservations.data.source.response.RoomReservationResponse
import com.ctr.hotelreservations.data.source.response.UserResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/21
 */

class BookingViewModel(
    private var localRepository: LocalRepository,
    private val hotelRepository: HotelRepository,
    private val userRepository: UserRepository
) : BookingVMContract, BaseViewModel() {

    private val roomReservations = mutableListOf<RoomReservationResponse.RoomReservation>()

    private val roomsReservationBody = RoomsReservationBody()

    override fun getRoomsReservationBody() = roomsReservationBody

    override fun getRoomReservations(): MutableList<RoomReservationResponse.RoomReservation> =
        roomReservations

    override fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>?
    ): Single<RoomReservationResponse> {
        return hotelRepository.addNewRoomsReservation(
            numberOfRooms,
            listPromoCode,
            getRoomsReservationBody()
        )
            .addProgressLoading()
            .doOnSuccess { response ->
                getRoomReservations().apply {
                    clear()
                    addAll(response.roomReservations)
                }
            }
    }

    override fun getUserInfo(): Single<UserResponse> {
        return userRepository.getUserFollowId(localRepository.getUserId())
            .addProgressLoading()
    }

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogStateObservable
}
