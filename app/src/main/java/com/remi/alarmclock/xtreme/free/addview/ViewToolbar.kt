package com.remi.alarmclock.xtreme.free.addview

import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.textCustom

class ViewToolbar(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val tvTitle: TextView
    val ivAdd: ImageView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        tvTitle = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.white),
                6.667f * w,
                "display_heavy",
                context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            leftMargin = (5.556f * w).toInt()
            addRule(CENTER_VERTICAL, TRUE)
        })

        ivAdd = ImageView(context).apply {
            setImageResource(R.drawable.ic_add)
        }
        addView(ivAdd, LayoutParams((8.33f * w).toInt(), (8.33f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
            addRule(CENTER_VERTICAL, TRUE)
            rightMargin = (5.556f * w).toInt()
        })
    }
}