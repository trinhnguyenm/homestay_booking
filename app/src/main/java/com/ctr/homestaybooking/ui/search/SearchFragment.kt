package com.ctr.homestaybooking.ui.search

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.source.response.Place
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.home.places.PlaceAdapter
import com.ctr.homestaybooking.ui.placedetail.PlaceDetailActivity
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * Created by at-trinhnguyen2 on 2020/12/21
 */
class SearchFragment : BaseFragment() {
    private lateinit var vm: SearchVMContract

    companion object {
        fun getInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? SearchActivity)?.let {
            vm = it.vm
        }
        edtSearch.requestFocus()
        initRecyclerView()
        initSwipeRefresh()
        initListener()
    }

    override fun isNeedPaddingTop() = true

    private fun initListener() {
        ivBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        edtSearch.afterTextChanged {
            vm.getSearchBody().address = it
            vm.getSearchBodyEdit().address = it
        }
        edtSearch.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (!edtSearch.text.isNullOrBlank()) {
                    searchPlaces()
                }
            }
        }
        ivFilter.onClickDelayAction {
            (activity as? SearchActivity)?.openFilterFragment()
        }
    }

    private fun subscribeFavoritesInDatabase() {
        vm.let { vm ->
            vm.getFavorites().observeOnUiThread()
                .subscribe({ favorites ->
                    vm.getPlaces().forEach { place ->
                        place.isLike = favorites.map { it.id }.contains(place.id)
                    }
                    recyclerView?.adapter?.notifyDataSetChanged()
                }, {})
        }
    }

    private fun initRecyclerView() {
        recyclerView.let {
            it.setHasFixedSize(true)
            it.adapter = PlaceAdapter(vm.getPlaces()).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
                adapter.onLikeClicked = this::handlerLikeClick
            }
        }
        val locations = mutableListOf(
            Location("Hà Nội", R.drawable.img_location_ha_noi),
            Location("Đà Nẵng", R.drawable.img_location_da_nang),
            Location("Hồ Chí Minh", R.drawable.img_location_ho_chi_minh),
            Location("Hội An", R.drawable.img_location_hoi_an),
            Location("Đà Lạt", R.drawable.img_location_da_lat),
            Location("Vũng tàu", R.drawable.img_location_vung_tau)
        )
        recyclerViewLocation.let {
            it.setHasFixedSize(true)
            it.adapter = LocationAdapter(locations).also { adapter ->
                adapter.onItemClicked = this::handlerItemLocationClick
            }
        }
    }

    private fun handlerItemClick(place: Place) {
        place.id.let { PlaceDetailActivity.start(this, it) }
    }

    private fun handlerLikeClick(place: Place) {
        if (place.isLike) {
            vm.insert(listOf(Favorite(place.id, place.toJsonString())))
                .observeOnUiThread().subscribe()
        } else {
            vm.deleteAll(listOf(Favorite(place.id, place.toJsonString())))
                .observeOnUiThread().subscribe()
        }
    }

    private fun handlerItemLocationClick(location: Location) {
        edtSearch.setText(location.name)
        searchPlaces()
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            searchPlaces()
        }
    }

    internal fun searchPlaces() {
        vm.getSearchBody().apply { Log.d("--=", "body+${this}") }
        vm.getSearchBodyEdit().apply { Log.d("--=", "edit+${this}") }
        vm.searchPlaces()
            .observeOnUiThread()
            .subscribe({
                if (it.length == 0) {
                    rlLocation.visible()
                } else {
                    rlLocation.gone()
                }
                subscribeFavoritesInDatabase()
                recyclerView.adapter?.notifyDataSetChanged()
            }, {
                handlerGetApiError(it)
            }).addDisposable()
    }


    private fun handlerGetApiError(throwable: Throwable) {
        activity?.showErrorDialog(throwable)
    }

    override fun getProgressObservable() = vm.getProgressObservable()
}
