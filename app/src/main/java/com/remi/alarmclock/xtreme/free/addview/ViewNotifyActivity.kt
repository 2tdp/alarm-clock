package com.remi.alarmclock.xtreme.free.addview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

@SuppressLint("ResourceType")
class ViewNotifyActivity(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val tvTime: TextView
    private val vRing: LottieAnimationView
    val tvAction1: TextView
    val tvAction2: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(Color.parseColor("#181818"))

        vRing = LottieAnimationView(context).apply {
            id = 1221
            setAnimation(R.raw.iv_sflash)
            repeatCount = LottieDrawable.INFINITE
        }
        vRing.playAnimation()
        addView(vRing, LayoutParams((84f * w).toInt(), (84f * w).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        })

        val rlTop = RelativeLayout(context)
        tvTime = TextView(context).apply {
            id = 1222
            textCustom("", Color.WHITE, 16.667f * w, "display_bold", context)
        }
        rlTop.addView(tvTime, LayoutParams(-2, -2).apply { addRule(CENTER_IN_PARENT, TRUE) })

        val tv = TextView(context).apply {
            textCustom(
                resources.getString(R.string.alarm), Color.WHITE, 6.667f * w,
                "display_regular", context
            )
        }
        rlTop.addView(tv, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, tvTime.id)
            topMargin = -(4.44f * w).toInt()
        })
        addView(rlTop, LayoutParams(-1, -1).apply { addRule(ABOVE, vRing.id) })

        tvAction2 = TextView(context).apply {
            id = 1223
            textCustom(
                resources.getString(R.string.stop), Color.WHITE, 4.44f * w, "display_bold", context)
            gravity = Gravity.CENTER
            createBackground(
                intArrayOf(Color.parseColor("#2C2C2C")),
                (2.5f * w).toInt(), -1, -1
            )
        }
        addView(tvAction2, LayoutParams(-1, (15.556f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
            bottomMargin = (9.44f * w).toInt()
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        tvAction1 = TextView(context).apply {
            textCustom(
                resources.getString(R.string.snooze), Color.BLACK, 4.44f * w, "display_bold", context)
            gravity = Gravity.CENTER
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                (2.5f * w).toInt(), -1, -1
            )
        }
        addView(tvAction1, LayoutParams(-1, (15.556f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
            bottomMargin = (6.667f * w).toInt()
            addRule(ABOVE, tvAction2.id)
        })

        val cal = Calendar.getInstance(Locale.getDefault())
        Timer().schedule(object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                Handler(Looper.getMainLooper()).post{
                    tvTime.text = "${cal[Calendar.HOUR_OF_DAY]}:${if (cal[Calendar.MINUTE] < 10) "0${cal[Calendar.MINUTE]}" else cal[Calendar.MINUTE]}"
                }
            }
        }, 0,1000)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}