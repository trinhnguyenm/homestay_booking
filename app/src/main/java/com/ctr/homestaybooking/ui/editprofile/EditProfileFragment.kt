package com.ctr.homestaybooking.ui.editprofile

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseChooseImageFragment
import com.ctr.homestaybooking.data.model.Gender
import com.ctr.homestaybooking.data.source.UserRepository
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.ui.wedget.CustomDatePickerDialog
import com.ctr.homestaybooking.ui.wedget.SpinnerType
import com.ctr.homestaybooking.util.DateUtil.FORMAT_DATE
import com.ctr.homestaybooking.util.FORMAT_DATE_API
import com.ctr.homestaybooking.util.convert
import com.ctr.homestaybooking.util.format
import com.ctr.homestaybooking.util.toCalendar
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.input_time_layout.view.*
import java.io.File
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/12/06
 */

class EditProfileFragment : BaseChooseImageFragment() {
    private lateinit var vm: EditProfileVMContract
    private var estimationDateTime = Calendar.getInstance()
    private var datePickerDialogEstimationDate: CustomDatePickerDialog? = null


    companion object {
        fun newInstance() = EditProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = EditProfileViewModel(App.instance.localRepository, UserRepository())
        initDatePicker()
        getUserInfo()
        initView()
        initListener()
    }

    override fun getImageView(): ImageView = imgAvatar

    override fun getPathImageSuccess(path: String) {
        context?.uploadImageFirebase(listOf(Uri.fromFile(File(path)))) { task ->
            if (task.isSuccessful) {
                vm.getUserBody().imageUrl = task.result.toString()
                updateNextButton()
            } else {
                task.exception?.let {
                    activity?.showErrorDialog(it)
                }
            }
        }
    }

    override fun isNeedPaddingTop() = true

    private fun initView() {
        inputFirstName.validateData = {
            it.trim().isNotEmpty()
        }

        inputLastName.validateData = {
            it.trim().isNotEmpty()
        }

        inputPhone.validateData = {
            it.trim().length == 10
        }

        inputEmail.validateData = {
            it.trim().isNotEmpty() && it.isEmailValid()
        }

        inputGender.initSpinner(SpinnerType.GENDER)
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        inputFirstName.afterTextChange = {
            vm.getUserBody().firstName = it
            updateNextButton()
        }

        inputLastName.afterTextChange = {
            vm.getUserBody().lastName = it
            updateNextButton()
        }
        inputPhone.afterTextChange = {
            vm.getUserBody().phoneNumber = it
            updateNextButton()
        }

        inputEmail.afterTextChange = {
            vm.getUserBody().email = it
            updateNextButton()
        }

        inputGender.onItemSelectedListener = { item ->
            vm.getUserBody().gender = Gender.values().find { it.name == item?.getCode() }
            updateNextButton()
        }

        inputBirthday.tvContent.onClickDelayAction {
            handleShowDatePicker(inputBirthday.tvContent, datePickerDialogEstimationDate)
        }

        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        imgAvatar.onClickDelayAction {
            showPopupGetImage()
        }

        tvSave.onClickDelayAction {
            vm.getUserBody().apply { Log.d("--=", "+${this}") }
            vm.editProfile().observeOnUiThread().subscribe({

            }, {
                activity?.showErrorDialog(it)
            })
        }
    }

    private fun getUserInfo() {
        vm.getUserInfo()
            .observeOnUiThread()
            .subscribe({
                showOldData()
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }

    private fun showOldData() {
        vm.getUserDetail()?.apply {
            Glide.with(imgAvatar).load(imageUrl).into(imgAvatar)
            inputFirstName.setText(firstName)
            inputLastName.setText(lastName)
            inputEmail.setText(email)
            inputPhone.setText(phoneNumber)
            inputGender.selectPosition(gender.name)
            inputBirthday.tvContent.text = birthday.convert(FORMAT_DATE, FORMAT_DATE_API)
            estimationDateTime = birthday.toCalendar(FORMAT_DATE_API)
        }
    }

    private fun validateData(): Boolean {
        return inputFirstName.isValidateDataNotEmpty() &&
                inputLastName.isValidateDataNotEmpty() &&
                inputPhone.isValidateDataNotEmpty() &&
                inputEmail.isValidateDataNotEmpty() &&
                inputGender.isValid()
    }

    private fun updateNextButton() {
        if (validateData()) {
            tvSave.isEnabled = true
            tvSave.alpha = 1f
        } else {
            tvSave.isEnabled = false
            tvSave.alpha = 0.4f
        }
    }

    override fun getProgressObservable() = vm.getProgressObservable()

    private fun handleShowDatePicker(tvDate: TextView, datePicker: CustomDatePickerDialog?) {
        val dateSelected =
            if (tvDate.text.isNullOrEmpty()) estimationDateTime
            else tvDate.text.toString().toCalendar(FORMAT_DATE)
        datePicker?.let {
            it.updateDate(
                dateSelected.get(Calendar.YEAR),
                dateSelected.get(Calendar.MONTH),
                dateSelected.get(Calendar.DAY_OF_MONTH)
            )
            it.show()
        }
    }

    private fun initDatePicker() {
        context?.let { context ->
            datePickerDialogEstimationDate = CustomDatePickerDialog(
                context,
                inputBirthday.tvTimeLabel.text.toString(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    estimationDateTime = GregorianCalendar(year, month, dayOfMonth)
                    inputBirthday.tvContent.text = estimationDateTime.format(FORMAT_DATE)
                    vm.getUserBody().birthday =
                        estimationDateTime.format(FORMAT_DATE_API)
                },
                estimationDateTime.get(Calendar.YEAR),
                estimationDateTime.get(Calendar.MONTH),
                estimationDateTime.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialogEstimationDate?.setCancelable(false)
            val currentDate = Calendar.getInstance()
            datePickerDialogEstimationDate?.setMaxDate(
                currentDate[Calendar.YEAR],
                currentDate[Calendar.MONTH],
                currentDate[Calendar.DAY_OF_MONTH]
            )
            datePickerDialogEstimationDate?.setMinDate(
                1900,
                0,
                0
            )
        }
    }
}
