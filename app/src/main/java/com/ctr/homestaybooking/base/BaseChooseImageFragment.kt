package com.ctr.homestaybooking.base

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.core.text.HtmlCompat
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.data.source.LocalRepository
import com.ctr.homestaybooking.extension.showDialog
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.util.SharedReferencesUtil
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File

/**
 * @author at-dinhtruong
 */
abstract class BaseChooseImageFragment : BaseFragment() {

    companion object {
        private const val FILE_PROVIDER = "com.ctr.homestaybooking.fileprovider"
        private const val PICK_IMAGE_REQUEST_CODE = 1357
        private const val IMAGE_CAPTURE_REQUEST_CODE = 2468
        internal const val CAMERA_PERMISSION = 1238
        internal const val GALLERY_PERMISSION = 1239
        internal const val REQUEST_CODE_SETTING = 2233
    }

    private lateinit var viewModel: BaseUploadVMContract
    private var pathDelete: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = BaseUploadViewModel(LocalRepository(view.context))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                openCropActivity(uri)
            }
        }

        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val file = File(viewModel.getCurrentPhotoPath())
            if (file.length() > 0) {
                val uri = Uri.fromFile(file)
                openCropActivity(uri)
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            pathDelete = this.handleSaveImageCrop(data, resultCode)
            getPathImageSuccess(pathDelete)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        context?.let { context ->
            if (grantResults.isNotEmpty()) {
                permissions.forEach {
                    if (checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED) {
                        when (requestCode) {
                            CAMERA_PERMISSION -> {
                                viewModel.savePermission(
                                    SharedReferencesUtil.KEY_PERMISSION_CAMERA,
                                    true
                                )
                                handleGetImageCamera()
                            }
                            GALLERY_PERMISSION -> {
                                viewModel.savePermission(
                                    SharedReferencesUtil.KEY_PERMISSION_GALLERY,
                                    true
                                )
                                handleGetImageGallery()
                            }
                        }
                    } else {
                        when (requestCode) {
                            CAMERA_PERMISSION -> {
                                viewModel.savePermission(
                                    SharedReferencesUtil.KEY_PERMISSION_CAMERA,
                                    !shouldShowRequestPermissionRationale(it)
                                )
                            }
                            GALLERY_PERMISSION -> {
                                viewModel.savePermission(
                                    SharedReferencesUtil.KEY_PERMISSION_GALLERY,
                                    !shouldShowRequestPermissionRationale(it)
                                )
                            }
                        }
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        if (viewModel.getCurrentPhotoPath().isNotEmpty()) {
            viewModel.deleteFile(viewModel.getCurrentPhotoPath())
        }
        viewModel.deleteFile(pathDelete)
        super.onDestroy()
    }

    abstract fun getImageView(): ImageView

    abstract fun getPathImageSuccess(path: String)

    internal fun showPopupGetImage() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(
            HtmlCompat.fromHtml(
                "Choose image",
                0
            )
        )

        builder.setItems(arrayOf("CAMERA", "GALLERY")) { _, which ->
            when (which) {
                0 -> {
                    checkPermissionCamera()
                }
                else -> {
                    checkPermissionGallery()
                }
            }
        }
        builder.setPositiveButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog?.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.apply {
                setTextColor(ContextCompat.getColor(context, R.color.colorBrightBlue))
            }
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun checkPermissionCamera() {
        activity?.let {
            if (checkSelfPermission(it, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
                    } else {
                        if (viewModel.getPermission(SharedReferencesUtil.KEY_PERMISSION_CAMERA)) {
                            handleShowPopupMoveToSetting()
                        } else {
                            viewModel.savePermission(
                                SharedReferencesUtil.KEY_PERMISSION_CAMERA,
                                true
                            )
                            requestPermissions(
                                arrayOf(Manifest.permission.CAMERA),
                                CAMERA_PERMISSION
                            )
                        }
                    }
                }
            } else {
                handleGetImageCamera()
            }
        }
    }

    private fun handleGetImageCamera() {
        activity?.let {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(it.packageManager)?.also {
                    if (viewModel.getCurrentPhotoPath().isNotEmpty()) {
                        viewModel.deleteFile(viewModel.getCurrentPhotoPath())
                    }
                    val photoFile: File? = viewModel.createImageFile()
                    photoFile?.also { file ->
                        val photoURI: Uri = FileProvider.getUriForFile(
                            App.instance.applicationContext,
                            FILE_PROVIDER,
                            file
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        App.instance.isIgnoreAppGoToBackground = true
                        startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE)
                    }
                }
            }
        }
    }

    private fun checkPermissionGallery() {
        activity?.let {
            if (checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            GALLERY_PERMISSION
                        )
                    } else {
                        if (viewModel.getPermission(SharedReferencesUtil.KEY_PERMISSION_GALLERY)) {
                            handleShowPopupMoveToSetting()
                        } else {
                            viewModel.savePermission(
                                SharedReferencesUtil.KEY_PERMISSION_GALLERY,
                                true
                            )
                            requestPermissions(
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                GALLERY_PERMISSION
                            )
                        }
                    }
                }
            } else {
                handleGetImageGallery()
            }
        }
    }

    private fun handleGetImageGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        App.instance.isIgnoreAppGoToBackground = true
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun handleShowPopupMoveToSetting() {
        activity?.showDialog("Setting", "App need permission to use camera and gallery", "ok", {
            val packageName: String = activity?.packageName ?: ""
            App.instance.isIgnoreAppGoToBackground = true
            startActivityForResult(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:$packageName")
                ),
                REQUEST_CODE_SETTING
            )
        }, "cancel")
    }

    private fun openCropActivity(uri: Uri) {
        context?.let {
            val intent = CropImage.activity(uri)
                .setFixAspectRatio(true)
                .setAspectRatio(getImageView().width, getImageView().height)
                .setAllowRotation(false)
                .setAllowFlipping(false)
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(it, CropImageActivity::class.java)
            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }

    private fun handleSaveImageCrop(data: Intent?, resultCode: Int): String {
        val result = CropImage.getActivityResult(data)
        if (resultCode == Activity.RESULT_OK) {
            val pathImage = viewModel.handleSaveData(result.uri)
            val file = File(pathImage)
            if (file.exists()) {
                val myBitmap = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(file.absolutePath),
                    getImageView().width,
                    getImageView().height
                )
                getImageView().setImageBitmap(myBitmap)
            }
            return pathImage
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            //Todo implement late
        }
        return ""
    }
}
