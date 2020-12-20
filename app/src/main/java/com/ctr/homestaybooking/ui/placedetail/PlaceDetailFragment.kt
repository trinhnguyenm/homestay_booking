package com.ctr.homestaybooking.ui.placedetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ctr.homestaybooking.R
import com.ctr.homestaybooking.base.BaseFragment
import com.ctr.homestaybooking.data.model.BookingType
import com.ctr.homestaybooking.data.model.Favorite
import com.ctr.homestaybooking.data.model.ImageSlideData
import com.ctr.homestaybooking.data.source.FavoriteRepository
import com.ctr.homestaybooking.data.source.PlaceRepository
import com.ctr.homestaybooking.extension.*
import com.ctr.homestaybooking.ui.App
import com.ctr.homestaybooking.util.DateUtil
import com.ctr.homestaybooking.util.convert
import kotlinx.android.synthetic.main.fragment_place_detail.*
import kotlinx.android.synthetic.main.layout_item_review.*
import kotlinx.android.synthetic.main.layout_place_amenity.*
import kotlinx.android.synthetic.main.layout_place_detail.*
import kotlinx.android.synthetic.main.layout_place_review.*
import kotlinx.android.synthetic.main.layout_place_thumbnail.*
import sdk.chat.core.session.ChatSDK
import sdk.guru.common.RX


/**
 * Created by at-trinhnguyen2 on 2020/06/11
 */
class PlaceDetailFragment : BaseFragment() {
    private lateinit var viewModel: PlaceDetailVMContract
    private var favorites: List<Favorite>? = null

    companion object {
        fun newInstance() = PlaceDetailFragment()

        internal const val KEY_PLACE_ID = "key_place_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = PlaceDetailViewModel(PlaceRepository(), FavoriteRepository())
        subscribeFavoritesInDatabase()
        initRecyclerView()
        initView()
        initListener()
    }

    private fun initRecyclerView() {
        recyclerViewPromos.let {
            it.setHasFixedSize(true)
            it.adapter = PromoAdapter(viewModel.getPromos())
        }
        recyclerViewAmenities.let {
            it.setHasFixedSize(true)
            it.adapter = AmenityAdapter(viewModel.getAmenities())
        }
    }

    private fun subscribeFavoritesInDatabase() {
        viewModel.let { vm ->
            vm.getFavorites().observeOnUiThread()
                .subscribe {
                    favorites = it
                    vm.getPlaceDetail()?.apply {
                        btnLike.isChecked = it.map { item -> item.id }.contains(id)
                    }
                }
        }
    }

    override fun isNeedPaddingTop() = true

