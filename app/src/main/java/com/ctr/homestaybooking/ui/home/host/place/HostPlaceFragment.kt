package com.ctr.homestaybooking.ui.home.host.place

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.response.PlaceDetail
import com.ctr.homestaybooking.extension.observeOnUiThread
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.extension.showErrorDialog
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.setupplace.PlaceSetupActivity
import kotlinx.android.synthetic.main.fragment_host_place.*

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class HostPlaceFragment : BaseFragment() {
    private lateinit var viewModel: HostPlaceVMContract

    companion object {
        fun getInstance() =
            HostPlaceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_host_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = HostPlaceViewModel(
            App.instance.localRepository,
            PlaceRepository()
        )
        getPlaceFromServer()
        initListener()
        initRecyclerView()
        initSwipeRefresh()
    }

    override fun getProgressObservable() = viewModel.getProgressObservable()

    private fun initListener() {
        ivStartAction.onClickDelayAction {
            activity?.let { PlaceSetupActivity.start(it, 0) }
        }
    }

    private fun initRecyclerView() {
        recyclerView.let {
            it.setHasFixedSize(true)
            it.adapter = HostPlaceAdapter(viewModel.getPlaces()).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
            }
        }
    }

    private fun handlerItemClick(place: PlaceDetail) {
        place.id.let { activity?.let { it1 -> PlaceSetupActivity.start(it1, it) } }
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
}
