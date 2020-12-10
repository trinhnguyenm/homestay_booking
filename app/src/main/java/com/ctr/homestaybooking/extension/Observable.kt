package com.ctr.homestaybooking.extension

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
internal fun <T> Observable<T>.observeOnUiThread(): Observable<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

internal fun <T> Single<T>.observeOnUiThread(): Single<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

internal fun <T> Flowable<T>.observeOnUiThread(): Flowable<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

internal fun Completable.observeOnUiThread(): Completable =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
