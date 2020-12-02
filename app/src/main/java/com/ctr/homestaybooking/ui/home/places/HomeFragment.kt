package com.ctr.homestaybooking.ui.home.places

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.response.Place
import com.ctr.homestaybooking.extension.observeOnUiThread
import com.ctr.homestaybooking.extension.showErrorDialog
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.placedetail.PlaceDetailActivity
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class HomeFragment : BaseFragment() {
    private lateinit var viewModel: HomeVMContract

    companion object {
        fun getInstance() =
            HomeFragment()
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
        viewModel = HomeViewModel(
            App.instance.localRepository,
            PlaceRepository()
        )
        getPlaceFromServer()
        initRecyclerView()
        initSwipeRefresh()
    }

    private fun initRecyclerView() {

        recyclerView.let {
            it.setHasFixedSize(true)
            it.adapter = PlaceAdapter(viewModel.getPlaces()).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
            }
        }
    }

    private fun handlerItemClick(place: Place) {
        place.id?.let { PlaceDetailActivity.start(this, it) }
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getPlaceFromServer()
        }
    }

    private fun getPlaceFromServer() {
        viewModel.getPlacesFromServer()
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
