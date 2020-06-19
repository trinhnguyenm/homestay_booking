package com.ctr.hotelreservations.ui.home.brands

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ctr.hotelreservations.R
import com.ctr.hotelreservations.base.BaseFragment
import com.ctr.hotelreservations.data.source.response.HotelResponse
import com.ctr.hotelreservations.extension.onClickDelayAction
import kotlinx.android.synthetic.main.fragment_brand.*

/**
 * Created by at-trinhnguyen2 on 2020/05/31
 */
class BrandFragment : BaseFragment() {

    companion object {
        private const val KEY_HOTEL = "key_hotel"

        fun getInstance(hotel: HotelResponse.Body) =
            BrandFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        KEY_HOTEL,
                        hotel
                    )
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initRecyclerView()
    }

    override fun isNeedPaddingTop() = true

    private fun initView() {
        arguments?.getParcelable<HotelResponse.Body>(KEY_HOTEL)?.let {
            tvName.text = it.name
            tvDescription.text = it.description
            context?.let { context ->
                Glide.with(context)
                    .load(it.images.firstOrNull()?.name)
                    .into(ivHotel)
            }
        }
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }
    }

    private fun initRecyclerView() {
        val brandAdapter =
            BrandAdapter(
                arguments?.getParcelable<HotelResponse.Body>(KEY_HOTEL)?.brands
                    ?: HotelResponse.Body(listOf()).brands
            )
        rcvBrand.apply {
            setHasFixedSize(true)
            adapter = brandAdapter
        }
        brandAdapter.onItemClicked = {
            Toast.makeText(context, "Clicked ${it.id}", Toast.LENGTH_SHORT).show()
        }
    }
}
