package com.ctr.homestaybooking.ui.home.favotite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.source.FavoriteRepository
import com.ctr.homestaybooking.data.source.response.Place
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.home.HomeActivity.Companion.TAB_HOME_POSITION
import com.ctr.homestaybooking.ui.home.MyMainActivity
import com.ctr.homestaybooking.ui.home.places.PlaceAdapter
import com.ctr.homestaybooking.ui.placedetail.PlaceDetailActivity
import com.ctr.homestaybooking.util.toCalendar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.layout_view_no_data.*

/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class FavoriteFragment : BaseFragment() {
    private lateinit var viewModel: FavoriteVMContract

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = FavoriteViewModel(FavoriteRepository())
        subscribeFavoritesInDatabase()
        initRecyclerView()
        initListener()
    }

    private fun subscribeFavoritesInDatabase() {
        viewModel.let { vm ->
            vm.getFavorites().observeOnUiThread()
                .subscribe({ favorites ->
                    vm.getPlaces().apply {
                        clear()
                        addAll(favorites.map { Gson().fromJson(it.place, Place::class.java) })
                        sortedByDescending {
                            it.likedTime?.toCalendar()?.time
                        }
                    }
                    if (vm.getPlaces().isEmpty()) {
                        recyclerView.gone()
                        llNoData.visible()
                    } else {
                        recyclerView.visible()
                        llNoData.gone()
                    }
                    recyclerView?.adapter?.notifyItemRangeChanged(0, vm.getPlaces().size)
                }, {})
        }
    }

    private fun initRecyclerView() {
        recyclerView.let {
            it.setHasFixedSize(true)
            it.adapter = PlaceAdapter(viewModel.getPlaces()).also { adapter ->
                adapter.onItemClicked = this::handlerItemClick
                adapter.onLikeClicked = this::handlerLikeClick
            }
        }
    }

    private fun handlerItemClick(place: Place) {
        place.id.let { PlaceDetailActivity.start(this, it) }
    }

    private fun handlerLikeClick(place: Place) {
        if (place.isLike) {
            viewModel.insert(listOf(Favorite(place.id, place.toJsonString()))).observeOnUiThread()
                .subscribe()
        } else {
            viewModel.deleteAll(listOf(Favorite(place.id, place.toJsonString())))
                .observeOnUiThread().subscribe()
        }
    }

    private fun initListener() {
        tvActionMore.onClickDelayAction {
            (activity as? MyMainActivity)?.setTabSelection(TAB_HOME_POSITION)
        }
    }

    override fun isNeedPaddingTop() = false
}
