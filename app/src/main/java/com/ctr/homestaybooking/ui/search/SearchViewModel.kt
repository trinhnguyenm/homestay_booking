package com.ctr.homestaybooking.ui.search

import com.ctr.homestaybooking.base.BaseViewModel
import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.model.SearchBody
import com.ctr.homestaybooking.data.source.FavoriteRepository
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.data.source.datasource.LocalDataSource
import com.ctr.homestaybooking.data.source.response.Place
import com.ctr.homestaybooking.data.source.response.PlaceResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject


class SearchViewModel(
    private val localRepository: LocalDataSource,
    private val placeRepository: PlaceRepository,
    private val favoriteRepository: FavoriteRepository
) : SearchVMContract, BaseViewModel() {

    private val places = mutableListOf<Place>()

    private val searchBody = SearchBody()

    private val searchBodyEdit = SearchBody()

    override fun getPlaces(): MutableList<Place> = places

    override fun getSearchBody() = searchBody

    override fun getSearchBodyEdit() = searchBodyEdit

    override fun copy(from: SearchBody, to: SearchBody) {
        to.apply {
            from.let {
                address = it.address
                bookingType = it.bookingType
                guestCount = it.guestCount
                roomCount = it.roomCount
                bedCount = it.bedCount
                bathroomCount = it.bathroomCount
                minPrice = it.minPrice
                maxPrice = it.maxPrice
                cancelType = it.cancelType
            }
        }
    }

    override fun searchPlaces(): Single<PlaceResponse> {
        return placeRepository.searchPlace(searchBody)
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
