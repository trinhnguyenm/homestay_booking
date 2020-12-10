package com.ctr.homestaybooking.ui.home.favotite

import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.source.datasource.FavoriteLocalDataSource
import com.ctr.homestaybooking.data.source.response.Place

/**
 * Created by at-trinhnguyen2 on 2020/12/07
 */
open class FavoriteViewModel(
    private val favoriteRepository: FavoriteLocalDataSource
) : FavoriteVMContract {
    private val places = mutableListOf<Place>()

    override fun getPlaces() = places

    override fun insert(items: List<Favorite>) = favoriteRepository.insert(items)

    override fun getFavorites() = favoriteRepository.getFavorites()

    override fun deleteAll(items: List<Favorite>) = favoriteRepository.deleteAll(items)

}
