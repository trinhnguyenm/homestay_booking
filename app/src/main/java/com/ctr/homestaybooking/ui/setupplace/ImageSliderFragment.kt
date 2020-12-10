package com.ctr.homestaybooking.ui.setupplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.ImageSlideData
import com.ctr.homestaybooking.extension.onClickDelayAction
import com.ctr.homestaybooking.ui.wedget.ImageSliderAdapter
import kotlinx.android.synthetic.main.fragment_image_slider.*

/**
 * Created by at-trinhnguyen2 on 2020/12/06
 */

class ImageSliderFragment : BaseFragment() {

    companion object {
        fun newInstance(imageSlideData: ImageSlideData) = ImageSliderFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_IMAGE_SLIDE_DATA, imageSlideData)
            }
        }

        internal const val KEY_IMAGE_SLIDE_DATA = "key_image_slide_data"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<ImageSlideData>(KEY_IMAGE_SLIDE_DATA)?.let {
            tvPage.text = "${it.currentPosition + 1}/${it.images.size}"
            viewPager.adapter = context?.let { it1 -> ImageSliderAdapter(it1, it.images) }
            viewPager.currentItem = it.currentPosition
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    tvPage.text = "${position + 1}/${it.images.size}"
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
        }

        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }
    }

    override fun isNeedPaddingTop() = false

}
