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
class ViewItemRecord(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val ivTick: ImageView
    val ivDel: ImageView
    val tvName: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        ivTick = ImageView(context).apply {
            id = 1221
            setImageResource(R.drawable.ic_un_choose)
        }
        addView(ivTick, LayoutParams((6.667f * w).toInt(), (6.667f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            addRule(CENTER_VERTICAL, TRUE)
        })

        ivDel = ImageView(context).apply {
            id = 1222
            setImageResource(R.drawable.ic_del)
        }
        addView(ivDel, LayoutParams((6.667f * w).toInt(), (6.667f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
            addRule(CENTER_VERTICAL, TRUE)
            rightMargin = (5.556f * w).toInt()
        })

        tvName = TextView(context).apply {
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.white),
                4.44f * w,
                "display_regular",
                context
            )
        }
        addView(tvName, LayoutParams(-1, -2).apply {
            addRule(LEFT_OF, ivDel.id)
            addRule(RIGHT_OF, ivTick.id)
            addRule(CENTER_VERTICAL, TRUE)
            leftMargin = (5.556f * w).toInt()
        })
    }
}