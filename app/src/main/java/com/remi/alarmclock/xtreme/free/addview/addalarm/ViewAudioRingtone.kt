package com.remi.alarmclock.xtreme.free.addview.addalarm

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.viewpager2.widget.ViewPager2
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom

@SuppressLint("ResourceType")
class ViewAudioRingtone(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val ivBack: ImageView
    val tvUnknown: TextView
    val tvRecord: TextView
    val vPager: ViewPager2

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_background))
        isFocusable = true
        isClickable = true

        val rlToolbar = RelativeLayout(context).apply { id = 1221 }

        ivBack = ImageView(context).apply {
            id = 1222
            setImageResource(R.drawable.ic_back)
        }
        rlToolbar.addView(ivBack, LayoutParams((8.33f * w).toInt(), (8.33f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            addRule(CENTER_VERTICAL, TRUE)
        })

        val tvTitle = TextView(context).apply {
            textCustom(
                resources.getString(R.string.ringtone) + "s",
                ContextCompat.getColor(context, R.color.white),
                6.667f * w, "display_heavy", context
            )
        }
        rlToolbar.addView(tvTitle, LayoutParams(-2, -2).apply {
            addRule(RIGHT_OF, ivBack.id)
            addRule(CENTER_VERTICAL, TRUE)
            leftMargin = (4.44f * w).toInt()
        })
        addView(rlToolbar, LayoutParams(-1, (17.22f * w).toInt()).apply {
            topMargin = (12.22f * w).toInt()
        })

        val rl = RelativeLayout(context).apply {
            id = 1223
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            setPadding((1.11f * w).toInt())
        }

        tvUnknown = TextView(context).apply {
            textCustom(
                resources.getString(R.string.unknown),
                ContextCompat.getColor(context, R.color.black),
                4.44f * w,
                "display_bold",
                context
            )
            gravity = Gravity.CENTER
        }
        rl.addView(tvUnknown, LayoutParams((42.778f * w).toInt(), (13.33f * w).toInt()).apply {
            rightMargin = (0.83f * w).toInt()
        })

        tvRecord = TextView(context).apply {
            textCustom(
                resources.getString(R.string.record),
                Color.parseColor("#6F6F6F"),
                4.44f * w,
                "display_bold",
                context
            )
            gravity = Gravity.CENTER
        }
        rl.addView(tvRecord, LayoutParams((42.778f * w).toInt(), (13.33f * w).toInt()).apply {
            rightMargin = (0.83f * w).toInt()
            addRule(ALIGN_PARENT_END, TRUE)
        })

        addView(rl, LayoutParams(-1, (15.2778f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
            addRule(BELOW, rlToolbar.id)
        })

        swipeLayout("unknown")

        vPager = ViewPager2(context).apply { isHorizontalScrollBarEnabled = false }
        addView(vPager, LayoutParams(-1, -1).apply {
            addRule(BELOW, rl.id)
        })
    }

    fun swipeLayout(option: String) {
        when (option) {
            "unknown" -> {
                tvUnknown.apply {
                    createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }

                tvRecord.apply {
                    setBackgroundColor(Color.TRANSPARENT)
                    setTextColor(Color.parseColor("#6F6F6F"))
                }
            }
            "record" -> {
                tvRecord.apply {
                    createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }

                tvUnknown.apply {
                    setBackgroundColor(Color.TRANSPARENT)
                    setTextColor(Color.parseColor("#6F6F6F"))
                }
            }
        }
    }
}