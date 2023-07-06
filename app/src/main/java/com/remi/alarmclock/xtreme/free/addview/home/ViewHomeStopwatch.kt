package com.remi.alarmclock.xtreme.free.addview.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.addview.ViewToolbar
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import com.remi.alarmclock.xtreme.free.helpers.*

@SuppressLint("ResourceType")
class ViewHomeStopwatch(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val vToolbar: ViewToolbar
    val tvHour: TextView
    val tvDot1: TextView
    val tvMinute: TextView
    val tvSecond: TextView
    val tvMills: TextView
    val tvLeft: TextView
    val tvRight: TextView
    val rcvStopwatch: RecyclerView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_background))

        vToolbar = ViewToolbar(context).apply {
            id = 1221
            tvTitle.text = resources.getString(R.string.stopwatch)
            ivAdd.visibility = GONE
        }
        addView(vToolbar, LayoutParams(-1, (17.22f * w).toInt()).apply {
            topMargin = (12.33f * w).toInt()
        })

        val llTime = LinearLayout(context).apply {
            id = 1331
            orientation = LinearLayout.HORIZONTAL
        }
        tvHour = TextView(context).apply {
            visibility = GONE
            text = resources.getString(R.string.time_stopwatch)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 13.33f * w)
            gravity = Gravity.CENTER
        }
        llTime.addView(tvHour, LayoutParams((16.667f * w).toInt(), -2))
        tvDot1 = TextView(context).apply {
            visibility = GONE
            text = ":"
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 13.33f * w)
        }
        llTime.addView(tvDot1)
        tvMinute = TextView(context).apply {
            text = resources.getString(R.string.time_stopwatch)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 13.33f * w)
            gravity = Gravity.CENTER
        }
        llTime.addView(tvMinute, LayoutParams((16.667f * w).toInt(), -2))
        val tvDot2 = TextView(context).apply {
            text = ":"
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 13.33f * w)
        }
        llTime.addView(tvDot2)
        tvSecond = TextView(context).apply {
            text = resources.getString(R.string.time_stopwatch)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 13.33f * w)
            gravity = Gravity.CENTER
        }
        llTime.addView(tvSecond, LayoutParams((16.667f * w).toInt(), -2))
        val tvDot3 = TextView(context).apply {
            text = ","
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 13.33f * w)
        }
        llTime.addView(tvDot3)
        tvMills = TextView(context).apply {
            text = resources.getString(R.string.time_stopwatch)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 13.33f * w)
            gravity = Gravity.CENTER
        }
        llTime.addView(tvMills, LayoutParams((16.667f * w).toInt(), -2))
        addView(llTime, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, vToolbar.id)
            topMargin = (21.11f * w).toInt()
        })

        tvLeft = TextView(context).apply {
            id = 1223
            textCustom(
                resources.getString(R.string.lap), Color.parseColor("#707070"), 3.889f * w,
                "display_bold", context
            )
            background = ContextCompat.getDrawable(context, R.drawable.im_bg_btn_left)
            gravity = Gravity.CENTER
        }
        addView(tvLeft, LayoutParams((20f * w).toInt(), (20f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            topMargin = (21.944f * w).toInt()
            addRule(BELOW, llTime.id)
        })

        tvRight = TextView(context).apply {
            textCustom(
                resources.getString(R.string.start), Color.parseColor("#000000"), 3.889f * w,
                "display_bold", context
            )
            background = ContextCompat.getDrawable(context, R.drawable.im_bg_btn_start)
            gravity = Gravity.CENTER
        }
        addView(tvRight, LayoutParams((20f * w).toInt(), (20f * w).toInt()).apply {
            rightMargin = (5.556f * w).toInt()
            topMargin = (21.944f * w).toInt()
            addRule(ALIGN_PARENT_END, TRUE)
            addRule(BELOW, llTime.id)
        })

        rcvStopwatch = RecyclerView(context).apply { isVerticalScrollBarEnabled = false }
        addView(rcvStopwatch, LayoutParams(-1, -1).apply {
            addRule(BELOW, tvLeft.id)
            topMargin = (4.44f * w).toInt()
        })
    }

    fun actionStopwatch(action: String) {
        when(action) {
            ACTION_START_STOPWATCH -> {
                tvLeft.apply {
                    text = resources.getString(R.string.lap)
                    setTextColor(Color.parseColor("#BCBCBC"))
                }
                tvRight.apply {
                    text = resources.getString(R.string.pause)
                    background = ContextCompat.getDrawable(context, R.drawable.im_bg_btn_pause)
                }
            }

            ACTION_PAUSE_STOPWATCH -> {
                tvRight.apply {
                    text = resources.getString(R.string.start)
                    background = ContextCompat.getDrawable(context, R.drawable.im_bg_btn_start)
                }
                tvLeft.text = resources.getString(R.string.reset)
            }

            ACTION_RESET_STOPWATCH -> {
                tvHour.apply {
                    visibility = GONE
                    text = resources.getString(R.string.time_stopwatch)
                }
                tvMinute.text = resources.getString(R.string.time_stopwatch)
                tvSecond.text = resources.getString(R.string.time_stopwatch)
                tvMills.text = resources.getString(R.string.time_stopwatch)
                tvLeft.apply {
                    text = resources.getString(R.string.lap)
                    setTextColor(Color.parseColor("#707070"))
                }
                tvRight.apply {
                    text = resources.getString(R.string.start)
                    background = ContextCompat.getDrawable(context, R.drawable.im_bg_btn_start)
                }
            }
        }
    }
}