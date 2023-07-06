package com.remi.alarmclock.xtreme.free.addview.addalarm

import android.content.Context
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView

class ViewAudioUnknown(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val rcvRingtones: RecyclerView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        isClickable = true
        isFocusable = true

        rcvRingtones = RecyclerView(context).apply { isVerticalScrollBarEnabled = false }
        addView(rcvRingtones, -1, -1)
    }
}