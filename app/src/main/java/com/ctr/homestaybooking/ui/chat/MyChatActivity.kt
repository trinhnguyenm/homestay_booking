package com.ctr.homestaybooking.ui.chat

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.extension.getStatusBarHeight
import sdk.chat.ui.activities.ChatActivity

/**
 * Created by at-trinhnguyen2 on 2020/12/07
 */
class MyChatActivity : ChatActivity() {
    override fun getLayout() = R.layout.my_activity_chat

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        searchView.isEnabled = false
        paddingTop(root)
    }

    private fun paddingTop(vararg view: View) {
        view.forEach { it.setPadding(0, getStatusBarHeight(), 0, 0) }
    }
}
