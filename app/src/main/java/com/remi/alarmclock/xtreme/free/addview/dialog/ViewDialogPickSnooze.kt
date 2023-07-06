package com.remi.alarmclock.xtreme.free.addview.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import com.remi.alarmclock.xtreme.free.utils.Utils
import com.shawnlin.numberpicker.NumberPicker

@SuppressLint("ResourceType")
class ViewDialogPickSnooze(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val numbPicker: NumberPicker
    val tvOk : TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundResource(R.drawable.boder_dialog)

        val tvTitle = TextView(context).apply {
            id = 1221
            textCustom(
                resources.getString(R.string.snooze),
                ContextCompat.getColor(context, R.color.black),
                5f * w, "display_bold", context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (5.556f * w).toInt()
        })

        tvOk = TextView(context).apply {
            id = 1222
            textCustom(
                resources.getString(R.string.ok),
                ContextCompat.getColor(context, R.color.black),
                4.44f * w, "display_bold", context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        addView(tvOk, LayoutParams((41.11f * w).toInt(), (15.556f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            bottomMargin = (6.667f * w).toInt()
        })

        val rl = RelativeLayout(context)
        val v = View(context).apply {
            createBackground(
                intArrayOf(Color.parseColor("#17202020")),
                (2.5f * w).toInt(), -1, -1
            )
        }
        rl.addView(v, LayoutParams(-1, (11.667f * w).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        })
        numbPicker = NumberPicker(context).apply {
            maxValue = 30
            minValue = 0
            value = 10
            selectedTextSize = 4.44f * w
            textSize = 3.889f * w
            typeface = Utils.getTypeFace("display", "display_regular.otf", context)
            dividerColor = Color.TRANSPARENT
            orientation = NumberPicker.VERTICAL
        }
        rl.addView(numbPicker, -1, -1)

        addView(rl, LayoutParams(-1, -1).apply {
            addRule(BELOW, tvTitle.id)
            addRule(ABOVE, tvOk.id)
            topMargin = (3.33f * w).toInt()
            bottomMargin = (3.33f * w).toInt()
            leftMargin = (8.889f * w).toInt()
            rightMargin = (8.889f * w).toInt()
        })
    }
}