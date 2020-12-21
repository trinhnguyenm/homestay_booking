package com.ctr.homestaybooking.ui.setupplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.CancelType
import com.ctr.homestaybooking.extension.observeOnUiThread
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.extension.showDialog
import com.ctr.homestaybooking.extension.showErrorDialog
import com.ctr.homestaybooking.ui.wedget.SpinnerType
import com.ctr.homestaybooking.util.FORMAT_DATE_TIME_API_POST
import com.ctr.homestaybooking.util.format
import com.ctr.homestaybooking.util.toCalendar
import kotlinx.android.synthetic.main.fragment_place_setup_price.*
import kotlinx.android.synthetic.main.layout_place_setup_price.*
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/12/03
 */

class PlaceSetupPriceFragment : BaseFragment() {
    private lateinit var vm: PlaceSetupVMContract

    companion object {
        fun newInstance() = PlaceSetupPriceFragment().apply {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_setup_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? PlaceSetupActivity)?.vm?.let {
            vm = it
        }
        initView()
        initListener()
        initData()
    }

    override fun getProgressObservable() =
        (activity as? PlaceSetupActivity)?.vm?.getProgressObservable()

    override fun isNeedPaddingTop() = true

    private fun initView() {

        inputPrize.validateData = {
            it.isNotBlank() && it.length <= 20 && it.length >= 5
        }

        spinnerCancerType.initSpinner(SpinnerType.CANCER_TYPE)

        spinnerCheckInEarly.initSpinner(SpinnerType.HOURS, "12")

        spinnerCheckInLately.initSpinner(SpinnerType.HOURS, "14")

        spinnerCheckOut.initSpinner(SpinnerType.HOURS, "12")
    }

    private fun initListener() {
        ivBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvSave.onClickDelayAction {
            Log.d("--=", "initListener: ${vm.getPlaceBody()}")
            vm.getPlaceBody().amenities = mutableListOf(1, 2, 3, 4, 5)
            vm.editPlace().observeOnUiThread().subscribe({
                activity?.showDialog(
                    getString(R.string.success_toast),
                    null,
                    getString(R.string.ok),
                    {
                        activity?.onBackPressed()
                    })
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
        }

        inputPrize.afterTextChange = {
            vm.getPlaceBody().pricePerDay = it.toDouble()
            handleNextButton()
        }

        spinnerCancerType.onItemSelectedListener = {
            vm.getPlaceBody().cancelType =
                when (it?.getCode()) {
                    CancelType.FLEXIBLE.name -> {
                        tvCancelPolicyInfo.text = getString(R.string.cancel_policy_flexible)
                        CancelType.FLEXIBLE
                    }
                    CancelType.MODERATE.name -> {
                        tvCancelPolicyInfo.text = getString(R.string.cancel_policy_moderate)
                        CancelType.MODERATE
                    }
                    else -> {
                        tvCancelPolicyInfo.text = getString(R.string.cancel_policy_strict)
                        CancelType.STRICT
                    }
                }

            handleNextButton()
        }
        spinnerCheckInEarly.onItemSelectedListener = {
            it?.getCode()?.toIntOrNull()?.let {
                vm.getPlaceBody().earliestCheckInTime =
                    Calendar.getInstance().apply { set(2020, 0, 1, it, 0, 0) }
                        .format(FORMAT_DATE_TIME_API_POST)
            }
            handleNextButton()
        }
        spinnerCheckInLately.onItemSelectedListener = {
            it?.getCode()?.toIntOrNull()?.let {
                vm.getPlaceBody().latestCheckInTime =
                    Calendar.getInstance().apply { set(2020, 0, 1, it, 0, 0) }
                        .format(FORMAT_DATE_TIME_API_POST)
            }
            handleNextButton()
        }
        spinnerCheckOut.onItemSelectedListener = {
            it?.getCode()?.toIntOrNull()?.let {
                vm.getPlaceBody().checkOutTime =
                    Calendar.getInstance().apply { set(2020, 0, 1, it, 0, 0) }
                        .format(FORMAT_DATE_TIME_API_POST)
            }
            handleNextButton()
        }
    }

    private fun initData() {
        showOldData()
    }

    private fun showOldData() {
        vm.getPlaceDetail()?.apply {
            pricePerDay?.let {
                inputPrize.setText(it.toString())
            }

            cancelType?.let {
                spinnerCancerType?.selectPosition(it.name)
            }
            earliestCheckInTime?.let { time ->
                spinnerCheckInEarly?.selectPosition(
                    (time.toCalendar().get(Calendar.HOUR_OF_DAY) - 8).also {
                        if (it < 1) {
                            (it + 24).toString()
                        } else {
                            it.toString()
                        }
                    }
                )
            }
            latestCheckInTime?.let { time ->
                spinnerCheckInLately?.selectPosition(
                    (time.toCalendar().get(Calendar.HOUR_OF_DAY) - 8).also {
                        if (it < 1) {
                            (it + 24).toString()
                        } else {
                            it.toString()
                        }
                    }
                )
            }
            checkOutTime?.let { time ->
                spinnerCheckOut?.selectPosition(
                    (time.toCalendar().get(Calendar.HOUR_OF_DAY) - 8).also {
                        if (it < 1) {
                            (it + 24).toString()
                        } else {
                            it.toString()
                        }
                    }
                )
            }
        }
    }

    private fun isAllValid() =
        inputPrize.isValidateDataNotEmpty() &&
                spinnerCancerType.isValid() &&
                spinnerCheckInEarly.isValid() &&
                spinnerCheckInLately.isValid() &&
                spinnerCheckOut.isValid()

    private fun handleNextButton() {
        if (isAllValid()) {
            tvSave.isEnabled = true
            tvSave.alpha = 1f
        } else {
            tvSave.isEnabled = false
            tvSave.alpha = 0.4f
        }
    }
}
