package com.ctr.homestaybooking.ui.home.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.extension.observeOnUiThread
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.extension.showDialog
import com.ctr.homestaybooking.extension.showErrorDialog
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.auth.AuthActivity
import com.ctr.homestaybooking.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.fragment_account.*
import sdk.chat.core.session.ChatSDK
import sdk.guru.common.RX


/**
 * Created by at-trinhnguyen2 on 2020/06/02
 */
class AccountFragment : BaseFragment() {
    private lateinit var vm: AccountVMContract

    companion object {
        fun newInstance() =
            AccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = AccountViewModel(
            App.instance.localRepository,
            UserRepository()
        )
        if (App.instance.localRepository.isHostSession()) {
            tvUserType.text = "Chủ nhà"
        } else {
            tvUserType.text = "Du lịch"
        }

        getUserInfo()

        tvViewProfile.onClickDelayAction {
        }

        llSupport.onClickDelayAction {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:1800 6969")
            startActivity(intent)
        }

        tvLogout.onClickDelayAction {
            activity?.showDialog("Warning", "Do you want to logout?", "OK", {
                ChatSDK.auth().logout().observeOn(RX.main()).subscribe {
                    Log.d("--=", "logout ")
                    App.instance.localRepository.removeToken()
                    App.instance.localRepository.removeUserId()
                    App.instance.localRepository.setUserSession()
                    activity?.let {
                        AuthActivity.start(this, isOpenLogin = true, isShowButtonBack = false)
                        finishAffinity(it)
                    }
                }
            })
        }

        llSwap.onClickDelayAction {
            if (vm.isHostSession()) {
                vm.setUserSession()
            } else {
                vm.setHostSession()
            }
            startActivity(Intent(activity, SplashActivity::class.java))
            activity?.finish()
        }
    }

    private fun getUserInfo() {
        vm.getUserInfo()
            .observeOnUiThread()
            .subscribe({
                context?.let { context ->
                    Glide.with(context).load(it.body.imageUrl).into(imgAvatar)
                }
                tvName.text = it.body.firstName + " " + it.body.lastName
                tvEmail.text = it.body.email
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }
}
