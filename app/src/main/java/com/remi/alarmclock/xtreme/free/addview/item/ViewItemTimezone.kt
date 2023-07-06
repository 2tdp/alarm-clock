package com.remi.alarmclock.xtreme.free.addview.item

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.textCustom

@SuppressLint("ResourceType")
class ViewItemTimezone(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0f
    }

    val tvName: TextView
    val tvTime: TextView
    val ivTick: ImageView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        tvName = TextView(context).apply {
            textCustom(
                "", Color.WHITE, 4.44f * w, "display_regular", context
            )
        }
        addView(tvName, LayoutParams(-2, -2).apply {
            leftMargin = (5.556f * w).toInt()
            addRule(CENTER_VERTICAL, TRUE)
        })

        tvTime = TextView(context).apply {
            id = 1221
            textCustom(
                "", Color.WHITE, 4.44f * w, "display_regular", context
            )
        }
        addView(tvTime, LayoutParams(-2, -2).apply {
            rightMargin = (5.556f * w).toInt()
            addRule(CENTER_VERTICAL, TRUE)
            addRule(ALIGN_PARENT_END, TRUE)
        })

        ivTick = ImageView(context).apply {
            visibility = GONE
            setImageResource(R.drawable.ic_tick)
        }
        addView(ivTick, LayoutParams((5.556f * w).toInt(), (5.556f * w).toInt()).apply {
            addRule(LEFT_OF, tvTime.id)
            addRule(CENTER_VERTICAL, TRUE)
            rightMargin = (3.33f * w).toInt()
        })
    }
}