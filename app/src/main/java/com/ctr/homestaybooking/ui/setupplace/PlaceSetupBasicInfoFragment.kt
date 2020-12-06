package com.ctr.homestaybooking.ui.setupplace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.BookingType
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.wedget.SpinnerType
import kotlinx.android.synthetic.main.fragment_place_setup_basic_info.*
import kotlinx.android.synthetic.main.input_group_layout.view.*
import kotlinx.android.synthetic.main.layout_place_setup_info.*
import kotlinx.android.synthetic.main.layout_place_setup_introduction.*
import kotlinx.android.synthetic.main.layout_place_setup_location.*

/**
 * Created by at-trinhnguyen2 on 2020/12/03
 */

class PlaceSetupBasicInfoFragment : BaseFragment() {
    private lateinit var vm: PlaceSetupVMContract

    companion object {
        fun newInstance() = ImageSliderFragment().apply {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_setup_basic_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? PlaceSetupActivity)?.vm?.let {
            vm = it
        }
        getPlaceTypes()
        initView()
        initListener()
        initData()
    }

    override fun getProgressObservable() =
        (activity as? PlaceSetupActivity)?.vm?.getProgressObservable()

    override fun isNeedPaddingTop() = true

    private fun initView() {
        inputAddress.inputField.isEnabled = false

        spinnerBookingType.initSpinner(SpinnerType.BOOKING_TYPE)

        inputName.validateData = {
            it.isNotBlank() && it.length <= 255
        }

        inputDescription.validateData = {
            it.isNotBlank() && it.length <= 5000
        }

        inputSize.validateData = {
            it.isNotBlank() && it.toIntOrNull() != 0
        }

        inputStreet.validateData = {
            it.isNotBlank() && it.length <= 255
        }

        inputAddress.validateData = { true }
    }

