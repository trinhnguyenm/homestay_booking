package com.ctr.homestaybooking.ui.home.account

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.Role
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.extension.observeOnUiThread
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.extension.showDialog
import com.ctr.homestaybooking.extension.showErrorDialog
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.auth.AuthActivity
import com.ctr.homestaybooking.ui.editprofile.EditProfileActivity
import com.ctr.homestaybooking.ui.editprofile.EditProfileActivity.Companion.KEY_IS_NEED_UPDATE
import com.ctr.homestaybooking.ui.editprofile.EditProfileActivity.Companion.REQUEST_CODE_EDIT_PROFILE
import com.ctr.homestaybooking.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.fragment_profile.*
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = AccountViewModel(
            App.instance.localRepository,
            UserRepository()
        )
        getUserInfo()
        initSwipeRefresh()
        initListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_EDIT_PROFILE) {
            data?.getBooleanExtra(KEY_IS_NEED_UPDATE, false)?.let {
                if (it) {
                    getUserInfo()
                }
            }
        }
    }

    override fun getProgressObservable() = vm.getProgressObservable()

    private fun initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAzureRadiance)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh?.isRefreshing = false
            }, 300L)
            getUserInfo()
        }
    }

    private fun initListener() {
        llPersonalInfo.onClickDelayAction {
        }

        llSupport.onClickDelayAction {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:1800 6969")
            startActivity(intent)
        }

        llPersonalInfo.onClickDelayAction {
            EditProfileActivity.start(this)
        }

        llLogout.onClickDelayAction {
            activity?.showDialog(
                getString(R.string.warning),
                getString(R.string.do_you_want_logout),
                getString(R.string.ok),
                {
                    ChatSDK.auth().logout().observeOn(RX.main())
                        .doOnSubscribe {
                            vm.getProgressObservable().onNext(true)
                        }.doFinally {
                            vm.getProgressObservable().onNext(false)
                        }
                        .subscribe {
                            App.instance.localRepository.removeToken()
                            App.instance.localRepository.removeUserId()
                            App.instance.localRepository.setUserSession()
                            activity?.let {
                                AuthActivity.start(
                                    this,
                                    isOpenLogin = true,
                                    isShowButtonBack = false
                                )
                                finishAffinity(it)
                            }
                        }
                },
                getString(R.string.cancer),
                isCancelable = true
            )
        }

        llUserType.onClickDelayAction {
            Log.d("--=", "isUserSession: ${vm.isUserSession()}")
            vm.getUserResponse()?.userDetail?.let { userDetail ->
                if (!userDetail.roles.contains(Role.ROLE_HOST)) {
                    Log.d("--=", "!contains: ROLE_HOST")
                    vm.upToHost().observeOnUiThread().subscribe({
                        vm.setHostSession()
                        startActivity(Intent(activity, SplashActivity::class.java))
                        activity?.finish()
                    }, {
                        activity?.showErrorDialog(it)
                    })
                } else {
                    if (vm.isUserSession()) {
                        vm.setHostSession()
                    } else {
                        vm.setUserSession()
                    }
                    startActivity(Intent(activity, SplashActivity::class.java))
                    activity?.finish()
                }
            }

        }
    }

    private fun getUserInfo() {
        vm.getUserInfo()
            .observeOnUiThread()
            .subscribe({
                context?.let { context ->
                    Glide.with(context).load(it.userDetail.imageUrl).into(imgAvatar)
                }
                tvName.text = it.userDetail.getName()
                tvEmail.text = it.userDetail.email
                if (it.userDetail.roles.contains(Role.ROLE_HOST)) {
                    if (App.instance.localRepository.isUserSession()) {
                        tvUserType.text = getString(R.string.user_type_host)
                    } else {
                        tvUserType.text = getString(R.string.user_type_travel)
                    }
                } else {
                    tvUserType.text = getString(R.string.become_host)
                }
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }
}
