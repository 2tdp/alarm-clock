package com.remi.alarmclock.xtreme.free.addview.item

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom

@SuppressLint("ResourceType")
class ViewItemAlarm(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0f
    }

    val tvTitle: TextView
    val tvTime: TextView
    val tvTypeTime: TextView
    val sw: SwitchCompat
    val vRepeat: ViewRepeat
    val vDel: ImageView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        createBackground(
            intArrayOf(Color.parseColor("#202020")),
            (3f * w).toInt(), -1, -1
        )

        val rlTop = RelativeLayout(context).apply {
            id = 1221
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                colors = intArrayOf(
                    Color.parseColor("#2C2C2C"),
                    Color.parseColor("#2C2C2C")
                )
                cornerRadii = floatArrayOf(2.5f * w, 2.5f * w, 2.5f * w, 2.5f * w, 0f,0f,0f,0f)
            }
        }
        val ivIcon = ImageView(context).apply {
            id = 1222
            setImageResource(R.drawable.ic_notify)
        }
        rlTop.addView(ivIcon, LayoutParams((5.556f * w).toInt(), (5.556f * w).toInt()).apply {
            leftMargin = (4.44f * w).toInt()
            topMargin = (2.22f * w).toInt()
            addRule(CENTER_VERTICAL, TRUE)
        })
        tvTitle = TextView(context).apply {
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
            textCustom(
                "", Color.WHITE, 4.44f * w, "display_medium", context
            )
        }
        rlTop.addView(tvTitle, LayoutParams(-1, -2).apply {
            addRule(RIGHT_OF, ivIcon.id)
            leftMargin = (4.44f * w).toInt()
            rightMargin = (9.44f * w).toInt()
            addRule(CENTER_VERTICAL, TRUE)
        })
        addView(rlTop, LayoutParams(-1, (10f * w).toInt()))

        val rlCenter = RelativeLayout(context)
        tvTime = TextView(context).apply {
            id = 1223
            gravity = Gravity.CENTER
            textCustom(
                "", Color.WHITE, 6.667f * w, "display_regular", context
            )
        }
        rlCenter.addView(tvTime, LayoutParams(-2, -1).apply {
            addRule(CENTER_VERTICAL, TRUE)
        })
        tvTypeTime = TextView(context).apply {
            id = 1224
            gravity = Gravity.BOTTOM
            textCustom(
                "", Color.parseColor("#BCBCBC"),
                4.44f * w, "display_regular", context
            )
        }
        rlCenter.addView(tvTypeTime, LayoutParams(-2, -2).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            addRule(RIGHT_OF, tvTime.id)
            bottomMargin = (-2.22f * w).toInt()
            leftMargin = (1.11f * w).toInt()
        })
        sw = SwitchCompat(context).apply {
            setThumbResource(R.drawable.custom_switch_thumb)
            setTrackResource(R.drawable.custom_switch_track)
        }
        rlCenter.addView(sw, LayoutParams((20.556f * w).toInt(), (8.889f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
            addRule(CENTER_VERTICAL, TRUE)
        })
        addView(rlCenter, LayoutParams(-1, (8.889f * w).toInt()).apply {
            addRule(BELOW, rlTop.id)
            topMargin = (6.667f * w).toInt()
            leftMargin = (4.44f * w).toInt()
            rightMargin = (4.44f * w).toInt()
        })

        vRepeat = ViewRepeat(context)
        addView(vRepeat, LayoutParams(-2, -2).apply {
            leftMargin = (4.44f * w).toInt()
            bottomMargin = (3.33f * w).toInt()
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })

        vDel = ImageView(context).apply { setImageResource(R.drawable.im_del_alarm) }
        addView(vDel, LayoutParams((15f * w).toInt(), (11.667f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            addRule(ALIGN_PARENT_END, TRUE)
        })
    }

    fun setRepeat(lstRepeat: MutableList<String>) {
        vRepeat.tvMo.visibility = GONE
        vRepeat.tvTu.visibility = GONE
        vRepeat.tvWe.visibility = GONE
        vRepeat.tvTh.visibility = GONE
        vRepeat.tvFr.visibility = GONE
        vRepeat.tvSa.visibility = GONE
        vRepeat.tvSu.visibility = GONE
        vRepeat.tvDaily.visibility = GONE
        if (lstRepeat.size == 7) vRepeat.tvDaily.visibility = VISIBLE
        else {
            for (date in lstRepeat) {
                if (date == "mo") vRepeat.tvMo.visibility = VISIBLE
                if (date == "tu") vRepeat.tvTu.visibility = VISIBLE
                if (date == "we") vRepeat.tvWe.visibility = VISIBLE
                if (date == "th") vRepeat.tvTh.visibility = VISIBLE
                if (date == "fr") vRepeat.tvFr.visibility = VISIBLE
                if (date == "sa") vRepeat.tvSa.visibility = VISIBLE
                if (date == "su") vRepeat.tvSu.visibility = VISIBLE
            }
        }
    }

    inner class ViewRepeat(context: Context) : RelativeLayout(context) {

        val tvDaily: TextView
        val tvMo: TextView
        val tvTu: TextView
        val tvWe: TextView
        val tvTh: TextView
        val tvFr: TextView
        val tvSa: TextView
        val tvSu: TextView

        init {
            tvDaily = TextView(context).apply {
                textCustom(
                    resources.getString(R.string.daily),
                    Color.WHITE, 3.889f * w, "display_regular", context
                )
                createBackground(
                    intArrayOf(Color.parseColor("#2B2B2B")),
                    (2.5f * w).toInt(), -1, -1
                )
                gravity = Gravity.CENTER
                visibility = GONE
            }
            addView(tvDaily, LayoutParams((12.778f * w).toInt(), (6.667f * w).toInt()))

            tvMo = TextView(context).apply {
                id = 1221
                textCustom(
                    resources.getString(R.string.mo),
                    Color.WHITE, 3.889f * w, "display_regular", context
                )
                createBackground(
                    intArrayOf(Color.parseColor("#2B2B2B")),
                    (2.5f * w).toInt(), -1, -1
                )
                gravity = Gravity.CENTER
                visibility = GONE
            }
            addView(tvMo, LayoutParams(10 * w.toInt(), (6.667f * w).toInt()).apply {
                rightMargin = (0.556f * w).toInt()
            })

            tvTu = TextView(context).apply {
                id = 1222
                textCustom(
                    resources.getString(R.string.tu),
                    Color.WHITE, 3.889f * w, "display_regular", context
                )
                createBackground(
                    intArrayOf(Color.parseColor("#2B2B2B")),
                    (2.5f * w).toInt(), -1, -1
                )
                gravity = Gravity.CENTER
                visibility = GONE
            }
            addView(tvTu, LayoutParams(10 * w.toInt(), (6.667f * w).toInt()).apply {
                addRule(RIGHT_OF, tvMo.id)
                leftMargin = (0.556f * w).toInt()
                rightMargin = (0.556f * w).toInt()
            })

            tvWe = TextView(context).apply {
                id = 1223
                textCustom(
                    resources.getString(R.string.we),
                    Color.WHITE, 3.889f * w, "display_regular", context
                )
                createBackground(
                    intArrayOf(Color.parseColor("#2B2B2B")),
                    (2.5f * w).toInt(), -1, -1
                )
                gravity = Gravity.CENTER
                visibility = GONE
            }
            addView(tvWe, LayoutParams(10 * w.toInt(), (6.667f * w).toInt()).apply {
                addRule(RIGHT_OF, tvTu.id)
                leftMargin = (0.556f * w).toInt()
                rightMargin = (0.556f * w).toInt()
            })

            tvTh = TextView(context).apply {
                id = 1224
                textCustom(
                    resources.getString(R.string.th),
                    Color.WHITE, 3.889f * w, "display_regular", context
                )
                createBackground(
                    intArrayOf(Color.parseColor("#2B2B2B")),
                    (2.5f * w).toInt(), -1, -1
                )
                gravity = Gravity.CENTER
                visibility = GONE
            }
            addView(tvTh, LayoutParams(10 * w.toInt(), (6.667f * w).toInt()).apply {
                addRule(RIGHT_OF, tvWe.id)
                leftMargin = (0.556f * w).toInt()
                rightMargin = (0.556f * w).toInt()
            })

            tvFr = TextView(context).apply {
                id = 1225
                textCustom(
                    resources.getString(R.string.fr),
                    Color.WHITE, 3.889f * w, "display_regular", context
                )
                createBackground(
                    intArrayOf(Color.parseColor("#2B2B2B")),
                    (2.5f * w).toInt(), -1, -1
                )
                gravity = Gravity.CENTER
                visibility = GONE
            }
            addView(tvFr, LayoutParams(10 * w.toInt(), (6.667f * w).toInt()).apply {
                addRule(RIGHT_OF, tvTh.id)
                leftMargin = (0.556f * w).toInt()
                rightMargin = (0.556f * w).toInt()
            })

            tvSa = TextView(context).apply {
                id = 1226
                textCustom(
                    resources.getString(R.string.sa),
                    Color.WHITE, 3.889f * w, "display_regular", context
                )
                createBackground(
                    intArrayOf(Color.parseColor("#2B2B2B")),
                    (2.5f * w).toInt(), -1, -1
                )
                gravity = Gravity.CENTER
                visibility = GONE
            }
            addView(tvSa, LayoutParams(10 * w.toInt(), (6.667f * w).toInt()).apply {
                addRule(RIGHT_OF, tvFr.id)
                leftMargin = (0.556f * w).toInt()
                rightMargin = (0.556f * w).toInt()
            })

            tvSu = TextView(context).apply {
                id = 1227
                textCustom(
                    resources.getString(R.string.su),
                    Color.WHITE, 3.889f * w, "display_regular", context
                )
                createBackground(
                    intArrayOf(Color.parseColor("#2B2B2B")),
                    (2.5f * w).toInt(), -1, -1
                )
                gravity = Gravity.CENTER
                visibility = GONE
            }
            addView(tvSu, LayoutParams(10 * w.toInt(), (6.667f * w).toInt()).apply {
                addRule(RIGHT_OF, tvSa.id)
                leftMargin = (0.556f * w).toInt()
            })
        }
    }
}