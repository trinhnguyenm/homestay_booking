package com.ctr.homestaybooking.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.BookingType
import com.ctr.homestaybooking.data.model.CancelType
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.ui.wedget.CustomSpinner
import com.ctr.homestaybooking.ui.wedget.SpinnerType
import kotlinx.android.synthetic.main.fragment_filter_search.*

/**
 * Created by at-trinhnguyen2 on 2020/12/21
 */

class FilterFragment : BaseFragment() {
    private lateinit var vm: SearchVMContract

    companion object {
        fun getInstance() = FilterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? SearchActivity)?.let {
            vm = it.vm
        }
        vm.copy(vm.getSearchBody(), vm.getSearchBodyEdit())
        initView()
        initListener()
        showOldData()
    }

    override fun isNeedPaddingTop() = true

    override fun getProgressObservable() = vm.getProgressObservable()

    private fun initView() {
        mutableListOf<CustomSpinner.SpinnerItem>().apply {
            add(0, SpinnerType("Vui lòng chọn", ""))
            addAll(SpinnerType.BOOKING_TYPE)
            spinnerBookingType.initSpinner(this)
        }

        mutableListOf<CustomSpinner.SpinnerItem>().apply {
            add(0, SpinnerType("Vui lòng chọn", ""))
            addAll(SpinnerType.CANCER_TYPE)
            spinnerCancerType.initSpinner(this)
        }
    }

    private fun initListener() {
        ivBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvApply.onClickDelayAction {
            vm.copy(vm.getSearchBodyEdit(), vm.getSearchBody())
            activity?.onBackPressed()
        }

        tvResetAll.onClickDelayAction {
            inputMinPrice.setText("100000")
            inputMaxPrice.setText("5000000")
            spinnerBookingType.selectPosition("")
            spinnerCancerType.selectPosition("")
            pickerGuestNo.setValue(0)
            pickerRoomNo.setValue(0)
            pickerBedRoomNo.setValue(0)
            pickerBathRoomNo.setValue(0)
        }

        inputMinPrice.validateData = { true }

        inputMaxPrice.validateData = { true }

        spinnerBookingType.onItemSelectedListener = {
            vm.getSearchBodyEdit().bookingType =
                when (it?.getCode()) {
                    BookingType.INSTANT_BOOKING.name -> BookingType.INSTANT_BOOKING
                    BookingType.REQUEST_BOOKING.name -> BookingType.REQUEST_BOOKING
                    else -> null
                }
        }

        spinnerCancerType.onItemSelectedListener = {
            vm.getSearchBodyEdit().cancelType =
                when (it?.getCode()) {
                    CancelType.FLEXIBLE.name -> {
                        tvCancelPolicyInfo.text = getString(R.string.cancel_policy_flexible)
                        CancelType.FLEXIBLE
                    }
                    CancelType.MODERATE.name -> {
                        tvCancelPolicyInfo.text = getString(R.string.cancel_policy_moderate)
                        CancelType.MODERATE
                    }
                    CancelType.STRICT.name -> {
                        tvCancelPolicyInfo.text = getString(R.string.cancel_policy_strict)
                        CancelType.STRICT
                    }
                    else -> {
                        tvCancelPolicyInfo.text = ""
                        null
                    }
                }
        }

        inputMinPrice.afterTextChange = {
            vm.getSearchBodyEdit().minPrice = if (it.isBlank()) null else it.toDouble()
        }

        inputMaxPrice.afterTextChange = {
            vm.getSearchBodyEdit().maxPrice = if (it.isBlank()) null else it.toDouble()
        }

        pickerGuestNo.onValueChange = {
            vm.getSearchBodyEdit().guestCount = if (it == 0) null else it
        }
        pickerRoomNo.onValueChange = {
            vm.getSearchBodyEdit().roomCount = if (it == 0) null else it
        }
        pickerBedRoomNo.onValueChange = {
            vm.getSearchBodyEdit().bedCount = if (it == 0) null else it
        }
        pickerBathRoomNo.onValueChange = {
            vm.getSearchBodyEdit().bathroomCount = if (it == 0) null else it
        }
    }

    private fun showOldData() {
        vm.getSearchBody().apply {
            inputMinPrice.setText(if (minPrice == null) "" else minPrice.toString())
            inputMaxPrice.setText(if (maxPrice == null) "" else maxPrice.toString())
            if (cancelType == null) {
                spinnerCancerType.selectPosition("")
            } else {
                spinnerCancerType.selectPosition(cancelType!!.name)
            }
            if (bookingType == null) {
                spinnerBookingType.selectPosition("")
            } else {
                spinnerBookingType.selectPosition(bookingType!!.name)
            }
            pickerGuestNo.setValue(guestCount ?: 0)
            pickerRoomNo.setValue(roomCount ?: 0)
            pickerBedRoomNo.setValue(bedCount ?: 0)
            pickerBathRoomNo.setValue(bathroomCount ?: 0)
        }
    }
}