    private fun initListener() {
        ivBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvSave.onClickDelayAction {
            Log.d("--=", "initListener: ${vm.getPlaceBody()}")
            vm.editPlace().observeOnUiThread().subscribe({
                activity?.showDialog(
                    getString(R.string.success_toast),
                    null,
                    getString(R.string.ok),
                    {
                        activity?.onBackPressed()
                    })
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
        }

        inputName.afterTextChange = {
            vm.getPlaceBody().name = it
            handleNextButton()
        }

        inputDescription.afterTextChange = {
            vm.getPlaceBody().description = it
            handleNextButton()
        }

        inputSize.afterTextChange = {
            vm.getPlaceBody().size = it.toIntOrNull()
            handleNextButton()
        }

        inputStreet.afterTextChange = {
            vm.getPlaceBody().street = it
            updateAddress()
            handleNextButton()
        }

        spinnerPlaceType.onItemSelectedListener = {
            vm.getPlaceBody().placeTypeId = it?.getCode()?.toIntOrNull()
            handleNextButton()
        }
        spinnerBookingType.onItemSelectedListener = {
            vm.getPlaceBody().bookingType =
                if (it?.getCode() == BookingType.INSTANT_BOOKING.name) BookingType.INSTANT_BOOKING else BookingType.REQUEST_BOOKING
            handleNextButton()
        }
        spinnerProvince.onItemSelectedListener = {
            it?.getCode()?.toIntOrNull()?.let { it1 -> getProvinceById(it1) }
            handleNextButton()
        }
        spinnerDistrict.onItemSelectedListener = { district ->
            vm.getProvinceDetail()?.districts?.find { it.id == district?.getCode()?.toIntOrNull() }
                ?.apply {
                    wards?.map { SpinnerType(it.getName(), it.id.toString()) }
                        ?.let {
                            spinnerWard?.initSpinner(
                                it,
                                vm.getPlaceDetail()?.wardDetail?.id.toString()
                            )
                        }
                }
            handleNextButton()
        }
        spinnerWard.onItemSelectedListener = {
            vm.getPlaceBody().wardId = it?.getCode()?.toIntOrNull()
            updateAddress()
            handleNextButton()
        }

        pickerGuestNo.onValueChange = {
            vm.getPlaceBody().guestCount = it
            handleNextButton()
        }
        pickerRoomNo.onValueChange = {
            vm.getPlaceBody().roomCount = it
            handleNextButton()
        }
        pickerBedRoomNo.onValueChange = {
            vm.getPlaceBody().bedCount = it
            handleNextButton()
        }
        pickerBathRoomNo.onValueChange = {
            vm.getPlaceBody().bathroomCount = it
            handleNextButton()
        }
    }

    private fun initData() {
        if (vm.getPlaceDetail().apply { Log.d("--=", "getPlaceDetail+${this}") } == null) {
            vm.getPlaceBody().guestCount = 0
            vm.getPlaceBody().roomCount = 0
            vm.getPlaceBody().bedCount = 0
            vm.getPlaceBody().bathroomCount = 0
        } else {
            showOldData()
        }
    }

    private fun updateAddress() {
        if (inputStreet.getText().isNotBlank()) {
            inputAddress.setText(
                "${inputStreet.getText()}, ${spinnerWard?.getSelectedItem()?.getText()}, ${
                    spinnerDistrict.getSelectedItem()?.getText()
                }, ${spinnerProvince.getSelectedItem()?.getText()}"
            )
        } else {
            inputAddress.setText("")
        }
    }

    private fun getPlaceTypes() {
        vm.getPlaceTypes().observeOnUiThread()
            .subscribe({ response ->
                response.placeTypes.map {
                    SpinnerType(it.name, it.id.toString())
                }.let {
                    spinnerPlaceType?.initSpinner(
                        it,
                        vm.getPlaceDetail()?.placeType?.id?.toString()
                    )
                }
                getProvinces()
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }


    private fun getProvinces() {
        vm.getProvinces().observeOnUiThread()
            .subscribe({ response ->
                response.provinces.map { SpinnerType(it.getName(), it.id.toString()) }.let {
                    spinnerProvince?.initSpinner(
                        it,
                        vm.getPlaceDetail()?.wardDetail?.districtDetail?.province?.id.toString()
                    )
                }
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }

    private fun getProvinceById(id: Int) {
        vm.getProvinceById(id).observeOnUiThread()
            .subscribe({ response ->
                response.provinceDetail.districts?.map {
                    SpinnerType(it.getName(), it.id.toString())
                }?.let {
                    spinnerDistrict?.initSpinner(
                        it,
                        vm.getPlaceDetail()?.wardDetail?.districtDetail?.id.toString()
                    )
                }
            }, {
                activity?.showErrorDialog(it)
            }).addDisposable()
    }

    private fun showOldData() {
        vm.getPlaceDetail()?.apply {
            name?.let {
                inputName.setText(it)
            }
            description?.let {
                inputDescription.setText(it)
            }
            bookingType?.let {
                spinnerBookingType?.selectPosition(it.name)
            }
            street?.let {
                inputStreet.setText(it)
            }
            size?.let {
                inputSize.setText(it.toString())
            }
            guestCount?.let {
                pickerGuestNo.setValue(it)
            }
            roomCount?.let {
                pickerRoomNo.setValue(it)
            }
            bedCount?.let {
                pickerBedRoomNo.setValue(it)
            }
            bathroomCount?.let {
                pickerBathRoomNo.setValue(it)
            }
        }
    }

    private fun isAllValid() =
        inputName.isValidateDataNotEmpty() &&
                inputDescription.isValidateDataNotEmpty() &&
                inputSize.isValidateDataNotEmpty() &&
                inputStreet.isValidateDataNotEmpty() &&
                spinnerPlaceType.isValid() &&
                spinnerBookingType.isValid() &&
                spinnerProvince.isValid() &&
                spinnerDistrict.isValid() &&
                spinnerWard.isValid()

    private fun handleNextButton() {
        if (isAllValid()) {
            tvSave.isEnabled = true
            tvSave.textColor(R.color.colorAccent)
        } else {
            tvSave.isEnabled = false
            tvSave.textColor(R.color.greyishBrown)
        }
    }
}
