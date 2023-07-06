package com.remi.alarmclock.xtreme.free.addview.addtimezone

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.format.DateFormat
import android.widget.RelativeLayout
import android.widget.TextView
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import java.util.*

@SuppressLint("ResourceType")
class ViewTime(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0f
    }

    val tvTime: TextView
    val tvDate: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        tvTime = TextView(context).apply {
            id = 1221
            textCustom(
                "", Color.WHITE, 16.667f * w, "display_regular", context
            )
        }
        addView(tvTime, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (11.11f * w).toInt()
        })

        tvDate = TextView(context).apply {
            textCustom(
                "", Color.WHITE, 5.556f * w, "display_regular", context
            )
        }
        addView(tvDate, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            bottomMargin = (13.333f * w).toInt()
        })
    }

    fun setTime() {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        tvTime.text = DateFormat.format("HH:mm", calendar.timeInMillis).toString()
        tvDate.text = DateFormat.format("EEE, MMM dd", calendar.timeInMillis).toString()
    }
}