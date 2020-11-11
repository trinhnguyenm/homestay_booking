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
import com.ctr.homestaybooking.data.model.UpdateMyBooking
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.response.MyBookingResponse
import com.ctr.homestaybooking.extension.*
import kotlinx.android.synthetic.main.fragment_home.swipeRefresh
import kotlinx.android.synthetic.main.fragment_my_booking.*
import kotlinx.android.synthetic.main.layout_view_no_data.*

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class MyBookingFragment : BaseFragment() {
    private lateinit var viewModel: MyBookingVMContract
    private var filterDays = 365

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
        viewModel =
            MyBookingViewModel(
                PlaceRepository()
            )
//        getMyBookings()
//        initView()
//        initListener()
//        initRecyclerView()
//        initSwipeRefresh()
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
                    tvAll.onClickDelayAction {
                        filterDays = 365
                        this@MyBookingFragment.txtFilterCheckIn.text = "All"
                        dismiss()
                    }

                    tv7Days.onClickDelayAction {
                        filterDays = 3
                        this@MyBookingFragment.txtFilterCheckIn.text = "3 days ago"
                        dismiss()
                    }

                    tv30Days.onClickDelayAction {
                        filterDays = 30
                        this@MyBookingFragment.txtFilterCheckIn.text = "30 days ago"
                        dismiss()
                    }
                    setOnDismissListener {
                        Log.d("--=", "initListener: setOnDismissListener")

                        viewModel.filterMyBooking(filterDays)
                        this@MyBookingFragment.rclBooking.adapter?.notifyDataSetChanged()
                    }
                    show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        addDisposables(
            RxBus.listen(UpdateMyBooking::class.java)
                .observeOnUiThread()
                .subscribe {
                    Log.d("--=", "onResume: ${it.isNeedUpdate}")
                    if (it.isNeedUpdate) {
                        getMyBookings()
                    }
            })
    }

    private fun initView() {
        ivNoData.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_empty_data))
        tvNoData.text = resources.getString(R.string.no_data_booking)
        tvActionMore.invisible()
    }

    override fun isNeedPaddingTop() = true

    private fun initRecyclerView() {
        rclBooking.let {
            it.setHasFixedSize(true)
            it.adapter = MyBookingAdapter(
                viewModel.getBookings()
            ).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
            }
        }
    }

    private fun handlerItemClick(booking: MyBookingResponse.MyBooking) {
        (parentFragment as? MyBookingContainerFragment)?.openPaymentFragment(booking)
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getMyBookings()
        }
    }

    internal fun getMyBookings() {
        Log.d("--=", "getMyBookings: ")
        addDisposables(
            viewModel.getBookingHistory()
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
                })
        )
    }


    private fun handlerGetApiError(throwable: Throwable) {
        activity?.showErrorDialog(throwable)
    }

    override fun getProgressBarControlObservable() = viewModel.getProgressObservable()
}
