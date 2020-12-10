package com.ctr.homestaybooking.ui.wedget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R


/**
 * Created by at-trinhnguyen2 on 2020/12/06
 */
class ImageSliderAdapter(private val context: Context, private val images: List<String>) :
    PagerAdapter() {
    override fun getCount() = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_image_slider, container, false)
        Glide.with(context).load(images[position]).into(
            view.findViewById(R.id.imageSlider)
        )
        container.addView(view)
        return view
    }
}
