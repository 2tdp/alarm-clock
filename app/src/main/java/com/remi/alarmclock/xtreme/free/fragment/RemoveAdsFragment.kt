package com.remi.alarmclock.xtreme.free.fragment

import android.os.Bundle
import android.view.View
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import com.remi.alarmclock.xtreme.free.addview.ViewRemoveAds

class RemoveAdsFragment: BaseFragment() {

    private lateinit var vRemoveAds: ViewRemoveAds

    companion object{
        fun newInstance(): RemoveAdsFragment {
            val args = Bundle()

            val fragment = RemoveAdsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewLayout(): View {
        vRemoveAds = ViewRemoveAds(requireContext())
        return vRemoveAds
    }

    override fun setUp() {
        vRemoveAds.vLifetime.setOnClickListener { vRemoveAds.clickOption(0) }
        vRemoveAds.vMonth.setOnClickListener { vRemoveAds.clickOption(1) }
        vRemoveAds.vYear.setOnClickListener { vRemoveAds.clickOption(2) }

        vRemoveAds.ivBack.setOnClickListener { popBackStack("RemoveAdsFragment") }
        vRemoveAds.tvRemoveAds.setOnClickListener {  }
    }
}