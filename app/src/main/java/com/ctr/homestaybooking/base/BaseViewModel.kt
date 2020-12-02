package com.ctr.homestaybooking.base

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
open class BaseViewModel {
    internal val progressBarDialogObservable = BehaviorSubject.create<Boolean>()

    internal fun <T> Single<T>.addProgressLoading() = this.doOnSubscribe {
        progressBarDialogObservable.onNext(true)
    }.doFinally {
        progressBarDialogObservable.onNext(false)
    }
}
