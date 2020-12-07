package com.ctr.homestaybooking.ui.setupplace

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.ImageSlideData
import com.ctr.homestaybooking.extension.*
import kotlinx.android.synthetic.main.fragment_place_setup_basic_info.ivBack
import kotlinx.android.synthetic.main.fragment_place_setup_basic_info.tvSave
import kotlinx.android.synthetic.main.fragment_place_setup_image.*


/**
 * Created by at-trinhnguyen2 on 2020/12/05
 */
class PlaceSetupImageFragment : BaseFragment() {
    private lateinit var vm: PlaceSetupVMContract
    private var images = mutableListOf<String>()
    private var imagesPicked = mutableListOf<Uri>()

    companion object {
        fun newInstance() = PlaceSetupImageFragment()

        internal const val PICK_IMAGE_MULTIPLE = 4324
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_setup_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? PlaceSetupActivity)?.vm?.let {
            vm = it
        }
        initRecyclerView()
        initListener()
    }

    override fun getProgressObservable() =
        (activity as? PlaceSetupActivity)?.vm?.getProgressObservable()

    override fun isNeedPaddingTop() = true

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
                imagesPicked.clear()
                val uri: Uri? = data.data
                activity?.let { activity ->
                    if (uri != null) {
                        imagesPicked.add(uri)
                    } else {
                        data.clipData?.let { clipData ->
                            for (i in 0 until clipData.itemCount) {
                                imagesPicked.add(clipData.getItemAt(i).uri)
                            }
                        }
                    }
                    context?.uploadImageFirebase(imagesPicked) { task ->
                        if (task.isSuccessful) {
                            images.add(task.result.toString())
                            recyclerViewImages.adapter?.notifyDataSetChanged()
                        } else {
                            task.exception?.let {
                                activity.showErrorDialog(it)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    private fun initRecyclerView() {
        recyclerViewImages.let {
            it.setHasFixedSize(true)
            vm.getPlaceBody().images?.let { it1 ->
                images.clear()
                images.addAll(it1)
            }
            it.adapter = PlaceSetupImageAdapter(images).also { adapter ->
                adapter.onItemDeleteClicked = this::onItemDeleteClicked
                adapter.onItemClicked = this::handlerItemClick
            }
        }
    }

    private fun onItemDeleteClicked(position: Int) {
        images.removeAt(position)
        recyclerViewImages.adapter?.notifyItemRemoved(position)
    }

    private fun handlerItemClick(position: Int) {
        (activity as? PlaceSetupActivity)?.openImageSliderFragment(ImageSlideData(images, position))
    }

    private fun initListener() {
        ivBack.onClickDelayAction {
            activity?.onBackPressed()
        }

        tvAddImage.onClickDelayAction {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_MULTIPLE
            )
        }

        tvSave.onClickDelayAction {
            if (images.size < 4) {
                activity?.showSnackbar(container, getString(R.string.image_size_min_error))
            } else {
                vm.getPlaceBody().images = images
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
        }
    }
}
