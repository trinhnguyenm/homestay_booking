package com.ctr.homestaybooking.ui.setupplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
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
    private lateinit var vm: PlaceSetupVMContract

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
        (activity as? PlaceSetupActivity)?.vm?.let {
            vm = it
        }

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

    private fun showPopup(v: View) {
        PopupMenu(v.context, v).apply {
            inflate(R.menu.menu_place_setup)
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.tvUnlisted -> {
                        activity?.showDialog(
                            getString(R.string.warning),
                            if (vm.getPlaceDetail()?.status == PlaceStatus.UNLISTED) {
                                "Chỗ ở sẽ hiển thị với người dùng, bạn có muốn hiện?"
                            } else {
                                "Chỗ ở sẽ không hiển thị với người dùng, bạn có muốn ẩn?"
                            },
                            getString(R.string.ok),
                            {
                                vm.reversePlaceStatusByID().observeOnUiThread().subscribe({
                                    activity?.showDialog(
                                        getString(R.string.success_toast),
                                        if (it.placeDetail.status == PlaceStatus.UNLISTED) {
                                            "Đã hiện chỗ ở"
                                        } else {
                                            "Đã ẩn chỗ ở"
                                        }
                                    )
                                }, {
                                    activity?.showErrorDialog(it)
                                })
                            },
                            getString(R.string.cancel)
                        )
                        true
                    }
                    R.id.tvDelete -> {
                        activity?.showDialog(
                            getString(R.string.warning),
                            "Chỗ ở bị xóa sẽ không thể khôi phục, bạn có muốn xóa?",
                            getString(R.string.ok),
                            {
                                vm.deletePlace().observeOnUiThread().subscribe({
                                    activity?.showDialog(
                                        getString(R.string.success_toast),
                                        "Đã xóa chỗ ở",
                                        "Quay lại",
                                        {
                                            activity?.finish()
                                        }
                                    )
                                }, {
                                    activity?.showErrorDialog(it)
                                })
                            },
                            getString(R.string.cancel)
                        )
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }

    private fun initListener() {
        ivBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        ivMore.onClickDelayAction {
            showPopup(ivMore)
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
        vm.getPlaceDetail(id).observeOnUiThread()
            .subscribe({
                container.visible()
                updateData()
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }

    internal fun updateData() {
        (activity as? PlaceSetupActivity)?.vm?.getPlaceDetail()?.let {
            if (it.submitStatus == SubmitStatus.ACCEPT && it.status == PlaceStatus.LISTED) {
                tvRequest.gone()
            } else {
                tvRequest.visible()
            }
            when {
                it.isSubmitCalendar() -> {
                    liBasic.setCompleted(true)
                    liTakePhotos.setCompleted(true)
                    liRate.setCompleted(true)
                    liCalendar.setCompleted(true)
                    tvRequest.isEnabled = true
                }
                it.isSubmitPrice() -> {
                    liBasic.setCompleted(true)
                    liTakePhotos.setCompleted(true)
                    liRate.setCompleted(true)
                    liCalendar.setVisibleUpdate(true)
                }
                it.isSubmitImages() -> {
                    liBasic.setCompleted(true)
                    liTakePhotos.setCompleted(true)
                    liRate.setVisibleUpdate(true)
                }
                it.isSubmitBasicInfo() -> {
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
