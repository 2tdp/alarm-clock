package com.remi.alarmclock.xtreme.free.addview.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import com.remi.alarmclock.xtreme.free.viewcustom.SimpleRatingBar

@SuppressLint("ResourceType")
class ViewDialogRattingApp(context: Context) : RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val ratingBar: SimpleRatingBar
    val tvSend: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        val rl = RelativeLayout(context).apply {
            createBackground(intArrayOf(Color.WHITE), (2.5f * w).toInt(), -1, -1)
        }
        tvSend = TextView(context).apply {
            id = 1225
            textCustom(
                resources.getString(R.string.send),
                ContextCompat.getColor(context, R.color.black),
                4.44f * w,
                "display_bold",
                context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        rl.addView(tvSend, LayoutParams(-1, (15.556f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            leftMargin = (18.33f * w).toInt()
            rightMargin = (18.33f * w).toInt()
            bottomMargin = (6.667f * w).toInt()
        })

        ratingBar = SimpleRatingBar(context).apply {
            id = 1224
            numberOfStars = 5
            rating = 0f
            stepSize = 0.1f
            starSize = 11.667f * w
            starCornerRadius = 3.5f * w
            fillColor = ContextCompat.getColor(context, R.color.gray_light2)
            pressedStarBackgroundColor = ContextCompat.getColor(context, R.color.gold)
            pressedFillColor = ContextCompat.getColor(context, R.color.gray_light2)
            starBackgroundColor = ContextCompat.getColor(context, R.color.gold)

        }
        rl.addView(ratingBar, LayoutParams(-1, (10f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL)
            addRule(ABOVE, tvSend.id)
            bottomMargin = (6.667f * w).toInt()
        })

        val tvDes = TextView(context).apply {
            id = 1223
            textCustom(
                resources.getString(R.string.rate_your_experience_with_us),
                ContextCompat.getColor(context, R.color.black),
                3.889f * w,
                "display_regular",
                context
            )
        }
        rl.addView(tvDes, LayoutParams(-2, -2).apply {
            bottomMargin = (6.667f * w).toInt()
            addRule(ABOVE, ratingBar.id)
            addRule(CENTER_HORIZONTAL, TRUE)
        })

        val tvTitle = TextView(context).apply {
            textCustom(
                resources.getString(R.string.rate_app),
                ContextCompat.getColor(context, R.color.black),
                5f * w,
                "display_bold",
                context
            )
        }
        rl.addView(tvTitle, LayoutParams(-2, -2).apply {
            bottomMargin = (2.22f * w).toInt()
            addRule(ABOVE, tvDes.id)
            addRule(CENTER_HORIZONTAL, TRUE)
        })

        addView(rl, LayoutParams(-1, (76.667f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        val iv = ImageView(context).apply {
            setImageResource(R.drawable.im_dialog_rate)
        }
        addView(iv, LayoutParams((41.667f * w).toInt(), (35f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
        })
    }
}