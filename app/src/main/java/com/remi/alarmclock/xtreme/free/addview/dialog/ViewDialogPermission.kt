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
import com.remi.alarmclock.xtreme.free.utils.UtilsDrawable

@SuppressLint("ResourceType")
class ViewDialogPermission(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val vNotify: ViewItemPermission
    val vBattery: ViewItemPermission
    val vOverlay: ViewItemPermission

    val tvCancel: TextView
    val tvAllow: TextView

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundResource(R.drawable.boder_dialog)

        val tvTitle = TextView(context).apply {
            id = 1221
            textCustom(
                resources.getString(R.string.permission), Color.BLACK, 5f * w,
                "display_bold", context
            )
        }
        addView(tvTitle, LayoutParams(-2, -2).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (5f * w).toInt()
        })

        vNotify = ViewItemPermission(context).apply {
            id = 1222
            UtilsDrawable.changeIcon(
                context, "ring", 2, R.drawable.ic_ring, iv,
                Color.parseColor("#959595"), Color.parseColor("#959595")
            )
            tv.apply {
                text = context.getString(R.string.send_notifications)
                setTextColor(Color.parseColor("#959595"))
            }
        }
        addView(vNotify, LayoutParams(-2, -2).apply {
            topMargin = (8.33f * w).toInt()
            addRule(BELOW, tvTitle.id)
        })

        vBattery = ViewItemPermission(context).apply {
            id = 1223
            UtilsDrawable.changeIcon(
                context, "battery", 1, R.drawable.ic_battery, iv,
                Color.parseColor("#959595"), Color.parseColor("#959595")
            )
            tv.apply {
                text = context.getString(R.string.turn_battery_service)
                setTextColor(Color.parseColor("#959595"))
            }
        }
        addView(vBattery, LayoutParams(-2, -2).apply {
            addRule(BELOW, vNotify.id)
            topMargin = (4.44f * w).toInt()
        })

        vOverlay = ViewItemPermission(context).apply {
            id = 1224
            UtilsDrawable.changeIcon(
                context, "overlay", 3, R.drawable.ic_overlay, iv,
                Color.parseColor("#959595"), Color.parseColor("#959595")
            )
            tv.apply {
                text = context.getString(R.string.allow_apps_overlay)
                setTextColor(Color.parseColor("#959595"))
            }
        }
        addView(vOverlay, LayoutParams(-2, -2).apply {
            addRule(BELOW, vBattery.id)
            topMargin = (4.44f * w).toInt()
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
        tvAllow = TextView(context).apply {
            id = 1334
            textCustom(
                resources.getString(R.string.allow),
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
            leftMargin = (4.44f * w).toInt()
            rightMargin = (3.33f * w).toInt()
            topMargin = (8.33f * w).toInt()
            addRule(BELOW, vOverlay.id)
        })
        addView(tvAllow, LayoutParams((30.556f * w).toInt(), (15.556f * w).toInt()).apply {
            rightMargin = (4.44f * w).toInt()
            leftMargin = (3.33f * w).toInt()
            topMargin = (8.33f * w).toInt()
            addRule(BELOW, vOverlay.id)
            addRule(ALIGN_PARENT_END, TRUE)
        })
    }

    fun checkOption(option: Int) {
        when(option) {
            0 -> {
                vNotify.apply {
                    UtilsDrawable.changeIcon(
                        context, "ring", 2, R.drawable.ic_ring, iv,
                        Color.parseColor("#000000"), Color.parseColor("#000000")
                    )
                    tv.setTextColor(Color.BLACK)
                }
            }
            1 -> {
                vBattery.apply {
                    UtilsDrawable.changeIcon(
                        context, "battery", 1, R.drawable.ic_battery, iv,
                        Color.parseColor("#000000"), Color.parseColor("#000000")
                    )
                    tv.setTextColor(Color.parseColor("#000000"))
                }
            }
            2 -> {
                vOverlay.apply {
                    UtilsDrawable.changeIcon(
                        context, "overlay", 3, R.drawable.ic_overlay, iv,
                        Color.parseColor("#000000"), Color.parseColor("#000000")
                    )
                    tv.setTextColor(Color.parseColor("#000000"))
                }
            }
        }
    }

    inner class ViewItemPermission(context: Context): RelativeLayout(context) {

        val iv: ImageView
        val tv: TextView

        init {
            iv = ImageView(context).apply { id = 1221 }
            addView(iv, LayoutParams((6.667f * w).toInt(), (6.667f * w).toInt()).apply {
                leftMargin = (5f * w).toInt()
            })

            tv = TextView(context).apply {
                textCustom("", Color.WHITE, 3.889f * w, "display_regular", context)
            }
            addView(tv, LayoutParams(-2, -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
                addRule(RIGHT_OF, iv.id)
                leftMargin = (4.44f * w).toInt()
            })
        }
    }
}