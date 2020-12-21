package com.ctr.homestaybooking.ui.home.host.calendar

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
import kotlinx.android.synthetic.main.fragment_home.swipeRefresh
import kotlinx.android.synthetic.main.fragment_my_booking.*
import kotlinx.android.synthetic.main.layout_view_no_data.*

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class HostBookingFragment : BaseFragment() {
    private lateinit var vm: HostBookingVMContract
    private var filterDays = 365
    private var bookingStatus: BookingStatus? = null

    companion object {
        fun newInstance() =
            HostBookingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_host_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm =
            HostBookingViewModel(
                PlaceRepository(), App.instance.localRepository
            )
        getBookingHistory()
        initView()
        initListener()
        initRecyclerView()
        initSwipeRefresh()
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
                        this@HostBookingFragment.tvFilterDay.text = "Tất cả"
                        this@HostBookingFragment.tvFilterStatus.text = ""
                        dismiss()
                    }

                    tv7Days.onClickDelayAction {
                        filterDays = 3
                        this@HostBookingFragment.tvFilterDay.text = "3 ngày trước"
                        dismiss()
                    }

                    tv30Days.onClickDelayAction {
                        filterDays = 30
                        this@HostBookingFragment.tvFilterDay.text = "1 tháng trước"
                        dismiss()
                    }
                    tv90Days.onClickDelayAction {
                        filterDays = 90
                        this@HostBookingFragment.tvFilterDay.text = "3 tháng trước"
                        dismiss()
                    }
                    tvPending.onClickDelayAction {
                        bookingStatus = BookingStatus.PENDING
                        this@HostBookingFragment.tvFilterStatus.text =
                            BookingStatus.PENDING.getText()
                        dismiss()
                    }
                    tvUnpaid.onClickDelayAction {
                        bookingStatus = BookingStatus.UNPAID
                        this@HostBookingFragment.tvFilterStatus.text =
                            BookingStatus.UNPAID.getText()
                        dismiss()
                    }
                    tvPaid.onClickDelayAction {
                        bookingStatus = BookingStatus.PAID
                        this@HostBookingFragment.tvFilterStatus.text = BookingStatus.PAID.getText()
                        dismiss()
                    }
                    tvComplete.onClickDelayAction {
                        bookingStatus = BookingStatus.COMPLETED
                        this@HostBookingFragment.tvFilterStatus.text =
                            BookingStatus.COMPLETED.getText()
                        dismiss()
                    }
                    tvCancer.onClickDelayAction {
                        bookingStatus = BookingStatus.CANCELLED
                        this@HostBookingFragment.tvFilterStatus.text =
                            BookingStatus.CANCELLED.getText()
                        dismiss()
                    }
                    setOnDismissListener {
                        Log.d("--=", "initListener: setOnDismissListener")
                        vm.filterMyBooking(filterDays, bookingStatus)
                        this@HostBookingFragment.rclBooking.adapter?.notifyDataSetChanged()
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
        tvNoData.text = "Bạn chưa có đơn đặt chỗ."
        tvActionMore.invisible()
    }

    override fun isNeedPaddingTop() = false

    private fun initRecyclerView() {
        rclBooking.let {
            it.setHasFixedSize(true)
            it.adapter = HostBookingAdapter(
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
