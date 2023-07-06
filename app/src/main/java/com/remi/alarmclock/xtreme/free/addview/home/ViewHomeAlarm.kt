package com.remi.alarmclock.xtreme.free.addview.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.textCustom

@SuppressLint("ResourceType")
class ViewHomeAlarm(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val vToolbar: ViewToolbarAlarm
    val rcvAlarm: RecyclerView

    val rlNoData: RelativeLayout

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_background))

        vToolbar = ViewToolbarAlarm(context).apply { id = 1221 }
        addView(vToolbar, LayoutParams(-1, (17.22f * w).toInt()).apply {
            topMargin = (12.33f * w).toInt()
        })

        rcvAlarm = RecyclerView(context).apply {
            visibility = GONE
            isVerticalScrollBarEnabled = false
        }
        addView(rcvAlarm, LayoutParams(-1, -1).apply {
            addRule(BELOW, vToolbar.id)
        })

        rlNoData = RelativeLayout(context)
        val iv = ImageView(context).apply {
            id = 1667
            setImageResource(R.drawable.img_no_data_alarm)
        }
        rlNoData.addView(iv, LayoutParams(-1, (55.556f * w).toInt()))
        val tv = TextView(context).apply {
            textCustom(
                resources.getString(R.string.no_alarm), Color.parseColor("#A6A6A6"),
                5.556f * w, "display_regular", context
            )
        }
        rlNoData.addView(tv, LayoutParams(-2, -2).apply {
            addRule(BELOW, iv.id)
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (3.33f * w).toInt()
        })
        addView(rlNoData, LayoutParams((55.556f * w).toInt(), -2).apply {
            addRule(BELOW, vToolbar.id)
            topMargin = (15f * w).toInt()
            addRule(CENTER_HORIZONTAL, TRUE)
        })
    }

    inner class ViewToolbarAlarm(context: Context) : RelativeLayout(context) {

        val ivNavi: ImageView
        val ivAdd: ImageView

        init {
            ivNavi = ImageView(context).apply {
                id = 1221
                setImageResource(R.drawable.ic_navi_home)
            }
            addView(ivNavi, LayoutParams((8.33f * w).toInt(), (8.33f * w).toInt()).apply {
                leftMargin = (5.556f * w).toInt()
                addRule(CENTER_VERTICAL, TRUE)
            })

            val tvTitle = TextView(context).apply {
                textCustom(
                    resources.getString(R.string.alarms),
                    ContextCompat.getColor(context, R.color.white),
                    6.667f * w,
                    "display_heavy",
                    context
                )
            }
            addView(tvTitle, LayoutParams(-2, -2).apply {
                leftMargin = (4.44f * w).toInt()
                addRule(CENTER_VERTICAL, TRUE)
                addRule(RIGHT_OF, ivNavi.id)
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
}