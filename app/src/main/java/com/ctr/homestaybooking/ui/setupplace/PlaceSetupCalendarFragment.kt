package com.ctr.homestaybooking.ui.setupplace

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.DateStatus
import com.ctr.homestaybooking.data.source.response.BookingSlot
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.util.FORMAT_DATE_API
import com.ctr.homestaybooking.util.format
import com.ctr.homestaybooking.util.isContain
import com.ctr.homestaybooking.util.toDate
import com.squareup.timessquare.CalendarCellDecorator
import com.squareup.timessquare.CalendarPickerView
import com.squareup.timessquare.DefaultDayViewAdapter
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_place_setup_basic_info.*
import kotlinx.android.synthetic.main.fragment_place_setup_basic_info.tvSave
import java.util.*


/**
 * Created by at-trinhnguyen2 on 2020/12/07
 */

class PlaceSetupCalendarFragment : BaseFragment() {
    private lateinit var vm: PlaceSetupVMContract
    private var unavailableDates = listOf<Date>()
    private var bookedDates = listOf<Date>()

    companion object {
        fun newInstance() = PlaceSetupCalendarFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_setup_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? PlaceSetupActivity)?.vm?.let {
            vm = it
        }
        initView()
        initListener()
    }

    override fun getProgressObservable() =
        (activity as? PlaceSetupActivity)?.vm?.getProgressObservable()

    override fun isNeedPaddingTop() = true

    private fun initView() {
        val current = Calendar.getInstance()
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)
        vm.getPlaceBody().bookingSlots
            ?.filter { it.status == DateStatus.UNAVAILABLE }
            ?.map { it.date.toDate() }
            ?.filter { it.after(current.time) }
            ?.let { unavailableDates = it }
        vm.getPlaceBody().bookingSlots
            ?.filter { it.status == DateStatus.BOOKED }
            ?.map { it.date.toDate() }
            ?.filter { it.after(current.time) }
            ?.let { bookedDates = it }
        unavailableDates.apply { Log.d("--=", "unavailableDates+${this}") }
        bookedDates.apply { Log.d("--=", "bookedDates+${this}") }

        calendarPicker.apply {
            setCustomDayView(DefaultDayViewAdapter())
            decorators = emptyList<CalendarCellDecorator>()
            init(current.time, nextYear.time).inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                .withSelectedDates(unavailableDates)
            setOnInvalidDateSelectedListener {
                Log.d("--=", "invalid: ${it}")
            }
            highlightDates(bookedDates)
            setDateSelectableFilter { !bookedDates.isContain(it) }
        }
    }

    private fun showPopup(v: View) {
        PopupMenu(v.context, v).apply {
            inflate(R.menu.menu)
            setOnMenuItemClickListener { item ->
                when (item?.itemId) {
                    R.id.exportCalendar -> {
                        vm.getExportCalendar().observeOnUiThread().subscribe({
                            val url =
                                context?.getDownloadUrl("calendars/place_${vm.getPlaceDetail()?.id}.ics") {
                                    if (it.isSuccessful) {
                                        Log.d("--=", "showPopup: ${it.result}")
                                        val clipboard =
                                            context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                        clipboard?.setPrimaryClip(
                                            ClipData.newPlainText(
                                                null,
                                                it.result.toString()
                                            )
                                        )
                                        Toast.makeText(
                                            context,
                                            "Đã copy vào clipboard",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        activity?.showDialog(
                                            "Lỗi",
                                            "Vui lòng thử lại!",
                                            null,
                                            null,
                                            "Cancer"
                                        )
                                    }
                                }
                        }, {
                            activity?.showErrorDialog(it)
                        })
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

        tvSave.onClickDelayAction {
            vm.getPlaceBody().bookingSlots?.apply {
                removeAll { it.status != DateStatus.BOOKED }
                addAll(calendarPicker.selectedDates.map {
                    BookingSlot(
                        it.format(FORMAT_DATE_API),
                        DateStatus.UNAVAILABLE
                    )
                })
                size.apply { Log.d("--=", "+${this}") }
            }

            Log.d("--=", "initListener: ${vm.getPlaceBody()}")
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

    }
}
