package com.remi.alarmclock.xtreme.free.addview.addalarm

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import com.remi.alarmclock.xtreme.free.recorder.WaveformView
import com.remi.alarmclock.xtreme.free.utils.UtilsBitmap
import com.remi.alarmclock.xtreme.free.viewcustom.ViewAnim

@SuppressLint("ResourceType")
class ViewAudioRecord(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val tvNoFile : TextView
    val tvAnim: ViewAnim
    val ivAddRecord: ImageView

    val rlRecording: RelativeLayout
    val tvNameRecord: TextView
    val tvDuration: TextView
    val waveformView: WaveformView
    val ivStopRecord: ImageView
    val rlOutRecord: View

    val rcvAudio: RecyclerView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        isClickable = true
        isFocusable = true

        tvNoFile = TextView(context).apply {
            textCustom(
                resources.getString(R.string.no_file_yet),
                ContextCompat.getColor(context, R.color.gray_text),
                4.44f * w,
                "display_regular",
                context
            )
        }
        addView(tvNoFile, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (32.778f * w).toInt()
        })

        ivAddRecord = ImageView(context).apply {
            id= 1221
            setImageResource(R.drawable.ic_add_record)
        }
        addView(ivAddRecord, LayoutParams((21.667f * w).toInt(), (21.667f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            addRule(CENTER_HORIZONTAL, TRUE)
            bottomMargin = (10f * w).toInt()
        })

        tvAnim = ViewAnim(context).apply {
            setBitmap(UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.ic_text_anim)!!)
        }
        addView(tvAnim, LayoutParams((60.556f * w).toInt(), (22.556f * w).toInt()).apply {
            addRule(ABOVE, ivAddRecord.id)
            addRule(CENTER_HORIZONTAL, TRUE)
            bottomMargin = (5.556f * w).toInt()
        })

        val rl = RelativeLayout(context)

        rlRecording = RelativeLayout(context).apply {
            id = 1330
            visibility = GONE
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (3.5f * w).toInt(), -1, -1
            )
            isClickable = true
            isFocusable = true
        }
        rlOutRecord = View(context).apply {
            visibility = GONE
            setBackgroundColor(ContextCompat.getColor(context, R.color.black2))
        }
        rl.addView(rlOutRecord, LayoutParams(-1, -1))
        tvNameRecord = TextView(context).apply {
            id = 1224
            textCustom(
                resources.getString(R.string.record_file),
                ContextCompat.getColor(context, R.color.white),
                5.556f * w,
                "display_bold",
                context
            )
        }
        rlRecording.addView(tvNameRecord, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (6.667f * w).toInt()
        })
        tvDuration = TextView(context).apply {
            id = 1225
            textCustom(
                resources.getString(R.string.duration),
                Color.WHITE, 4.44f * w, "display_regular", context
            )
        }
        rlRecording.addView(tvDuration, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, tvNameRecord.id)
            topMargin = (2.22f * w).toInt()
        })
        waveformView = WaveformView(context).apply {
            id = 1226
            setColor(ContextCompat.getColor(context, R.color.white))
        }
        rlRecording.addView(waveformView, LayoutParams(-1, (19.44f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (8.889f * w).toInt()
            addRule(BELOW, tvDuration.id)
        })
        ivStopRecord = ImageView(context).apply { setImageResource(R.drawable.ic_playing_record) }
        rlRecording.addView(ivStopRecord, LayoutParams((21.667f * w).toInt(), (21.667f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (8.889f * w).toInt()
            addRule(BELOW, waveformView.id)
        })
        rl.addView(rlRecording, LayoutParams(-1, (100f * w).toInt()).apply {
            bottomMargin = (-3.889 * w).toInt()
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        rcvAudio = RecyclerView(context).apply { isVerticalScrollBarEnabled = false }
        addView(rcvAudio, LayoutParams(-1, -1).apply {
            addRule(ABOVE, ivAddRecord.id)
            bottomMargin = (3.33f * w).toInt()
        })

        addView(rl, -1, -1)
    }

    fun bottomSheetRecord(option: String, context: Context) {
        when(option) {
            "show" -> {
                rlRecording.animation =
                    AnimationUtils.loadAnimation(context, R.anim.slide_up_in)
                rlRecording.visibility = VISIBLE

                rlOutRecord.visibility = VISIBLE
            }
            "hide" -> {
                rlRecording.animation =
                    AnimationUtils.loadAnimation(context, R.anim.slide_down_out)
                rlRecording.visibility = GONE

                rlOutRecord.visibility = GONE
            }
        }
    }
}