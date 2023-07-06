package com.remi.alarmclock.xtreme.free.addview.item

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.textCustom

@SuppressLint("ResourceType")
class ViewItemChoose(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val tvTitle: TextView
    val tvChoose: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        tvTitle = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.gray_text),
                4.44f * w,
                "display_bold",
                context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            addRule(CENTER_VERTICAL, TRUE)
        })
        val ivSnooze = ImageView(context).apply {
            id = 1227
            setImageResource(R.drawable.ic_right)
        }
        addView(ivSnooze, LayoutParams((6.667f * w).toInt(), (6.667f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
            addRule(CENTER_VERTICAL, TRUE)
        })
        tvChoose = TextView(context).apply {
            textCustom(
                resources.getString(R.string.time_snooze),
                ContextCompat.getColor(context, R.color.white),
                3.889f * w,
                "display_regular",
                context
            )
        }
        addView(tvChoose, LayoutParams(-2, -2).apply {
            addRule(CENTER_VERTICAL, TRUE)
            addRule(LEFT_OF, ivSnooze.id)
            rightMargin = (1.11f * w).toInt()
        })
    }
}