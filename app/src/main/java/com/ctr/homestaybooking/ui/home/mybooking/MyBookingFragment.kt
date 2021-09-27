package com.ctr.homestaybooking.ui.home.mybooking

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.bus.RxBus
import com.ctr.homestaybooking.data.model.BookingStatus
import com.ctr.homestaybooking.data.model.UpdateMyBooking
import com.ctr.homestaybooking.data.model.getText
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.response.Booking
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.booking.BookingActivity
import com.ctr.homestaybooking.ui.sheme.SchemeActivity
import kotlinx.android.synthetic.main.fragment_my_booking.*
import kotlinx.android.synthetic.main.layout_view_no_data.*

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class MyBookingFragment : BaseFragment() {
    private lateinit var vm: MyBookingVMContract
    private var filterDays = 365
    private var bookingStatus: BookingStatus? = null
    private var isVisibleToUser = false

    companion object {
        fun newInstance() =
            MyBookingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm =
            MyBookingViewModel(
                PlaceRepository(), App.instance.localRepository
            )
        getBookingHistory()
        initView()
        initListener()
        initRecyclerView()
        initSwipeRefresh()
        val linkType = activity?.intent?.getStringExtra(SchemeActivity.LINK_TYPE)
        val schemeSpecificPart =
            activity?.intent?.getStringExtra(SchemeActivity.SCHEME_SPECIFIC_PART)
        if (linkType == getString(R.string.paymentHost)) {
            schemeSpecificPart?.split(getString(R.string.paymentHost) + "/")?.let { list ->
                if (list.size >= 2 && list[1].toIntOrNull() != null) {
                    activity?.let { BookingActivity.start(it, list[1].toInt()) }
                }
            }
        }
    }

    private fun initListener() {
        cvFilterCheckIn.onClickDelayAction {
            context?.let { context ->
                val dialog = Dialog(context)
                dialog.apply {
                    setContentView(R.layout.layout_bottom_sheet_filter_day)
                    val tvAll = findViewById<TextView>(R.id.tvAll)
                    val tv7Days = findViewById<TextView>(R.id.tv7Days)
                    val tv30Days = findViewById<TextView>(R.id.tv30Days)
                    val tv90Days = findViewById<TextView>(R.id.tv90Days)
                    val tvPending = findViewById<TextView>(R.id.tvPending)
                    val tvUnpaid = findViewById<TextView>(R.id.tvUnpaid)
                    val tvPaid = findViewById<TextView>(R.id.tvPaid)
                    val tvComplete = findViewById<TextView>(R.id.tvComplete)
                    val tvCancer = findViewById<TextView>(R.id.tvCancer)
                    tvAll.onClickDelayAction {
                        filterDays = 365
                        bookingStatus = null
                        this@MyBookingFragment.tvFilterDay.text = "Tất cả"
                        this@MyBookingFragment.tvFilterStatus.text = ""
                        dismiss()
                    }

                    tv7Days.onClickDelayAction {
                        filterDays = 3
                        this@MyBookingFragment.tvFilterDay.text = "3 ngày trước"
                        dismiss()
                    }

                    tv30Days.onClickDelayAction {
                        filterDays = 30
                        this@MyBookingFragment.tvFilterDay.text = "1 tháng trước"
                        dismiss()
                    }
                    tv90Days.onClickDelayAction {
                        filterDays = 90
                        this@MyBookingFragment.tvFilterDay.text = "3 tháng trước"
                        dismiss()
                    }
                    tvPending.onClickDelayAction {
                        bookingStatus = BookingStatus.PENDING
                        this@MyBookingFragment.tvFilterStatus.text = BookingStatus.PENDING.getText()
                        dismiss()
                    }
                    tvUnpaid.onClickDelayAction {
                        bookingStatus = BookingStatus.UNPAID
                        this@MyBookingFragment.tvFilterStatus.text = BookingStatus.UNPAID.getText()
                        dismiss()
                    }
                    tvPaid.onClickDelayAction {
                        bookingStatus = BookingStatus.PAID
                        this@MyBookingFragment.tvFilterStatus.text = BookingStatus.PAID.getText()
                        dismiss()
                    }
                    tvComplete.onClickDelayAction {
                        bookingStatus = BookingStatus.COMPLETED
                        this@MyBookingFragment.tvFilterStatus.text =
                            BookingStatus.COMPLETED.getText()
                        dismiss()
                    }
                    tvCancer.onClickDelayAction {
                        bookingStatus = BookingStatus.CANCELLED
                        this@MyBookingFragment.tvFilterStatus.text =
                            BookingStatus.CANCELLED.getText()
                        dismiss()
                    }
                    setOnDismissListener {
                        Log.d("--=", "initListener: setOnDismissListener")
                        vm.filterMyBooking(filterDays, bookingStatus)
                        this@MyBookingFragment.rclBooking.adapter?.notifyDataSetChanged()
                    }
                    show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        RxBus.listen(UpdateMyBooking::class.java)
            .observeOnUiThread()
            .subscribe {
                if (it.isNeedUpdate) {
                    getBookingHistory()
                }
            }.addDisposable()
    }

    private fun initView() {
        ivNoData.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_empty_data))
        tvNoData.text = resources.getString(R.string.no_data_booking)
        tvActionMore.invisible()
    }

    override fun isNeedPaddingTop() = false

    override fun getUserVisibleHint(): Boolean {
        isVisibleToUser = true
        return super.getUserVisibleHint()
    }

    private fun initRecyclerView() {
        rclBooking.let {
            it.setHasFixedSize(true)
            it.adapter = MyBookingAdapter(
                vm.getBookings()
            ).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
            }
        }
    }

    private fun handlerItemClick(booking: Booking) {
        activity?.let { BookingActivity.start(it, booking.id) }
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getBookingHistory()
        }
    }

    internal fun getBookingHistory() {
        vm.getBookingHistory()
            .observeOnUiThread()
            .subscribe({
                if (it.length == 0) {
                    llNoData.visible()
                } else {
                    llNoData.invisible()
                }
                rclBooking.adapter?.notifyDataSetChanged()
            }, {
                handlerGetApiError(it)
            }).addDisposable()

    }


    private fun handlerGetApiError(throwable: Throwable) {
        activity?.showErrorDialog(throwable)
    }

    override fun getProgressObservable() = vm.getProgressObservable()
}
