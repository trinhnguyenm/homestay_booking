package com.ctr.homestaybooking.ui.home

import com.ctr.homestaybooking.data.source.datasource.LocalDataSource

/**
 * Created by at-trinhnguyen2 on 2020/11/25
 */

class HomeViewModel(private val localRepository: LocalDataSource) : HomeVMContract {

    override fun isUserSession() = localRepository.isUserSession()

}
