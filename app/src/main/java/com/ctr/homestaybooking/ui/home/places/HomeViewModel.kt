package com.ctr.homestaybooking.ui.home.places

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.source.FavoriteRepository
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.datasource.LocalDataSource
import com.ctr.homestaybooking.data.source.response.Place
import com.ctr.homestaybooking.data.source.response.PlaceResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class HomeViewModel(
    private val localRepository: LocalDataSource,
    private val placeRepository: PlaceRepository,
    private val favoriteRepository: FavoriteRepository
) : HomeVMContract, BaseViewModel() {

    private val places = mutableListOf<Place>()

    override fun getPlaces(): MutableList<Place> = places

    override fun getPlacesFromServer(): Single<PlaceResponse> {
        return placeRepository.getPlaces()
            .addProgressLoading()
            .doOnSuccess { response ->
                getPlaces().apply {
                    clear()
                    addAll(response.places)
                }
            }
    }

    override fun insert(items: List<Favorite>) = favoriteRepository.insert(items)

    override fun getFavorites() = favoriteRepository.getFavorites()

    override fun deleteAll(items: List<Favorite>) = favoriteRepository.deleteAll(items)

    override fun getProgressObservable(): BehaviorSubject<Boolean> =
        progressBarDialogObservable
}
