package com.remi.alarmclock.xtreme.free.addview.addtimezone

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.RelativeLayout
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import java.util.Timer
import java.util.TimerTask

class Item2FragClock: BaseFragment() {

    private lateinit var vTime : ViewTime

    companion object {
        var w = 0f
        fun newInstance(): Item2FragClock {
            val args = Bundle()

            val fragment = Item2FragClock()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewLayout(): View {
        w = resources.displayMetrics.widthPixels / 100f
        val rl = RelativeLayout(requireContext())
        vTime = ViewTime(requireContext())
        rl.addView(vTime, RelativeLayout.LayoutParams((56.389f * w).toInt(), (56.389f * w).toInt()).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        })
        return rl
    }

    override fun setUp() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post { vTime.setTime() }
            }
        }, 0, 1000)
    }
}