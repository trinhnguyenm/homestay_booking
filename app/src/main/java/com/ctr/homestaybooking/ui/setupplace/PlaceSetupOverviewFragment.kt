package com.ctr.homestaybooking.ui.setupplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.PlaceStatus
import com.ctr.homestaybooking.data.model.SubmitStatus
import com.ctr.homestaybooking.extension.*
import kotlinx.android.synthetic.main.fragment_place_setup_overview.*

/**
 * Created by at-trinhnguyen2 on 2020/12/03
 */
class PlaceSetupOverviewFragment : BaseFragment() {

    companion object {
        fun newInstance() = PlaceSetupOverviewFragment().apply {
        }

        internal const val KEY_PLACE_ID = "key_place_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_setup_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.intent?.getIntExtra(KEY_PLACE_ID, 0)?.let {
            if (it == 0) {
                liBasic.setVisibleUpdate(true)
            } else {
                container.gone()
                getPlaceDetail(it)
            }
        }
        initView()
        initListener()
    }

    override fun getProgressObservable() =
        (activity as? PlaceSetupActivity)?.vm?.getProgressObservable()

    override fun isNeedPaddingTop() = true

    override fun onResume() {
        super.onResume()
        updateData()
    }

    private fun initView() {

    }

    private fun initListener() {
        ivBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        liBasic.onClickDelayAction {
            (activity as? PlaceSetupActivity)?.openPlaceSetupBasicFragment()
        }
        liTakePhotos.onClickDelayAction {
            (activity as? PlaceSetupActivity)?.openPlaceSetupImageFragment()
        }
        liRate.onClickDelayAction {
            (activity as? PlaceSetupActivity)?.openPlaceSetupPrizeFragment()
        }
        liCalendar.onClickDelayAction {
            (activity as? PlaceSetupActivity)?.openPlaceSetupCalendarFragment()
        }
        tvRequest.onClickDelayAction {
            (activity as? PlaceSetupActivity)?.vm?.let { vm ->
                vm.getPlaceBody().apply {
                    submitStatus = SubmitStatus.ACCEPT
                    status = PlaceStatus.LISTED
                    apply { Log.d("--=", "+${this}") }
                }
                vm.editPlace().observeOnUiThread().subscribe({
                    updateData()
                    activity?.showDialog(
                        getString(R.string.success_toast),
                        null,
                        getString(R.string.ok),
                        {
                            activity?.onBackPressed()
                        })
                }, {
                    activity?.showErrorDialog(it)
                })
            }
        }
    }

    private fun getPlaceDetail(id: Int) {
        (activity as? PlaceSetupActivity)?.vm
            ?.getPlaceDetail(id)?.observeOnUiThread()
            ?.subscribe({
                container.visible()
                updateData()
            }, {
                activity?.showErrorDialog(it)
            })?.addDisposable()
    }

    private fun updateData() {
        (activity as? PlaceSetupActivity)?.vm?.getPlaceDetail()?.let {
            if (it.submitStatus == SubmitStatus.ACCEPT && it.status == PlaceStatus.LISTED) {
                tvRequest.gone()
            } else {
                tvRequest.visible()
            }
            when {
                it.isSubmitCalendar() -> {
                    Log.d("--=", "isSubmitCalendar:")
                    liBasic.setCompleted(true)
                    liTakePhotos.setCompleted(true)
                    liRate.setCompleted(true)
                    liCalendar.setCompleted(true)
                    tvRequest.isEnabled = true
                }
                it.isSubmitPrice() -> {
                    Log.d("--=", "isSubmitPrice")
                    liBasic.setCompleted(true)
                    liTakePhotos.setCompleted(true)
                    liRate.setCompleted(true)
                    liCalendar.setVisibleUpdate(true)
                }
                it.isSubmitImages() -> {
                    Log.d("--=", "isSubmitImages")
                    liBasic.setCompleted(true)
                    liTakePhotos.setCompleted(true)
                    liRate.setVisibleUpdate(true)
                }
                it.isSubmitBasicInfo() -> {
                    Log.d("--=", "isSubmitBasicInfo")
                    liBasic.setCompleted(true)
                    liTakePhotos.setVisibleUpdate(true)
                }
                else -> {
                    liBasic.setVisibleUpdate(true)
                }
            }
        }
    }
}
