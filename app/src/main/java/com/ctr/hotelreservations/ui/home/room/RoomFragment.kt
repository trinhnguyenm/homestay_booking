package com.ctr.hotelreservations.ui.home.room

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.extension.*
import com.ctr.hotelreservations.ui.home.MainActivity
import com.ctr.hotelreservations.util.DateUtil
import com.ctr.hotelreservations.util.DateUtil.FORMAT_DATE_TIME_CHECK_IN
import com.ctr.hotelreservations.util.DateUtil.FORMAT_DATE_TIME_DAY_IN_WEEK
import com.ctr.hotelreservations.util.DateUtil.FORMAT_DATE_TIME_FROM_API
import com.ctr.hotelreservations.util.DateUtil.ONE_HOUR
import kotlinx.android.synthetic.main.fragment_room_of_brand.*
import kotlinx.android.synthetic.main.include_layout_select_date.*
import java.util.*
import kotlin.math.abs

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class RoomFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var viewModel: RoomVMContract
    private lateinit var brand: HotelResponse.Hotel.Brand
    private val startDate = Calendar.getInstance()
    private val endDate = Calendar.getInstance()
    private var startDateString = "2020-06-20T14:00:00.682Z"
    private var endDateString = "2020-06-20T14:00:00.682Z"

    companion object {
        private const val KEY_BRAND = "key_brand"

        fun getInstance(brand: HotelResponse.Hotel.Brand) =
            RoomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        KEY_BRAND,
                        brand
                    )
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room_of_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = RoomViewModel(
            HotelRepository()
        )
        brand = arguments?.getParcelable<HotelResponse.Hotel.Brand>(KEY_BRAND)
            ?: HotelResponse.Hotel.Brand()
        getAllRoomStatus(brand.id, startDateString, endDateString)
        initListener()
        initRecyclerView()
        initSwipeRefresh()
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        viewSelected.onClickDelayAction {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog.newInstance(
                this,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
            datePicker.apply {
                isAutoHighlight = true
                setEndTitle("Check Out")
                setStartTitle("Check In")
            }
            (activity as? MainActivity)?.showDatePickerDialog(datePicker)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setOnDateSetListener(this)
    }

    override fun isNeedPaddingTop() = true

    private fun initRecyclerView() {
        recyclerView.let {
            it.setHasFixedSize(true)
            it.adapter = RoomAdapter(viewModel.getRoomTypes(), brand).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
            }
        }

    }

    private fun handlerItemClick(room: RoomTypeResponse.RoomTypeStatus) {
        Log.d("--=", "handlerItemClick: ${room}")
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getAllRoomStatus(
                brand.id,
                startDateString,
                endDateString
            )
        }
    }

    private fun getRooms(brandId: Int) {
        addDisposables(
            viewModel.getAllRoomByBrand(brandId)
                .observeOnUiThread()
                .subscribe({
                    recyclerView.adapter?.notifyDataSetChanged()
                }, {
                    handlerGetApiError(it)
                })
        )
    }

    private fun getAllRoomStatus(brandId: Int, startDate: String, endDate: String) {
        addDisposables(
            viewModel.getAllRoomStatus(brandId, startDate, endDate)
                .observeOnUiThread()
                .subscribe({
                    recyclerView.adapter?.notifyDataSetChanged()
                }, {
                    handlerGetApiError(it)
                })
        )
    }


    private fun handlerGetApiError(throwable: Throwable) {
        activity?.showErrorDialog(throwable)
    }

    override fun getProgressBarControlObservable() = viewModel.getProgressObservable()

    override fun onDateSet(
        view: DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int,
        yearEnd: Int,
        monthOfYearEnd: Int,
        dayOfMonthEnd: Int
    ) {
        startDate.set(year, monthOfYear, dayOfMonth, 14, 0, 0)
        endDate.set(yearEnd, monthOfYearEnd, dayOfMonthEnd, 12, 0, 0)
        startDateString = DateUtil.format(startDate, FORMAT_DATE_TIME_FROM_API)
        endDateString = DateUtil.format(endDate, FORMAT_DATE_TIME_FROM_API)
        lnSelected.invisible()
        lnStartDate.visible()
        tvRangeDate.visible()
        lnEndDate.visible()
        tvStartDayOfTheWeek.text = DateUtil.format(startDate, FORMAT_DATE_TIME_DAY_IN_WEEK)
        tvEndDayOfTheWeek.text = DateUtil.format(endDate, FORMAT_DATE_TIME_DAY_IN_WEEK)
        tvStartDate.text = DateUtil.format(startDate, FORMAT_DATE_TIME_CHECK_IN)
        tvEndDate.text = DateUtil.format(endDate, FORMAT_DATE_TIME_CHECK_IN)
        val dayNumber =
            (abs(startDate.timeInMillis - endDate.timeInMillis) + 2 * ONE_HOUR) / (24 * ONE_HOUR)
        tvRangeDate.text = resources.getString(R.string.roomDayNumber, dayNumber)
        if (startDateString.isNotEmpty() && endDateString.isNotEmpty()) {
            getAllRoomStatus(
                brand.id,
                startDateString,
                endDateString
            )
        }
    }
}
