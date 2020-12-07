package com.ctr.homestaybooking.ui.editprofile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseActivity
import com.ctr.homestaybooking.extension.addFragment

/**
 * Created by at-trinhnguyen2 on 2020/12/06
 */

class EditProfileActivity : BaseActivity() {

    companion object {
        internal fun start(
            from: Fragment
        ) {
            EditProfileActivity()
                .apply {
                    val intent = Intent(from.activity, EditProfileActivity::class.java)
                    from.startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE)
                }
        }

        internal const val REQUEST_CODE_EDIT_PROFILE = 1335
        internal const val KEY_IS_NEED_UPDATE = "key_is_need_update"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        addFragment(getContainerId(), EditProfileFragment.newInstance())
    }

    override fun getContainerId() = R.id.container
}
