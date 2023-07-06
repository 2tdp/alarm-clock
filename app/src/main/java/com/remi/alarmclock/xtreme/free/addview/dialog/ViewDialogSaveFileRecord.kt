package com.remi.alarmclock.xtreme.free.addview.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom

@SuppressLint("ResourceType")
class ViewDialogSaveFileRecord(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val edtNameFile: EditText

    val tvSave: TextView
    val tvCancel: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundResource(R.drawable.boder_dialog)

        val tvTitle = TextView(context).apply {
            id = 1221
            textCustom(
                resources.getString(R.string.save_file),
                Color.BLACK, 5f * w, "display_bold", context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (4.44f * w).toInt()
        })

        edtNameFile = EditText(context).apply {
            id = 1222
            setText(R.string.record_file)
            textCustom(
                "", Color.GRAY, Color.BLACK, 4.44f * w, "display_regular", context
            )
        }
        addView(edtNameFile, LayoutParams(-1, -2).apply {
            rightMargin = (4.44f * w).toInt()
            leftMargin = (4.44f * w).toInt()
            addRule(BELOW, tvTitle.id)
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
        tvSave = TextView(context).apply {
            id = 1334
            textCustom(
                resources.getString(R.string.save),
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
            topMargin = (5.556f * w).toInt()
            addRule(BELOW, edtNameFile.id)
        })
        addView(tvSave, LayoutParams((30.556f * w).toInt(), (15.278f * w).toInt()).apply {
            rightMargin = (5f * w).toInt()
            leftMargin = (3.33f * w).toInt()
            topMargin = (5.556f * w).toInt()
            addRule(BELOW, edtNameFile.id)
            addRule(ALIGN_PARENT_END, TRUE)
        })
    }
}