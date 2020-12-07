package com.ctr.homestaybooking.data.source

import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.source.datasource.FavoriteLocalDataSource
import com.ctr.homestaybooking.data.source.local.FavoriteLocalDataSourceImpl

/**
 * Created by at-trinhnguyen2 on 2020/12/07
 */

class FavoriteRepository : FavoriteLocalDataSource {

    private val favoriteLocalDataSource = FavoriteLocalDataSourceImpl()

    override fun insert(items: List<Favorite>) = favoriteLocalDataSource.insert(items)

    override fun getFavorites() = favoriteLocalDataSource.getFavorites()

    override fun deleteAll(items: List<Favorite>) = favoriteLocalDataSource.deleteAll(items)

}
