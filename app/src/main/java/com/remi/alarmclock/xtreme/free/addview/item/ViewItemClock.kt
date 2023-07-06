package com.remi.alarmclock.xtreme.free.addview.item

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.ntduc.swipereveallayout.SwipeRevealLayout
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom

class ViewItemClock(context: Context): SwipeRevealLayout(context) {

    companion object {
        var w = 0F
    }

    val tvName: TextView
    val tvTime: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        dragEdge = DRAG_EDGE_RIGHT

        val rlDel = RelativeLayout(context).apply {
            createBackground(
                intArrayOf(Color.parseColor("#FFB9B9")),
                (2.5f * w).toInt(), -1, -1
            )
        }
        val ivDel = ImageView(context).apply {
            setImageResource(R.drawable.ic_del)
            setColorFilter(Color.parseColor("#FF2424"))
        }
        rlDel.addView(ivDel, RelativeLayout.LayoutParams((6.667f * w).toInt(), (6.667f * w).toInt()).apply {
            addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
        })
        addView(rlDel, LayoutParams(-1, -1))

        val rl = RelativeLayout(context).apply {
            createBackground(
                intArrayOf(Color.parseColor("#202020")),
                (2.5f * w).toInt(), -1, -1
            )
        }
        tvName = TextView(context).apply {
            textCustom(
                "", Color.WHITE, 5f * w, "display_regular", context
            )
        }
        rl.addView(tvName, RelativeLayout.LayoutParams(-2, -2).apply {
            leftMargin = (5.556f * w).toInt()
            addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
        })

        tvTime = TextView(context).apply {
            textCustom(
                "", Color.WHITE, 8.889f * w, "display_regular", context
            )
        }
        rl.addView(tvTime, RelativeLayout.LayoutParams(-2, -2).apply {
            rightMargin = (5.556f * w).toInt()
            addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
        })
        addView(rl, LayoutParams(-1, -1))
    }
}