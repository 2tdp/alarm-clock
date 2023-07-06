package com.remi.alarmclock.xtreme.free.addview.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom

@SuppressLint("ResourceType")
class ViewDialogAudioPermission(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val tvTitle: TextView
    val tvDes: TextView
    val tvCancel: TextView
    val tvAllow: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundResource(R.drawable.boder_dialog)

        tvTitle = TextView(context).apply {
            id = 1221
            textCustom(
                resources.getString(R.string.audio),
                ContextCompat.getColor(context, R.color.black),
                5f * w, "display_bold", context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (5f * w).toInt()
        })

        tvDes = TextView(context).apply {
            id = 1222
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.black),
                3.889f * w, "display_regular", context
            )
             text = SpannableString(resources.getString(R.string.des_per_alarm)).apply {
                 setSpan(StyleSpan(Typeface.BOLD), 5, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
             }
        }
        addView(tvDes, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, tvTitle.id)
            topMargin = (2.22f * w).toInt()
        })

        tvCancel = TextView(context).apply {
            id = 1333
            textCustom(
                resources.getString(R.string.cancel),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w, "display_bold", context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        tvAllow = TextView(context).apply {
            id = 1334
            textCustom(
                resources.getString(R.string.allow),
                ContextCompat.getColor(context, R.color.black_background),
                4.44f * w, "display_bold", context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        addView(tvCancel, LayoutParams((30.556f * w).toInt(), (15.556f * w).toInt()).apply {
            leftMargin = (5f * w).toInt()
            rightMargin = (3.33f * w).toInt()
            topMargin = (8.33f * w).toInt()
            addRule(BELOW, tvDes.id)
        })
        addView(tvAllow, LayoutParams((30.556f * w).toInt(), (15.556f * w).toInt()).apply {
            rightMargin = (5f * w).toInt()
            leftMargin = (3.33f * w).toInt()
            topMargin = (8.33f * w).toInt()
            addRule(BELOW, tvDes.id)
            addRule(ALIGN_PARENT_END, TRUE)
        })
    }
}