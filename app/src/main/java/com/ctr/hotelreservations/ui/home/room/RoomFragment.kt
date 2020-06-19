package com.ctr.hotelreservations.ui.home.room

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.data.source.response.RoomResponse
import com.ctr.hotelreservations.extension.observeOnUiThread
import com.ctr.hotelreservations.extension.onClickDelayAction
import com.ctr.hotelreservations.extension.showErrorDialog
import kotlinx.android.synthetic.main.fragment_room_of_brand.*

/**
 * Created by at-trinhnguyen2 on 2020/06/19
 */
class RoomFragment : BaseFragment() {
    private lateinit var viewModel: RoomVMContract
    private var brandId = -1

    companion object {
        private const val KEY_BRAND = "key_brand"

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
            HotelRepository()
        )
        brandId = arguments?.getParcelable<HotelResponse.Hotel.Brand>(KEY_BRAND)?.id ?: -1
        getRooms(brandId)
        initListener()
        initRecyclerView()
        initSwipeRefresh()
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }
    }

    override fun isNeedPaddingTop() = true

    private fun initRecyclerView() {
        recyclerView.let {
            it.setHasFixedSize(true)
            it.adapter = RoomAdapter(viewModel.getRooms()).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
            }
        }

    }

    private fun handlerItemClick(room: RoomResponse.Room) {
        Log.d("--=", "handlerItemClick: ${room}")
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getRooms(brandId)
        }
    }

    private fun getRooms(brandId: Int) {
        addDisposables(
            viewModel.getAllRoomByBrand(brandId)
                .observeOnUiThread()
                .subscribe({
                    recyclerView.adapter?.notifyDataSetChanged()
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
