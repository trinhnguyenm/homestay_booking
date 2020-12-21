package com.ctr.homestaybooking.ui.booking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.extension.observeOnUiThread
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.extension.showDialog
import com.ctr.homestaybooking.extension.showErrorDialog
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.util.FORMAT_DATE_TIME_API_POST
import com.ctr.homestaybooking.util.format
import kotlinx.android.synthetic.main.fragment_add_review.*
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/12/21
 */
class AddReviewFragment : BaseFragment() {
    private lateinit var vm: BookingVMContract

    companion object {
        fun newInstance(bookingId: Int) = AddReviewFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_BOOKING_ID, bookingId)
            }
        }

        private const val KEY_BOOKING_ID = "key_booking_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = BookingViewModel(
            App.instance.localRepository,
            PlaceRepository(),
            UserRepository()
        )
        arguments?.getInt(KEY_BOOKING_ID)?.let {
            vm.getReviewBody().bookingId = it.apply { Log.d("--=", "+${this}") }
        }
        initListener()
    }

    override fun getProgressObservable() = vm.getProgressObservable()

    override fun isNeedPaddingTop() = true

    private fun initListener() {
        inputReview.validateData = {
            it.isNotBlank()
        }

        ivBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvReview.onClickDelayAction {
            vm.getReviewBody().apply {
                arguments?.getInt(KEY_BOOKING_ID)?.let {
                    bookingId = it.apply { Log.d("--=", "+${this}") }
                }
                comment = inputReview.getText()
                rating = ratingBar.rating.toInt()
                createDate = Calendar.getInstance().format(FORMAT_DATE_TIME_API_POST)
            }
            vm.getReviewBody().apply { Log.d("--=", "+${this}") }
            vm.addReview().observeOnUiThread().subscribe({
                activity?.showDialog(
                    getString(R.string.success_toast),
                    null,
                    getString(R.string.ok),
                    {
                        activity?.finishAffinity()
                    })
            }, { activity?.showErrorDialog(it) })
        }
    }
}
