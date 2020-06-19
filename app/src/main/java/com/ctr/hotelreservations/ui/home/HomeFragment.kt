package com.ctr.hotelreservations.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.HotelRepository
import com.ctr.hotelreservations.extension.observeOnUiThread
import com.ctr.hotelreservations.extension.showErrorDialog
import com.ctr.hotelreservations.ui.App
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class HomeFragment : BaseFragment() {
    private lateinit var viewModel: HomeVMContract

    companion object {
        fun getInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = HomeViewModel(App.instance.localRepository, HotelRepository())
        getHotels()
        addDisposables()
        initRecyclerView()
        initSwipeRefresh()
    }

    private fun initRecyclerView() {
        val homeAdapter = HotelAdapter(viewModel.getHotelList())
        rcvHome.apply {
            setHasFixedSize(true)
            adapter = homeAdapter
        }

        homeAdapter.onItemClicked = {
            Toast.makeText(context, "Clicked ${it.id}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getHotels()
        }
    }

    private fun getHotels() {
        addDisposables(
            viewModel.getHotels()
                .observeOnUiThread()
                .subscribe({
                    rcvHome.adapter?.notifyDataSetChanged()
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
