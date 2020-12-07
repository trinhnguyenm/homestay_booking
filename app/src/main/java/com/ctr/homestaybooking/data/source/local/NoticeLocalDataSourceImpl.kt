package com.ctr.homestaybooking.data.source.local

import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.source.datasource.FavoriteLocalDataSource
import com.ctr.homestaybooking.data.source.local.db.AppDatabase
import com.ctr.homestaybooking.data.source.local.db.FavoriteDao
import com.ctr.homestaybooking.ui.App
import io.reactivex.Flowable

class FavoriteLocalDataSourceImpl(
    private val favoriteDao: FavoriteDao =
        AppDatabase.getInstance(App.instance.applicationContext).favoriteDao()
) : FavoriteLocalDataSource {
    override fun insert(items: List<Favorite>) = favoriteDao.insert(items)

    override fun getFavorites(): Flowable<List<Favorite>> = favoriteDao.getFavorites()

    override fun deleteAll(items: List<Favorite>) = favoriteDao.deleteAll(items)

}
