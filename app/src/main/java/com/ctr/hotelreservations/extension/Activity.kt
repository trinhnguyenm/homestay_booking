package com.ctr.hotelreservations.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.ctr.hotelreservations.R
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

/**
 * Use this extension for Activity
 */
private const val HTML_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>"
private const val DEFAULT_KEYBOARD_HEIGHT = 100 // 100 dp
private const val TIME_DELAY = 100L
private const val SNACK_BAR_DURATION = 1000
internal fun FragmentActivity.addFragment(
    containerId: Int,
    fragment: Fragment,
    transactionCallback: ((transaction: FragmentTransaction) -> Unit)? = null,
    addToBackStack: Boolean = false,
    tag: String? = null,
    isAddExisted: Boolean = false
) {
    if (isAddExisted || supportFragmentManager.findFragmentByTag(
            tag ?: fragment.javaClass.simpleName
        ) == null
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        transactionCallback?.invoke(transaction)
        transaction.add(containerId, fragment, tag ?: fragment.javaClass.simpleName)
        if (addToBackStack) {
            transaction.addToBackStack(tag ?: fragment.javaClass.simpleName)
        }
        transaction.commit()

    }
}

internal fun FragmentActivity.replaceFragment(
    containerId: Int,
    fragment: Fragment,
    transactionCallback: ((transaction: FragmentTransaction) -> Unit)? = null,
    addToBackStack: Boolean = false,
    tag: String? = null
) {
    if (supportFragmentManager.findFragmentByTag(tag ?: fragment.javaClass.simpleName) == null) {
        val transaction = supportFragmentManager.beginTransaction()
        transactionCallback?.invoke(transaction)
        transaction.replace(containerId, fragment, tag ?: fragment.javaClass.simpleName)
        if (addToBackStack) {
            transaction.addToBackStack(tag ?: fragment.javaClass.simpleName)
        }
        transaction.commit()
    }
}

internal fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        currentFocus?.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

internal fun FragmentActivity.showDialog(
    title: String?,
    message: String?,
    positiveButtonName: String? = null,
    positiveButtonCallBack: (() -> Unit)? = null,
    negativeButtonName: String? = null,
    negativeButtonCallBack: (() -> Unit)? = null,
    isCancelable: Boolean = false,
    onCancelCallBack: (() -> Unit)? = null
): AlertDialog {
    AlertDialog.Builder(this, R.style.AlertDialog).apply {
        title?.let { setTitle(HtmlCompat.fromHtml("<b>$it</b>", 0)) }
        message?.let {
            if (it.isHtmlString()) {
                setMessage(HtmlCompat.fromHtml(it, 0))
            } else {
                setMessage(it)
            }
        }
        setCancelable(isCancelable)
        setPositiveButton(positiveButtonName ?: getString(R.string.ok)) { _, _ ->
            positiveButtonCallBack?.invoke()
        }
        setNegativeButton(negativeButtonName) { _, _ ->
            negativeButtonCallBack?.invoke()
        }
        setOnCancelListener {
            onCancelCallBack?.invoke()
        }
        return show().apply {
            setCanceledOnTouchOutside(isCancelable)
            getButton(DialogInterface.BUTTON_POSITIVE).typeface =
                Typeface.DEFAULT_BOLD
        }
    }
}

@SuppressLint("WrongConstant")
internal fun FragmentActivity.showSnackbar(
    rootView: View,
    message: String,
    actionText: String? = null,
    actionTextColor: Int? = null,
    rightButtonOnclickListener: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    actionText?.let { action ->
        snackbar.setAction(action) {
            rightButtonOnclickListener?.invoke()
        }
        actionTextColor?.let { color ->
            snackbar.setActionTextColor(color)
        }
    }
    val layoutParams = (snackbar.view.layoutParams as? FrameLayout.LayoutParams)
        ?.apply {
            leftMargin = resources.getDimensionPixelSize(R.dimen.snackbar_margin)
            rightMargin = resources.getDimensionPixelSize(R.dimen.snackbar_margin)
            bottomMargin = resources.getDimensionPixelSize(R.dimen.snackbar_margin)
        }
    snackbar.view.layoutParams = layoutParams
    snackbar.duration = SNACK_BAR_DURATION
    snackbar.show()
}

internal fun String.isHtmlString(): Boolean {
    val pattern = Pattern.compile(HTML_PATTERN)
    val matcher = pattern.matcher(this)
    return matcher.find()
}

internal fun Activity.isKeyboardOpened(): Boolean {
    val r = Rect()
    val visibleThreshold = dip(DEFAULT_KEYBOARD_HEIGHT)
    val activityRoot = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    activityRoot.getWindowVisibleDisplayFrame(r)
    val heightDiff = activityRoot.rootView.height - r.height()
    return heightDiff > visibleThreshold
}
