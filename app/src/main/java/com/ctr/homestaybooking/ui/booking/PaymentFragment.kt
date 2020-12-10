package com.ctr.homestaybooking.ui.booking

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.BookingStatus
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
    private lateinit var vm: BookingViewModel
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
    private var bookingSteps = mutableListOf(
        StepBookingUI("Đặt chỗ"),
        StepBookingUI("Thanh toán"),
        StepBookingUI("Nhận phòng"),
        StepBookingUI("Đánh giá")
    )
    private var isNeedReload = false

    companion object {
        internal const val KEY_MY_BOOKING = "key_my_booking"

        fun newInstance(bookingId: Int) = PaymentFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_MY_BOOKING, bookingId)
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
        vm = BookingViewModel(
            App.instance.localRepository, PlaceRepository(), UserRepository()
        )
        arguments?.getInt(KEY_MY_BOOKING)?.let {
            getBookingById(it)
        }
        container.gone()
        initRecyclerView()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        if (isNeedReload) {
            isNeedReload = false
            arguments?.getParcelable<Booking>(KEY_MY_BOOKING)?.let {
                getBookingById(it.id)
            }
        }
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

    private fun getBookingById(id: Int) {
        vm.getBookingById(id)
            .observeOnUiThread()
            .subscribe({
                container.visible()
                initView()
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }

    private fun initView() {
        vm.getBooking()?.let {
            bookingId = it.id
            status = it.status
            if (App.instance.localRepository.isUserSession()) {
                tvTitle.text = it.place.name
                when (it.status) {
                    BookingStatus.PENDING -> {
                        tvPayNow.text = "Thanh toán"
                        tvPayNow.isEnabled = false
                        tvBookAlertTitle.text = "Đang đợi chủ nhà chấp nhận yêu cầu."
                        tvBookAlert.text = "Bạn có thể thanh toán sau khi chủ nhà chấp nhận."
                    }
                    BookingStatus.ACCEPTED -> {
                        tvPayNow.text = "Thanh toán"
                        tvBookAlertTitle.text = "Chủ nhà đã chấp nhận yêu cầu đặt chỗ của bạn."
                        tvBookAlert.text = "Thanh toán ngay"
                    }
                    BookingStatus.UNPAID -> {
                        tvPayNow.text = "Thanh toán"
                        tvBookAlertTitle.text = "Đã tạo đơn đặt chỗ"
                        tvBookAlert.text = "Thanh toán ngay"
                    }
                    BookingStatus.PAID -> {
                        tvPayNow.text = "Hủy đặt chỗ"
                        tvBookAlertTitle.text = "Đang đợi bạn check-in"
                        tvBookAlert.text =
                            "Vui lòng check-in trước 14:00 ngày ${
                                it.startDate.toCalendar(FORMAT_DATE_API)
                                    .format(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
                            }"
                        adapterBookingStep.setSelectedPosition(2)
                        rcvStepBooking.scrollToPosition(2)
                        adapterBookingStep.notifyDataSetChanged()
                    }
                    BookingStatus.COMPLETED -> {
                        tvPayNow.text = "Đặt lại"
                        tvBookAlertTitle.text = "Đơn đặt chỗ đã hoàn thành"
                        tvBookAlert.text = "Bạn có hài lòng? Vui lòng đánh giá chỗ ở này"
                        adapterBookingStep.setSelectedPosition(3)
                        adapterBookingStep.notifyDataSetChanged()
                        rcvStepBooking.scrollToPosition(3)
                    }
                    else -> {
                        tvPayNow.text = "Đặt lại"
                        tvBookAlertTitle.text = "Đơn đặt chỗ của bạn đã bị hủy"
                        tvBookAlert.text =
                            "Xin lỗi, đơn đặt chỗ của bạn không thành công."
                        adapterBookingStep.setSelectedPosition(2)
                        bookingSteps.apply {
                            clear()
                            addAll(
                                listOf(
                                    StepBookingUI("Đặt chỗ"),
                                    StepBookingUI("Thanh toán"),
                                    StepBookingUI("Không thành công")
                                )
                            )
                        }
                        rcvStepBooking.adapter?.notifyDataSetChanged()
                        rcvStepBooking.scrollToPosition(2)
                    }
                }
            } else {
                rcvStepBooking.invisible()
                tvTitle.text = it.user.getName()
                when (it.status) {
                    BookingStatus.PENDING -> {
                        tvPayNow.text = "Chấp nhận yêu cầu"
                        tvPayNow.isEnabled = true
                        tvBookAlertTitle.text =
                            "Đang chờ bạn chấp nhận yêu cầu đặt chỗ của người dùng."
                        tvBookAlert.gone()
                    }
                    else -> {
                        tvPayNow.gone()
                        tvBookAlert.gone()
                        tvBookAlertTitle.gone()
                    }
                }
            }

            tvBookingId.text = "Mã đơn: ${it.id}"
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
        tvRangeDate.text = "${numberOfDays}Đ"

        updateTotalFee(numberOfDays, numberOfRooms, prize, promoPercent)
    }

    private fun updateTotalFee(
        numberOfDays: Int,
        numberOfRooms: Int,
        prize: Double,
        promoPercent: Int
    ) {
        if (promoPercent == 0) llPromo.gone() else llPromo.visible()
        tvTitlePerNoNight.text = "$numberOfDays đêm"
        val pricePerNoNight = prize * numberOfDays
        tvPlacePrice.text = pricePerNoNight.toMoney()
        tvPerNoNight.text = "${prize.toMoney()} x $numberOfDays"

        tvDiscount.text = "${pricePerNoNight.toMoney()} x $promoPercent%"
        val pricePromo = pricePerNoNight * promoPercent / 100.0
        tvPromo.text = "-${pricePromo.toMoney()}"

        val priceTax = (pricePerNoNight - pricePromo) * 15 / 100.0
        tvTaxPercent.text = "${(pricePerNoNight - pricePromo).toMoney()} x 15%"
        tvTax.text = priceTax.toMoney()
        val total = (pricePerNoNight - pricePromo + priceTax)
        tvTotalFee.text = total.toMoney()
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvPayNow.onClickDelayAction {
            if (App.instance.localRepository.isUserSession()) {
                when (status) {
                    BookingStatus.UNPAID, BookingStatus.ACCEPTED -> {
                        vm.let { vm ->
                            vm.requestPayment(bookingId).observeOnUiThread().subscribe({ response ->
                                response.body.let {
                                    if (it.errorCode != 0) {
                                        activity?.showDialog("Lỗi", it.message)
                                    } else {
                                        it.payUrl.apply { Log.d("--=", "+${this}") }
                                            ?.let { url ->
                                                isNeedReload = true
                                                activity?.openBrowser(url)
                                            }
                                    }
                                }
                            }, {
                                activity?.showErrorDialog(it)
                            })
                        }
                    }
                    BookingStatus.PAID -> {
                        activity?.showDialog(
                            "Cảnh báo!",
                            "Bạn có muốn hủy không",
                            "Có",
                            {
                                changeBookingStatus(BookingStatus.CANCELLED)
                            }, "Không"
                        )
                    }
                    else -> {

                    }
                }
            } else {
                when (status) {
                    BookingStatus.PENDING -> {
                        activity?.showDialog(
                            "Cảnh báo!",
                            "Bạn có muốn chấp nhận đặt chỗ này?",
                            "Có",
                            {
                                changeBookingStatus(BookingStatus.ACCEPTED)
                            }, "Không"
                        )
                    }
                    else -> {

                    }
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
        vm.changeBookingStatus(bookingId, bookingStatus)
            .observeOnUiThread()
            .subscribe({
                initView()
                /*when (it.booking.status) {
                    BookingStatus.PAID -> {
                        status = BookingStatus.PAID
                        tvPayNow.text = "Hủy đơn"
                        tvBookAlertTitle.text = "Đang đợi bạn check-in"
                        tvBookAlert.text =
                            "Vui lòng check-in trước 14:00 ${startDate?.format(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)}"
                        adapterBookingStep.setSelectedPosition(2)
                        rcvStepBooking.scrollToPosition(2)
                        rcvStepBooking.adapter?.notifyDataSetChanged()
                        RxBus.publish(UpdateMyBooking(true))
                    }
                    BookingStatus.ACCEPTED -> {
                        status = BookingStatus.ACCEPTED
                        tvBookAlertTitle.text = "Wait for user check in"
                        tvBookAlert.text =
                            "User will check in before 14:00 ${startDate?.format(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)}"
                        RxBus.publish(UpdateMyBooking(true))
                    }
                    BookingStatus.CANCELLED -> {
                        status = BookingStatus.CANCELLED
                        tvPayNow.text = "Đặt lại"
                        tvBookAlertTitle.text = "Đơn đặt chỗ của bạn đã bị hủy"
                        tvBookAlert.text =
                            "Sorry, the booking has been cancelled. Please book again or book another room."
                        adapterBookingStep.setSelectedPosition(2)
                        bookingSteps.apply {
                            clear()
                            addAll(
                                listOf(
                                    StepBookingUI("Đặt chỗ"),
                                    StepBookingUI("Thanh toán"),
                                    StepBookingUI("Đã hủy")
                                )
                            )
                        }
                        rcvStepBooking.adapter?.notifyDataSetChanged()
                        rcvStepBooking.scrollToPosition(2)
                        RxBus.publish(UpdateMyBooking(true))
                    }
                }*/
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }

    override fun getProgressObservable() =
        vm.progressBarDialogObservable
}