    private fun initView() {
        activity?.intent?.getIntExtra(KEY_PLACE_ID, 0)?.let { placeId ->
            viewModel.getPlaceDetail(placeId).observeOnUiThread()
                .doOnSubscribe { lnLoadingHeaderDetail.visible() }
                .doFinally { lnLoadingHeaderDetail.gone() }
                .subscribe({ placeDetailResponse ->
                    placeDetailResponse.placeDetail.let { placeDetail ->
                        favorites?.let {
                            btnLike.isChecked = it.map { item -> item.id }.contains(placeDetail.id)
                        }
                        if (App.instance.localRepository.getUserId() == placeDetail.hostDetail?.id) {
                            tvBooking.gone()
                            tvHostName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        }
                        (activity as? PlaceDetailActivity)?.placeDetailResponse =
                            placeDetailResponse
                        (activity as? PlaceDetailActivity)?.bookingSlots = placeDetail.bookingSlots
                        placeDetail.images?.let {
                            context?.let { context ->
                                Glide.with(context).load(it[0]).into(imgBanner1)
                                Glide.with(context).load(it[1]).into(imgBanner2)
                                Glide.with(context).load(it[2]).into(imgBanner3)
                                Glide.with(context).load(it[3]).into(imgBanner4)
                                if (it.size > 4) {
                                    tvMoreCountImage.visible()
                                    tvMoreCountImage.text = "${it.size - 3}+"
                                }
                                Glide.with(context).load(placeDetail.hostDetail?.imageUrl)
                                    .into(imgHost)
                                Glide.with(context)
                                    .load(placeDetail.reviews?.firstOrNull()?.userImage)
                                    .into(imgReviewUser)
                            }
                        }

                        tvName.text = placeDetail.name
                        tvPlaceAddress.text = placeDetail.address
                        tvPlaceType.text = placeDetail.placeType?.name
                        tvHostName.text = "chủ nhà ${placeDetail.hostDetail?.firstName}"
                        tvPlaceRoom.text = placeDetail.getRooms()
                        tvDescription.text = placeDetail.description
                        recyclerViewPromos.adapter?.notifyDataSetChanged()
                        recyclerViewAmenities.adapter?.notifyDataSetChanged()
                        ratingBar.rating = placeDetail.rateAverage.toFloat()
                        tvPlacePrice.text = placeDetail.pricePerDay.toMoney()
                        tvBooking.text =
                            if (placeDetail.bookingType == BookingType.INSTANT_BOOKING) "Đặt ngay" else "Gửi yêu cầu"

                        if (placeDetail.rateCount == 0) {
                            imgRatingStar.gone()
                            tvRateAverage.gone()
                            tvRateCount.gone()
                            clReview.gone()
                        } else {
                            imgRatingStar.visible()
                            tvRateAverage.visible()
                            tvRateCount.visible()
                            clReview.visible()
                            tvRateAverage.text = placeDetail.rateAverage.format(2)
                            tvReviewRateAverage.text = placeDetail.rateAverage.format(2)
                            tvRateCount.text = "(${placeDetail.rateCount})"
                            tvReviewRateCount.text = "(${placeDetail.rateCount} đánh giá)"
                            placeDetail.reviews?.firstOrNull()?.let {
                                tvReviewUsername.text = it.userName
                                tvReviewComment.text = it.comment
                                tvTime.text = it.createDate.convert(DateUtil.FORMAT_DATE)
                            }
                        }
                    }
                }, {
                    activity?.showErrorDialog(it)
                })
        }
    }

    private fun initListener() {
        imgBack.onClickDelayAction {
            activity?.onBackPressed()
        }
        tvBooking.onClickDelayAction {
            (activity as? PlaceDetailActivity)?.openCalendarFragment()
        }

        imgBanner1.onClickDelayAction {
            viewModel.getPlaceDetail()?.images?.let {
                (activity as? PlaceDetailActivity)?.openImageSliderFragment(ImageSlideData(it, 0))
            }
        }
        imgBanner2.onClickDelayAction {
            viewModel.getPlaceDetail()?.images?.let {
                (activity as? PlaceDetailActivity)?.openImageSliderFragment(ImageSlideData(it, 1))
            }
        }
        imgBanner3.onClickDelayAction {
            viewModel.getPlaceDetail()?.images?.let {
                (activity as? PlaceDetailActivity)?.openImageSliderFragment(ImageSlideData(it, 2))
            }
        }
        imgBanner4.onClickDelayAction {
            viewModel.getPlaceDetail()?.images?.let {
                (activity as? PlaceDetailActivity)?.openImageSliderFragment(ImageSlideData(it, 3))
            }
        }

        btnLike.setOnCheckedChangeListener { _, isChecked ->
            viewModel.getPlaceDetail()?.let {
                if (isChecked) {
                    viewModel.insert(listOf(Favorite(it.id, it.toPlace().toJsonString())))
                        .observeOnUiThread().subscribe()
                } else {
                    viewModel.deleteAll(listOf(Favorite(it.id, it.toPlace().toJsonString())))
                        .observeOnUiThread().subscribe()
                }
            }
        }

        tvHostName.onClickDelayAction {
            viewModel.getPlaceDetail()?.hostDetail?.uuid?.let { uuid ->
                ChatSDK.core().getUserForEntityID(uuid).observeOn(RX.main()).subscribe({ host ->
                    Log.d("--=", "host: ${host}")
                    ChatSDK.thread().createThread(listOf(host)).observeOn(RX.main())
                        .subscribe({ thread ->
                            Log.d("--=", "thread: ${thread}")
                            ChatSDK.ui().startChatActivityForID(context, thread.entityID)
                        }, {
                            activity?.showErrorDialog(it)
                        })
                }, {
                    activity?.showErrorDialog(it)
                })
            }

        }
    }

    override fun getProgressObservable() = viewModel.getProgressObservable()
}
