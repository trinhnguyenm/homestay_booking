package com.ctr.homestaybooking.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.wedget.ProgressBarDialog
import com.ctr.homestaybooking.util.SharedReferencesUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.dialog_progress_bar.*
import java.text.NumberFormat
import java.util.*

/**
 * Use this extension for Context
 */
internal fun Context.dimen(id: Int) = resources.getDimensionPixelSize(id)

internal fun Context.dip(value: Int) = (value * resources.displayMetrics.density).toInt()

internal fun Context.dip(value: Float) = (value * resources.displayMetrics.density).toInt()

internal fun Context.getScreenWidth() = resources.displayMetrics.widthPixels

internal fun Context.getScreenHeight() = resources.displayMetrics.heightPixels

internal fun formatNumber(number: Long) =
    NumberFormat.getNumberInstance(Locale.JAPANESE).format(number)

internal fun Context.openWebView(url: String) {
    val packageName = "com.android.chrome"
    if (!isAppInstalled(packageName)) {
        (this as? androidx.fragment.app.FragmentActivity)?.showDialog(
            title = "",
            message = getString(R.string.dialogMessageNotInstallGoogleChrome),
            positiveButtonName = getString(R.string.ok),
            positiveButtonCallBack = {
                openStoreGoogleChrome(this, packageName)
            },
            isCancelable = true
        )
        return
    }
    val app = ((this as? Activity)?.application as? App)
    val serviceConnection = object : CustomTabsServiceConnection() {
        override fun onCustomTabsServiceConnected(
            componentName: ComponentName,
            client: CustomTabsClient
        ) {
            client.warmup(0L)
            val handler = Handler()
            val customTabsIntent =
                CustomTabsIntent.Builder(client.newSession(object : CustomTabsCallback() {
                    override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
                        super.onNavigationEvent(navigationEvent, extras)
                        when (navigationEvent) {
                            TAB_SHOWN -> app?.onActivityStarted(null)
                            TAB_HIDDEN ->
                                handler.postDelayed({
                                    app?.onActivityStopped(null)
                                }, 300)
                        }
                    }
                })).build()
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(this@openWebView, Uri.parse(url))
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }
    val isBind = CustomTabsClient.bindCustomTabsService(this, packageName, serviceConnection)
    if (!isBind) {
        //Todo: Handle later
        Toast.makeText(this, "Please enable Chrome", Toast.LENGTH_LONG).show()
    }
}

internal fun Activity.openBrowser(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(browserIntent)
}

internal fun Context.getStatusBarHeightt(): Int {
    return SharedReferencesUtil.getInt(this, SharedReferencesUtil.KEY_STATUS_BAR_HEIGHT, 0)
}

internal fun Context.setMarginForToolBarRelative(rlToolbar: RelativeLayout) {
    val lp = rlToolbar.layoutParams as? RelativeLayout.LayoutParams
    lp?.topMargin = getStatusBarHeight()
    rlToolbar.layoutParams = lp
}

internal fun Context.getNavigationBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        return resources.getDimensionPixelSize(resourceId)
    }
    return 0
}

private fun openStoreGoogleChrome(context: Context, appPackageName: String) {
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
        )
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

private fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

internal fun Context.getStatusBarHeight(): Int {
    val resourceId: Int =
        resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}


internal fun Context?.uploadImageFirebase(imagesPicked: List<Uri>, task: (Task<Uri>) -> Unit) {
    val storageRef = FirebaseStorage.getInstance()
        .getReference("files")
    var count = 0
    val progressBarDialog = this?.let { ProgressBarDialog(it) }
    imagesPicked.forEachIndexed { index, uri ->
        if (index == 0) {
            progressBarDialog?.apply {
                show()
                tvPoorInternet?.visible()
                tvPoorInternet?.text = "${count + 1}/${imagesPicked.size}"
            }
        }
        val imageRef = storageRef.child(UUID.randomUUID().toString() + "_image")
        val uploadTask = imageRef.putFile(uri)
        uploadTask
            .addOnProgressListener {
                progressBarDialog?.progressBar?.progress =
                    (it.bytesTransferred / it.totalByteCount).toInt()
            }
            .addOnCompleteListener {
                count++
                progressBarDialog?.tvPoorInternet?.text =
                    "${count + 1}/${imagesPicked.size}"
                if (count == imagesPicked.size) {
                    progressBarDialog?.dismiss()
                }
            }
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener(task)
    }
}


internal fun Context?.getDownloadUrl(fileName: String, task: (Task<Uri>) -> Unit) {
    FirebaseStorage.getInstance().reference.child(fileName).downloadUrl.addOnCompleteListener(task)
}
