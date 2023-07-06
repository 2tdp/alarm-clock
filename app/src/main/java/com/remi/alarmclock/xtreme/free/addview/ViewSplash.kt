package com.remi.alarmclock.xtreme.free.addview

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import com.remi.alarmclock.xtreme.free.viewcustom.CustomSeekbarLoading

@SuppressLint("ResourceType")
class ViewSplash(context: Context): RelativeLayout(context) {
    companion object {
        var w = 0F
    }

    val customSeekbarLoading: CustomSeekbarLoading
    val v: LottieAnimationView

    init {
        w = resources.displayMetrics.widthPixels / 100F

        val ivBackground = ImageView(context).apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.black_background))
            scaleType = ImageView.ScaleType.FIT_XY
        }
        addView(ivBackground, -1, -1)

        v = LottieAnimationView(context).apply {
            setAnimation(R.raw.iv_sflash)
            repeatCount = LottieDrawable.INFINITE
        }
        v.playAnimation()
        addView(v, LayoutParams(-1, (81.11f * w).toInt()).apply {
            addRule(CENTER_IN_PARENT, TRUE)
            leftMargin = (9.4f * w).toInt()
            rightMargin = (9.4f * w).toInt()
        })

        val tv = TextView(context).apply {
            id = 1221
            textCustom(
                resources.getString(R.string.this_action_may_contain_ads),
                ContextCompat.getColor(context, R.color.white),
                3.89f * w,
                "display_regular",
                context
            )
        }
        addView(tv, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            bottomMargin = (19.16f * w).toInt()
        })

        customSeekbarLoading = CustomSeekbarLoading(context).apply {
            setColorStroke(ContextCompat.getColor(context, R.color.main_color))
            setColorPro(ContextCompat.getColor(context, R.color.main_color))
            setColorText(ContextCompat.getColor(context, R.color.white))
            setTextSize(5.556f * w)
            setSizeStroke(w)
            setProgress(0)
        }
        addView(customSeekbarLoading, LayoutParams(-1, (15.556f * w).toInt()).apply {
            addRule(ABOVE, tv.id)
            bottomMargin = (4.44f * w).toInt()
            leftMargin = (11.11f * w).toInt()
            rightMargin = (11.114f * w).toInt()
        })
    }
}