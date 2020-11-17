package com.ctr.homestaybooking.ui.booking

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.bus.RxBus
import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.model.UpdateMyBooking
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.data.source.response.Booking
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.util.*
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.layout_payment.*
import java.util.*


/**
 * Created by at-trinhnguyen2 on 2020/06/27
 */
@SuppressLint("SetTextI18n")
class PaymentFragment : BaseFragment() {
    private lateinit var viewModel: BookingViewModel
    private lateinit var adapterBookingStep: BookingStepAdapter
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var promoPercent = 0
    private var numberOfRooms = 1
    private var numberOfDays = 1
    private var prize = 0.0
    private var totalFree = 0.0
    private var status: BookingStatus = BookingStatus.PENDING
    private var bookingId = 0
    private var roomReservationId = 0
    private var bookingSteps = mutableListOf(
        StepBookingUI("Booking"),
        StepBookingUI("Payment"),
        StepBookingUI("Check in"),
        StepBookingUI("Review")
    )

    companion object {
        internal const val KEY_MY_BOOKING = "key_my_booking"

        fun newInstance(booking: Booking) = PaymentFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_MY_BOOKING, booking)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = BookingViewModel(
            App.instance.localRepository, PlaceRepository(), UserRepository()
        )
        initRecyclerView()
        initView()
        initListener()
    }

    override fun isNeedPaddingTop() = true

    private fun initRecyclerView() {
        rcvStepBooking.apply {
            PagerSnapHelper().attachToRecyclerView(this)
            adapterBookingStep = BookingStepAdapter(
                bookingSteps
            ).apply { setSelectedPosition(1) }
            setHasFixedSize(true)
            adapter = adapterBookingStep
            scrollToPosition(1)
        }
    }

    private fun initView() {
        arguments?.getParcelable<Booking>(KEY_MY_BOOKING)?.let {
            bookingId = it.id
            when (it.status) {
                BookingStatus.PENDING -> {
                    tvPayNow.text = "Pay Now"
                    tvPayNow.isEnabled = false
                    tvBookAlertTitle.text = "Still Awaiting accept from host"
                    tvBookAlert.text = "Please complete you payment."
                }
                BookingStatus.ACCEPTED, BookingStatus.UNPAID -> {
                    tvPayNow.text = "Pay Now"
                    tvBookAlertTitle.text = "Still Awaiting Your Payment"
                    tvBookAlert.text = "Please complete you payment."
                }
                BookingStatus.PAID -> {
                    tvPayNow.text = "Cancel Booking"
                    tvBookAlertTitle.text = "Wait for you check in"
                    tvBookAlert.text =
                        "Please check in before 14:00 ${
                            it.startDate.toCalendar(FORMAT_DATE_API)
                                .format(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
                        }"
                    adapterBookingStep.setSelectedPosition(2)
                    rcvStepBooking.scrollToPosition(2)
                }
                BookingStatus.COMPLETED -> {
                    status = BookingStatus.COMPLETED
                    tvPayNow.text = "Book Again"
                    tvBookAlertTitle.text = "You booking has been completed"
                    tvBookAlert.text = "Please review this room."
                    adapterBookingStep.setSelectedPosition(3)
                    rcvStepBooking.scrollToPosition(3)
                }
                else -> {
                    status = BookingStatus.CANCELLED
                    tvPayNow.text = "Book Again"
                    tvBookAlertTitle.text = "You booking has been canceled"
                    tvBookAlert.text =
                        "Sorry, the booking has been cancelled. Please book again or book another room."
                    adapterBookingStep.setSelectedPosition(2)
                    bookingSteps.apply {
                        clear()
                        addAll(
                            listOf(
                                StepBookingUI("Booking"),
                                StepBookingUI("Payment"),
                                StepBookingUI("Cancelled")
                            )
                        )
                    }
                    rcvStepBooking.adapter?.notifyDataSetChanged()
                    rcvStepBooking.scrollToPosition(2)
                }
            }

            tvTitle.text = it.place.name
            tvBookingId.text = "Booking ID: ${it.id}"
            context?.let { context ->
                Glide.with(context).load(it.place.images?.firstOrNull()).into(ivPlaceThumb)
            }

            tvPlaceName.text = it.place.name
            tvPlaceRoom.text = it.place.getRooms()

            tvPlaceAddress.text = it.place.address
            startDate = it.startDate.toCalendar(FORMAT_DATE_API)
            endDate = it.endDate.toCalendar(FORMAT_DATE_API)
            numberOfDays = startDate.datesUntil(endDate).size
            prize = it.pricePerDay
            promoPercent = it.promo?.discountPercent ?: 0
            updateUI(startDate, endDate, numberOfDays, numberOfRooms, prize)
        }
    }

    private fun updateUI(
        startDate: Calendar?,
        endDate: Calendar?,
        numberOfDays: Int,
        numberOfRooms: Int,
        prize: Double
    ) {
        tvStartDate.text = startDate?.format(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
        tvEndDate.text = endDate?.format(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
        tvCheckinTime.text = startDate?.format(DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK)
        tvCheckOutTime.text = endDate?.format(DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK)
        tvRangeDate.text = "${numberOfDays}D"

        updateTotalFee(numberOfDays, numberOfRooms, prize, promoPercent)
    }

    private fun updateTotalFee(
        numberOfDays: Int,
        numberOfRooms: Int,
        prize: Double,
        promoPercent: Int
    ) {
        if (promoPercent == 0) llPromo.gone() else llPromo.visible()
        tvTitlePerNoNight.text = "Price per $numberOfDays night"
        val pricePerNoNight = prize * numberOfDays
        tvPerNoNight.text = "${prize.toString().getPriceFormat()} x $numberOfDays"

        tvTitlePerNoRoom.visibility = if (numberOfRooms == 1) View.GONE else View.VISIBLE
        tvPerNoRoom.visibility = if (numberOfRooms == 1) View.GONE else View.VISIBLE
        tvTitlePerNoRoom.text = "Price per $numberOfRooms room"
        val pricePerNoRoom = pricePerNoNight * 1
        tvPerNoRoom.text = "${pricePerNoNight.toString().getPriceFormat()} x $numberOfRooms"
        tvPlacePrice.text = pricePerNoRoom.toString().getPriceFormat()

        tvDiscount.text = "${pricePerNoRoom.toString().getPriceFormat()} x $promoPercent%"
        val pricePromo = pricePerNoRoom * promoPercent / 100.0
        tvPromo.text = "-${pricePromo.toString().getPriceFormat()}"

        val priceTax = (pricePerNoRoom - pricePromo) * 15 / 100.0
        tvTaxPercent.text = "${(pricePerNoRoom - pricePromo).toString().getPriceFormat()} x 15%"
        tvTax.text = priceTax.toString().getPriceFormat()

        totalFree = pricePerNoRoom - pricePromo + priceTax
        tvTotalFee.text = totalFree.toString().getPriceFormat()
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvPayNow.onClickDelayAction {
            when (status) {
                BookingStatus.UNPAID, BookingStatus.PENDING -> {
                    activity?.showDialog(
                        "Payment Method",
                        "Do you want pay ${totalFree.toString().getPriceFormat()} now?",
                        "Yes",
                        {
                            changeBookingStatus(BookingStatus.PAID)
                        }, "No"
                    )
                }
                BookingStatus.PAID -> {
                    activity?.showDialog(
                        "Warning!",
                        "Do you want cancel booking?",
                        "Yes",
                        {
                            changeBookingStatus(BookingStatus.CANCELLED)
                        }, "No"
                    )
                }
                else -> {
                }
            }

        }

        tvContactUs.onClickDelayAction {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:1800 6969")
            startActivity(intent)
        }
    }

    private fun changeBookingStatus(bookingStatus: BookingStatus) {
        addDisposables(
            viewModel.changeBookingStatus(bookingId, bookingStatus)
                .observeOnUiThread()
                .subscribe({
                    if (it.booking.status == BookingStatus.PAID) {
                        status = BookingStatus.PAID
                        tvPayNow.text = "Cancer Booking"
                        tvBookAlertTitle.text = "Wait for you check in"
                        tvBookAlert.text =
                            "Please check in before 14:00 ${startDate?.format(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)}"
                        adapterBookingStep.setSelectedPosition(2)
                        rcvStepBooking.scrollToPosition(2)
                        rcvStepBooking.adapter?.notifyDataSetChanged()
                        RxBus.publish(UpdateMyBooking(true))
                    } else if (it.booking.status == BookingStatus.CANCELLED) {
                        status = BookingStatus.CANCELLED
                        tvPayNow.text = "Book Again"
                        tvBookAlertTitle.text = "You booking has been canceled"
                        tvBookAlert.text =
                            "Sorry, the booking has been cancelled. Please book again or book another room."
                        adapterBookingStep.setSelectedPosition(2)
                        bookingSteps.apply {
                            clear()
                            addAll(
                                listOf(
                                    StepBookingUI("Booking"),
                                    StepBookingUI("Payment"),
                                    StepBookingUI("Cancelled")
                                )
                            )
                        }
                        rcvStepBooking.adapter?.notifyDataSetChanged()
                        rcvStepBooking.scrollToPosition(2)
                        RxBus.publish(UpdateMyBooking(true))
                    }
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }

    private fun changeRoomReservationStatus() {
        addDisposables(
            viewModel.changeRoomReservationStatus(roomReservationId)
                .observeOnUiThread()
                .subscribe({
                    if (it.body.status == BookingStatus.CANCELLED.name) {
                        status = BookingStatus.CANCELLED
                        tvPayNow.text = "Book Again"
                        tvBookAlertTitle.text = "You booking has been canceled"
                        tvBookAlert.text =
                            "Sorry, the booking has been cancelled. Please book again or book another room."
                        adapterBookingStep.setSelectedPosition(2)
                        bookingSteps.apply {
                            clear()
                            addAll(
                                listOf(
                                    StepBookingUI("Booking"),
                                    StepBookingUI("Payment"),
                                    StepBookingUI("Cancelled")
                                )
                            )
                        }
                        rcvStepBooking.adapter?.notifyDataSetChanged()
                        rcvStepBooking.scrollToPosition(2)
                        RxBus.publish(UpdateMyBooking(true))
                    }
                }, {
                    activity?.showErrorDialog(it)
                })
        )
    }

    override fun getProgressBarControlObservable() =
        viewModel.progressBarDialogStateObservable
}


