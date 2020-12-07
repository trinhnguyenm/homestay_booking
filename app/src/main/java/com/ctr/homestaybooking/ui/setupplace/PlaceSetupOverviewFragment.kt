package com.ctr.homestaybooking.ui.setupplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
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
        liCalendar.onClickDelayAction {
            (activity as? PlaceSetupActivity)?.openPlaceSetupCalendarFragment()
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
                    tvRequest.isEnabled = true
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
