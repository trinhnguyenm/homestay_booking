package com.ctr.hotelreservations.base

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ctr.hotelreservations.bus.RxBus
import com.ctr.hotelreservations.data.model.KeyBoardEvent
import com.ctr.hotelreservations.extension.getStatusBarHeight
import com.ctr.hotelreservations.extension.hideKeyboard
import com.ctr.hotelreservations.extension.isKeyboardOpened
import com.ctr.hotelreservations.extension.observeOnUiThread
import com.ctr.hotelreservations.ui.wedget.ProgressBarDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/05/30
 */
abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private const val DELAY_FOR_HIDE_KEYBOARD = 100L
    }

    private var isKeyboardShowed = false
    private var isKeyboardListenerAttach = false
    private val compositeDisposables = CompositeDisposable()
    private var progressDialog: ProgressBarDialog? = null

    private val keyBoardListener = ViewTreeObserver.OnGlobalLayoutListener {
        Handler().postDelayed({
            val isShow = isKeyboardOpened()
            if (isShow != isKeyboardShowed) {
                isKeyboardShowed = isShow
                RxBus.publish(KeyBoardEvent(isKeyboardShowed))
            }
        }, DELAY_FOR_HIDE_KEYBOARD)
    }

    open fun getContainerId(): Int = -1

    open fun getCurrentFragment(): Fragment? =
        supportFragmentManager.findFragmentById(getContainerId())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressBarDialog(this)
    }

    override fun onResume() {
        super.onResume()
        getProgressBarControlObservable()?.let {
            addDisposables(
                it.observeOnUiThread()
                    .subscribe(this::setProgressBarState)
            )
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposables.clear()
    }

    protected fun attachKeyboardListener(rootView: ViewGroup) {
        if (isKeyboardListenerAttach) {
            return
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(keyBoardListener)
        isKeyboardListenerAttach = true
    }

    protected fun removeKeyboardListener(rootView: ViewGroup) {
        if (isKeyboardListenerAttach) {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(keyBoardListener)
        }
    }

    protected fun paddingTop(vararg view: View) {
        view.forEach { it.setPadding(0, getStatusBarHeight(), 0, 0) }
    }

    protected open fun getProgressBarControlObservable(): BehaviorSubject<Boolean>? = null

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach {
            compositeDisposables.add(it)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view = currentFocus
        val ret = super.dispatchTouchEvent(event)
        val location = IntArray(2)
        when (event.action) {
            MotionEvent.ACTION_UP ->
                if (view is EditText) {
                    if (currentFocus !is EditText) {
                        currentFocus?.clearFocus()
                        view.hideKeyboard()
                    } else {
                        currentFocus?.let {
                            it.getLocationOnScreen(location)
                            val x = event.rawX + it.left - location[0]
                            val y = event.rawY + it.top - location[1]
                            if (x < it.left || x >= it.right || y < it.top || y > it.bottom) {
                                it.clearFocus()
                                it.hideKeyboard()
                            }
                        }
                    }
                }
        }
        return ret
    }

    protected fun fullScreenActivity() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val winParams = window.attributes
        winParams.flags =
            winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        window.attributes = winParams
    }

    protected fun setProgressBarState(state: Boolean) {
        if (state) {
            progressDialog?.show()
        } else {
            progressDialog?.dismiss()
        }
    }
}