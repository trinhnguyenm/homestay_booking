package com.ctr.homestaybooking.ui.sheme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.splash.SplashActivity
import jp.monedge.sonybank.ui.scheme.SchemeVMContract

/**
 * Created by at-trinhnguyen2 on 2020/11/19
 */

class SchemeActivity : BaseActivity() {

    companion object {
        internal const val LINK_TYPE = "linkType"
        internal const val SCHEME_SPECIFIC_PART = "schemeSpecificPart"
        internal const val INFORMATION = "information"
    }

    private var viewModel: SchemeVMContract? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = SchemeViewModel(App.instance.localRepository)
        handleOpenFromScheme()
    }

    private fun openSplashActivity(linkType: String? = null, schemeSpecificPart: String? = null) {
        startActivity(
            Intent(this, SplashActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    if (linkType != null) {
                        putExtra(LINK_TYPE, linkType)
                    }
                    if (schemeSpecificPart != null) {
                        putExtra(SCHEME_SPECIFIC_PART, schemeSpecificPart)
                    }
                }
        )
        finishAffinity()
    }

    private fun handleOpenFromScheme() {
        intent?.data?.let { uri ->
            Log.d("--=", "handleOpenFromScheme: ${uri}")
            val schemeSpecificParts = uri.schemeSpecificPart.split("/")
            val linkTypes = resources.getStringArray(R.array.linkTypes)
            for (part in schemeSpecificParts) {
                if (App.instance.currentActivity != null && App.instance.currentActivity !is SplashActivity
                    && !viewModel?.getToken().isNullOrBlank()
                ) {
                    finish()
                    return
                } else {
                    if (linkTypes.contains(part)) {
                        openSplashActivity(
                            part.apply { Log.d("--=", "+${this}") },
                            uri.schemeSpecificPart.apply { Log.d("--=", "+${this}") })
                        return
                    }
                }
            }
        }
        openSplashActivity()
    }
}
