package com.ctr.hotelreservations.ui.booking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.model.BookingStatus
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.UserRepository
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.RoomTypeResponse
import com.ctr.hotelreservations.extension.*
import com.ctr.hotelreservations.ui.App
import com.ctr.hotelreservations.ui.home.rooms.RoomFragment
import com.ctr.hotelreservations.ui.roomdetail.RoomDetailActivity
import com.ctr.hotelreservations.util.DateUtil
import com.ctr.hotelreservations.util.compareDay
import com.ctr.hotelreservations.util.parseToCalendar
import com.ctr.hotelreservations.util.parseToString
import kotlinx.android.synthetic.main.fragment_booking.*
import kotlinx.android.synthetic.main.layout_room_detail_booking.*
import java.util.*


/**
 * Created by at-trinhnguyen2 on 2020/06/16
 */
class BookingFragment : BaseFragment() {
    private lateinit var viewModel: BookingVMContract
    private var brand: HotelResponse.Hotel.Brand? = null
    private var roomTypeStatus: RoomTypeResponse.RoomTypeStatus? = null
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var numberOfRooms = 1
    private var prizePerNight = 0.0

    companion object {
        fun newInstance() = BookingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = BookingViewModel(
            App.instance.localRepository,
            HotelRepository(),
            UserRepository()
        )
        initView()
        initRecyclerView()
        initListener()
        getUserInfo()
    }

    override fun getProgressBarControlObservable() = viewModel.getProgressObservable()

    override fun isNeedPaddingTop() = true

    private fun initRecyclerView() {
        rcvStepBooking.apply {
            PagerSnapHelper().attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = BookingStepAdapter(
                listOf(
                    StepBookingUI(StepBooking.STEP_BOOKING, "Booking"),
                    StepBookingUI(StepBooking.STEP_BOOKING, "Payment"),
                    StepBookingUI(StepBooking.STEP_BOOKING, "Check in"),
                    StepBookingUI(StepBooking.STEP_BOOKING, "Review")
                )
            ).apply { setSelectedPosition(0) }
        }
    }

    private fun initView() {
        (activity as? BookingActivity)?.intent?.extras?.apply {
            brand = getParcelable(RoomFragment.KEY_BRAND) ?: HotelResponse.Hotel.Brand()
            roomTypeStatus = getParcelable(RoomDetailActivity.KEY_ROOM_TYPE_STATUS)
            startDate = getString(RoomDetailActivity.KEY_START_DATE)?.parseToCalendar()
            endDate = getString(RoomDetailActivity.KEY_END_DATE)?.parseToCalendar()
        }
        tvBookNow.isEnabled = false

        roomTypeStatus?.let {
            context?.let { context ->
                Glide.with(context).load(it.roomType.thumbnail).into(ivRoomThumb)
            }
            tvRoomTitle.text = it.roomType.name
            tvRoomInfo.text = it.roomType.getRoomTypeInfo()

            brand?.let { brand ->
                tvRoomAddress.text = brand.address
            }

            tvStartDate.text = startDate?.parseToString(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
            tvEndDate.text = endDate?.parseToString(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
            tvCheckinTime.text = startDate?.parseToString(DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK)
            tvCheckOutTime.text = endDate?.parseToString(DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK)
            tvRangeDate.text = "${startDate.compareDay(endDate)}D"
            pickerGuestNo.setMax(it.roomType.capacity)
            pickerRoomNo.setMax(it.totalRoomAvailable)
            prizePerNight =
                startDate.compareDay(endDate).times(it.roomType.price).times(numberOfRooms)
                    .times(1.1)
            tvTotalFee.text = prizePerNight.toString().getPriceFormat()
            tvPerNight.text = "${it.roomType.price.toString().getPriceFormat()} x 1 night"
            viewModel.getRoomsReservationBody().let { body ->
                body.brandId = brand?.id
                body.roomTypeId = roomTypeStatus?.roomType?.id
                body.startDate = startDate?.parseToString(DateUtil.FORMAT_DATE_TIME_FROM_API_3)
                body.endDate = endDate?.parseToString(DateUtil.FORMAT_DATE_TIME_FROM_API_3)
                body.status = BookingStatus.PENDING.toString()
            }
        }
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        inputFirstName.validateData = {
            it.trim().isNotEmpty()
        }

        inputLastName.validateData = {
            it.trim().isNotEmpty()
        }

        inputPhone.validateData = {
            it.trim().length == 10
        }

        inputEmail.validateData = {
            it.trim().isNotEmpty() && it.isEmailValid()
        }

        inputFirstName.afterTextChange = {
            viewModel.getRoomsReservationBody().firstName = it
            updateNextButtonState()
        }

        inputLastName.afterTextChange = {
            viewModel.getRoomsReservationBody().lastName = it
            updateNextButtonState()
        }

        inputPhone.afterTextChange = {
            //todo
            updateNextButtonState()
        }

        inputEmail.afterTextChange = {
            viewModel.getRoomsReservationBody().email = it
            updateNextButtonState()
        }

        pickerGuestNo.onValueChange = {
            //// TODO: 6/21/20
        }

        pickerRoomNo.onValueChange = {
            numberOfRooms = it
            tvTotalFee.text = numberOfRooms.times(prizePerNight).toString().getPriceFormat()
        }

        tvBookNow.onClickDelayAction {
            addNewRoomsReservation(numberOfRooms)
        }
    }

    private fun updateNextButtonState() {
        tvBookNow.isEnabled = validateData()
    }

    private fun validateData(): Boolean {
        return inputEmail.isValidateDataNotEmpty()
                && inputFirstName.isValidateDataNotEmpty()
                && inputLastName.isValidateDataNotEmpty()
                && inputPhone.isValidateDataNotEmpty()
                && numberOfRooms > 0
    }

    private fun addNewRoomsReservation(
        numberOfRooms: Int,
        listPromoCode: List<String>? = null
    ) {
        addDisposables(
            viewModel.addNewRoomsReservation(numberOfRooms, listPromoCode)
                .observeOnUiThread()
                .subscribe({
                    activity?.showDialog(
                        "Complete!",
                        "Booking id: ${it.roomReservations.firstOrNull()?.id}"
                    )
                    Log.d("--=", "addNewRoomsReservation: ${it}")
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }

    private fun getUserInfo() {
        addDisposables(
            viewModel.getUserInfo()
                .observeOnUiThread()
                .subscribe({
                    it.body.let { user ->
                        inputFirstName.setText(user.firstName)
                        inputLastName.setText(user.lastName)
                        inputEmail.setText(user.email)
                        inputPhone.setText(user.phone)
                    }
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }
}
