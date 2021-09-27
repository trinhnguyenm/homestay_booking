package com.ctr.homestaybooking.ui.booking

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
import com.ctr.homestaybooking.data.model.BookingType
import com.ctr.homestaybooking.data.model.UpdateMyBooking
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.data.source.response.PlaceDetailResponse
import com.ctr.homestaybooking.data.source.response.Promo
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.placedetail.PlaceDetailActivity
import com.ctr.homestaybooking.util.*
import kotlinx.android.synthetic.main.fragment_booking.*
import kotlinx.android.synthetic.main.layout_place_detail_booking.*
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/06/16
 */
class BookingFragment : BaseFragment() {
    private lateinit var viewModel: BookingVMContract
    private var placeDetailResponse: PlaceDetailResponse? = null
    private var promo: Promo? = null
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var numberOfDays = 1
    private var price = 0.0

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
            PlaceRepository(),
            UserRepository()
        )
        initView()
        initRecyclerView()
        initListener()
    }

    override fun getProgressObservable() = viewModel.getProgressObservable()

    override fun isNeedPaddingTop() = true

    private fun initRecyclerView() {
        rcvStepBooking.apply {
            PagerSnapHelper().attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = BookingStepAdapter(
                listOf(
                    StepBookingUI("Đặt chỗ"),
                    StepBookingUI("Thanh toán"),
                    StepBookingUI("Nhận phòng"),
                    StepBookingUI("Đánh giá")
                )
            ).apply { setSelectedPosition(0) }
        }
    }

    private fun initView() {
        (activity as? BookingActivity)?.intent?.extras?.apply {
            placeDetailResponse = getParcelable(PlaceDetailActivity.KEY_PLACE_DETAIL)
            startDate = getString(PlaceDetailActivity.KEY_START_DATE)?.toCalendar()
            endDate = getString(PlaceDetailActivity.KEY_END_DATE)?.toCalendar()
            placeDetailResponse?.placeDetail?.let { placeDetail ->
                startDate?.let { startDate ->
                    promo =
                        placeDetail.promos
                            ?.filter {
                                startDate.after(it.startDate.toCalendar())
                                        && startDate.before(it.endDate.toCalendar())
                            }
                            ?.sortedBy { it.discountPercent }?.lastOrNull()
                    viewModel.getBookingBody().startDate = startDate.format(FORMAT_DATE_API)
                }
                endDate?.let { viewModel.getBookingBody().endDate = it.format(FORMAT_DATE_API) }
                (if (placeDetail.bookingType == BookingType.INSTANT_BOOKING) "Đặt ngay" else "Gửi yêu cầu").let {
                    tvBookNow.text = it
                    tvTitle.text = it
                }
                viewModel.getBookingBody().apply {
                    id = 0
                    status =
                        if (placeDetail.bookingType == BookingType.INSTANT_BOOKING) BookingStatus.UNPAID else BookingStatus.PENDING
                    userId = viewModel.getUserId()
                    placeId = placeDetail.id
                }
            }
        }

        placeDetailResponse?.placeDetail?.let {
            context?.let { context ->
                Glide.with(context).load(it.images?.firstOrNull()).into(ivPlaceThumb)
            }
            tvPlaceName.text = it.name
            tvPlaceRoom.text = it.getRooms()
            tvPlaceAddress.text = it.address
            pickerAdultNo.setMax(it.guestCount ?: 1)
            pickerChildrenNo.setMax(it.guestCount ?: 1)

            numberOfDays = startDate?.datesUntil(endDate)?.size ?: 1
            price = it.pricePerDay ?: 0.0

            updateUI(startDate, endDate, numberOfDays, price)
        }
    }

    private fun updateUI(
        startDate: Calendar?,
        endDate: Calendar?,
        numberOfDays: Int,
        price: Double
    ) {
        tvStartDate.text = startDate?.format(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
        tvEndDate.text = endDate?.format(DateUtil.FORMAT_DATE_TIME_CHECK_IN_BOOKING)
        tvCheckinTime.text = startDate?.format(DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK)
        tvCheckOutTime.text = endDate?.format(DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK)
        tvRangeDate.text = "${numberOfDays}Đ"
        updateTotalFee(numberOfDays, price, promo?.discountPercent ?: 0)
    }

    private fun updateTotalFee(
        numberOfDays: Int,
        price: Double,
        promoPercent: Int
    ) {
        if (promoPercent == 0) llPromo.gone() else llPromo.visible()
        tvTitlePerNoNight.text = "$numberOfDays đêm"
        val pricePerNoNight = price * numberOfDays
        tvPlacePrice.text = pricePerNoNight.toMoney()
        tvPerNoNight.text = "${price.toMoney()} x $numberOfDays"

        tvDiscount.text = "${pricePerNoNight.toMoney()} x $promoPercent%"
        val pricePromo = pricePerNoNight * promoPercent / 100.0
        tvPromo.text = "-${pricePromo.toMoney()}"

        val priceTax = (pricePerNoNight - pricePromo) * 15 / 100.0
        tvTaxPercent.text = "${(pricePerNoNight - pricePromo).toMoney()} x 15%"
        tvTax.text = priceTax.toMoney()
        val total = (pricePerNoNight - pricePromo + priceTax)
        tvTotalFee.text = total.toMoney()

        viewModel.getBookingBody().apply {
            promoId = promo?.id
            pricePerDay = price
            priceForStay = pricePerNoNight
            taxPaid = priceTax
            totalPaid = total
        }
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        pickerAdultNo.onValueChange = {

        }

        pickerChildrenNo.onValueChange = {

        }

        constrainDate.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvBookNow.onClickDelayAction {
            addBooking()
        }
    }

    private fun addBooking() {
        viewModel.addBooking()
            .observeOnUiThread()
            .subscribe({ response ->
                RxBus.publish(UpdateMyBooking(true))
                (activity as? BookingActivity)?.openPaymentFragment(response.booking.id)
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }
}
