package com.remi.alarmclock.xtreme.free.addview.addtimezone

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment

class ItemFragClock: BaseFragment() {

    companion object {
        var w = 0f
        fun newInstance(): ItemFragClock {
            val args = Bundle()

            val fragment = ItemFragClock()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewLayout(): View {
        w = resources.displayMetrics.widthPixels / 100f
        val rl = RelativeLayout(requireContext())
        rl.addView(ViewClock(requireContext()), RelativeLayout.LayoutParams((56.389f * w).toInt(), (56.389f * w).toInt()).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        })
        return rl
    }

    override fun setUp() {
    }
}