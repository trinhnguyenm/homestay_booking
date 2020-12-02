package com.ctr.homestaybooking.ui.home.rooms

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.response.HotelResponse
import com.ctr.homestaybooking.data.source.response.RoomTypeResponse
import com.ctr.homestaybooking.extension.observeOnUiThread
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.extension.showErrorDialog
import com.ctr.homestaybooking.ui.wedget.NumberPicker
import com.ctr.homestaybooking.util.format
import kotlinx.android.synthetic.main.fragment_room_of_brand.*
import kotlinx.android.synthetic.main.include_layout_select_date.*
import java.util.*


/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class RoomFragment : BaseFragment() {
    private lateinit var viewModel: RoomVMContract
    private lateinit var brand: HotelResponse.Hotel.Brand
    private val startDate = Calendar.getInstance()
    private val endDate = Calendar.getInstance().apply { add(Calendar.DATE, 1) }
    private var numOfGuest = 1
    private var numOfRoom = 1

    companion object {
        internal const val KEY_BRAND = "key_brand"

        fun getInstance(brand: HotelResponse.Hotel.Brand) =
            RoomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        KEY_BRAND,
                        brand
                    )
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room_of_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = RoomViewModel(
            PlaceRepository()
        )
        brand = arguments?.getParcelable(KEY_BRAND)
            ?: HotelResponse.Hotel.Brand()
        brand.let {
            context?.let { context -> Glide.with(context).load(it.imgLink).into(ivBrand) }
            tvName.text = it.name
            tvDescription.text =
                if (it.desciption.isNullOrEmpty()) "Brand have no description" else it.desciption
        }
        getAllRoomStatus(
            brand.id,
            startDate.format(),
            endDate.format(),
            numOfGuest,
            numOfRoom
        )
        initListener()
        initRecyclerView()
        initSwipeRefresh()
    }

    override fun isNeedPaddingTop() = true

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        viewSelected.onClickDelayAction {
            val calendar = Calendar.getInstance()
        }

        lnGuests.onClickDelayAction {
            context?.let { context ->
                val dialog = Dialog(context)
                dialog.apply {
                    setContentView(R.layout.layout_dialog_number_picker)
                    val pickerGuestNo = findViewById<NumberPicker>(R.id.pickerAdultNo)
                    val pickerRoomNo = findViewById<NumberPicker>(R.id.pickerChildrenNo)
                    pickerGuestNo.apply {
                        setMax(50)
                        setValue(numOfGuest)
                    }
                    pickerRoomNo.apply {
                        setMax(50)
                        setValue(numOfRoom)
                    }

                    setOnDismissListener {
                        Log.d("--=", "initListener: setOnDismissListener")
                        numOfGuest = pickerGuestNo.getValue()
                        numOfRoom = pickerRoomNo.getValue()
                        viewModel.filterRoomStatus(numOfGuest, numOfRoom)
                        this@RoomFragment.recyclerView.adapter?.notifyDataSetChanged()
                        this@RoomFragment.tvGuests.text =
                            if (numOfGuest < 10) "0$numOfGuest" else numOfGuest.toString()
                    }
                    show()
                }
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView.let {
            it.setHasFixedSize(true)
            it.adapter = RoomAdapter(viewModel.getRoomTypes(), brand).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
            }
        }
    }

    private fun handlerItemClick(room: RoomTypeResponse.RoomTypeStatus) {
//        PlaceDetailActivity.start(
//            this,
//            Gson().fromJson(brand.toJsonString(), HotelResponse.Hotel.Brand::class.java),
//            room,
//            startDate.parseToString(),
//            endDate.parseToString()
//        )
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getAllRoomStatus(
                brand.id,
                startDate.format(),
                endDate.format(),
                numOfGuest,
                numOfRoom
            )
        }
    }

    private fun getAllRoomStatus(
        brandId: Int,
        startDate: String,
        endDate: String,
        numOfGuest: Int,
        numOfRoom: Int
    ) {
        viewModel.getAllRoomStatus(brandId, startDate, endDate, numOfGuest, numOfRoom)
            .observeOnUiThread()
            .subscribe({
                recyclerView.adapter?.notifyDataSetChanged()
            }, {
                handlerGetApiError(it)
            }).addDisposable()
    }


    private fun handlerGetApiError(throwable: Throwable) {
        activity?.showErrorDialog(throwable)
    }

    override fun getProgressObservable() = viewModel.getProgressObservable()
}
