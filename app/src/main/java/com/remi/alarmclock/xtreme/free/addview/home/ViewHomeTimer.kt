package com.remi.alarmclock.xtreme.free.addview.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.addview.ViewCounterTimer
import com.remi.alarmclock.xtreme.free.addview.ViewToolbar
import com.remi.alarmclock.xtreme.free.addview.item.ViewItemChoose
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.effectPressRectangle
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.utils.Utils
import com.remi.alarmclock.xtreme.free.viewcustom.CustomSeekbar
import com.shawnlin.numberpicker.NumberPicker

@SuppressLint("WrongConstant", "ResourceType", "ViewConstructor")
class ViewHomeTimer(context: Context, isSwipe: ICallBackCheck) : RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    private val isSwipe: ICallBackCheck
    var isEnableScroll = true

    val vToolbar: ViewToolbar
    private val rlCounterTime: RelativeLayout
    val vPickTimer: ViewPickTimer
    val vCounterTime: ViewCounterTimer
    val tvHour: TextView
    val tvMinute: TextView
    val tvSecond: TextView
    val tvTime: TextView
    val tvLeft: TextView
    val tvRight: TextView
    val vOption: ViewScroll

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_background))
        this.isSwipe = isSwipe

        vToolbar = ViewToolbar(context).apply {
            id = 1221
            tvTitle.text = resources.getString(R.string.timer)
            ivAdd.visibility = GONE
        }
        addView(vToolbar, LayoutParams(-1, (17.22f * w).toInt()).apply {
            topMargin = (12.33f * w).toInt()
        })

        val rlTime = RelativeLayout(context).apply { id = 1999 }
        vPickTimer = ViewPickTimer(context).apply { id = 1222 }
        rlTime.addView(vPickTimer, -1, -1)

        rlCounterTime = RelativeLayout(context).apply { visibility = GONE }
        vCounterTime = ViewCounterTimer(context)
        rlCounterTime.addView(vCounterTime, -1, -1)

        val v = View(context).apply {
            id = 1998
            setBackgroundColor(Color.TRANSPARENT)
        }
        rlCounterTime.addView(v, LayoutParams(1, 1).apply {
            addRule(CENTER_IN_PARENT)
        })

        val llTime = LinearLayout(context).apply {
            id = 1331
            orientation = LinearLayout.HORIZONTAL
        }
        tvHour = TextView(context).apply {
            textCustom(
                resources.getString(R.string.time_stopwatch), Color.WHITE,
                6.667f * w, "display_regular", context
            )
            gravity = Gravity.CENTER
        }
        llTime.addView(tvHour, LayoutParams((12.5f * w).toInt(), -2))
        val tvDot1 = TextView(context).apply {
            textCustom(
                ":", Color.WHITE,
                6.667f * w, "display_regular", context
            )
        }
        llTime.addView(tvDot1, -2, -2)
        tvMinute = TextView(context).apply {
            textCustom(
                resources.getString(R.string.time_stopwatch), Color.WHITE,
                6.667f * w, "display_regular", context
            )
            gravity = Gravity.CENTER
        }
        llTime.addView(tvMinute, LayoutParams((12.5f * w).toInt(), -2))
        val tvDot2 = TextView(context).apply {
            textCustom(
                ":", Color.WHITE,
                6.667f * w, "display_regular", context
            )
        }
        llTime.addView(tvDot2, -2, -2)
        tvSecond = TextView(context).apply {
            textCustom(
                resources.getString(R.string.time_stopwatch), Color.WHITE,
                6.667f * w, "display_regular", context
            )
            gravity = Gravity.CENTER
        }
        llTime.addView(tvSecond, LayoutParams((12.5f * w).toInt(), -2))
        rlCounterTime.addView(llTime, LayoutParams(-2, -2).apply {
            addRule(ABOVE, v.id)
            addRule(CENTER_HORIZONTAL, TRUE)
        })

        val llRing = LinearLayout(context).apply{
            gravity = Gravity.CENTER_VERTICAL
            orientation = LinearLayout.HORIZONTAL
        }
        val ivRing = ImageView(context).apply { setImageResource(R.drawable.ic_ring) }
        llRing.addView(ivRing, LayoutParams((5.556f * w).toInt(), (5.556f * w).toInt()).apply {
            gravity = Gravity.CENTER_VERTICAL
        })
        tvTime = TextView(context).apply {
            textCustom(
                "00:00", Color.parseColor("#737373"), 4.44f * w,
                "display_regular", context
            )
        }
        llRing.addView(tvTime, LayoutParams(-2, -2).apply {
            gravity = Gravity.CENTER_VERTICAL
            leftMargin = (2.22f * w).toInt()
        })
        rlCounterTime.addView(llRing, LayoutParams(-2, -2).apply {
            addRule(BELOW, v.id)
            addRule(CENTER_HORIZONTAL, TRUE)
        })

        rlTime.addView(rlCounterTime, -1, -1)
        addView(rlTime, LayoutParams(-1, (61.11f * w).toInt()).apply {
            addRule(BELOW, vToolbar.id)
            topMargin = (6.667f * w).toInt()
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
        })

        tvLeft = TextView(context).apply {
            id = 1223
            textCustom(
                resources.getString(R.string.cancel), Color.parseColor("#707070"), 3.889f * w,
                "display_bold", context
            )
            background = ContextCompat.getDrawable(context, R.drawable.im_bg_btn_left)
            gravity = Gravity.CENTER
        }
        addView(tvLeft, LayoutParams((20f * w).toInt(), (20f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            topMargin = (11.667f * w).toInt()
            addRule(BELOW, rlTime.id)
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
            topMargin = (11.667f * w).toInt()
            addRule(ALIGN_PARENT_END, TRUE)
            addRule(BELOW, rlTime.id)
        })

        vOption = ViewScroll(context).apply { isVerticalScrollBarEnabled = false }
        addView(vOption, LayoutParams(-1, -1).apply {
            addRule(BELOW, tvLeft.id)
            topMargin = (6.667f * w).toInt()
        })
    }

    fun actionTimer(action: String) {
        when(action) {
            ACTION_START_TIMER -> {
                tvLeft.setTextColor(Color.parseColor("#BCBCBC"))
                tvRight.apply {
                    text = resources.getString(R.string.pause)
                    background = ContextCompat.getDrawable(context, R.drawable.im_bg_btn_pause)
                }
                vPickTimer.visibility = GONE
                rlCounterTime.visibility = VISIBLE
            }
            ACTION_PAUSE_TIMER ->
                tvRight.apply {
                    text = resources.getString(R.string.resume)
                    background = ContextCompat.getDrawable(context, R.drawable.im_bg_btn_continue)
                }
            ACTION_CANCEL_TIMER -> {
                tvHour.text = resources.getString(R.string.time_stopwatch)
                tvMinute.text = resources.getString(R.string.time_stopwatch)
                tvSecond.text = resources.getString(R.string.time_stopwatch)
                tvLeft.setTextColor(Color.parseColor("#707070"))
                tvRight.apply {
                    text = resources.getString(R.string.start)
                    background = ContextCompat.getDrawable(context, R.drawable.im_bg_btn_start)
                }
                vPickTimer.visibility = VISIBLE
                rlCounterTime.visibility = GONE
            }
        }
    }

    inner class ViewScroll(context: Context): ScrollView(context) {

        val vRingtone: ViewItemChoose
        val sbVolume: CustomSeekbar
        val tvLvVolume : TextView
        val rlVibrate: RelativeLayout
        val swVibrate: SwitchCompat

        init {
            val rl = RelativeLayout(context)
            vRingtone = ViewItemChoose(context).apply {
                id = 1226
                tvTitle.text = resources.getString(R.string.ringtone)
                tvChoose.text = resources.getString(R.string.df)
                setPadding((5.556f * w).toInt(), 0, (5.556f * w).toInt(), 0)
                setBackgroundResource(effectPressRectangle(context).resourceId)
            }
            rl.addView(vRingtone, RelativeLayout.LayoutParams(-1, (6.667f * w).toInt()))

            val tvVolume = TextView(context).apply {
                id = 1228
                textCustom(
                    resources.getString(R.string.volume),
                    ContextCompat.getColor(context, R.color.gray_text),
                    4.44f * w,
                    "display_bold",
                    context
                )
            }
            rl.addView(tvVolume, RelativeLayout.LayoutParams(-2, -2).apply {
                leftMargin = (5.556f * w).toInt()
                topMargin = (5.556f * w).toInt()
                addRule(BELOW, vRingtone.id)
            })
            val rlVolume = RelativeLayout(context).apply { id = 1230 }
            tvLvVolume = TextView(context).apply {
                id = 1229
                textCustom(
                    "100%",
                    ContextCompat.getColor(context, R.color.white),
                    3.889f * w,
                    "display_regular",
                    context
                )
                gravity = Gravity.CENTER
            }
            rlVolume.addView(tvLvVolume, RelativeLayout.LayoutParams((9.722f * w).toInt(), -1).apply {
                addRule(ALIGN_PARENT_END, TRUE)
            })
            sbVolume = CustomSeekbar(context, isSwipe).apply {
                setMax(100)
                setProgress(100)
            }
            rlVolume.addView(sbVolume, RelativeLayout.LayoutParams(-1, (4.44f * w).toInt()).apply {
                addRule(LEFT_OF, tvLvVolume.id)
                addRule(CENTER_VERTICAL, TRUE)
                rightMargin = (3.33f * w).toInt()
            })
            rl.addView(rlVolume, RelativeLayout.LayoutParams(-1, (6.667f * w).toInt()).apply {
                leftMargin = (5.556f * w).toInt()
                rightMargin = (5.556f * w).toInt()
                topMargin = (3.33f * w).toInt()
                addRule(BELOW, tvVolume.id)
            })

            rlVibrate = RelativeLayout(context).apply { id = 1231 }
            val tvVibrate = TextView(context).apply {
                textCustom(
                    resources.getString(R.string.vibrate),
                    ContextCompat.getColor(context, R.color.gray_text),
                    4.44f * w,
                    "display_bold",
                    context
                )
            }
            rlVibrate.addView(tvVibrate, RelativeLayout.LayoutParams(-2, -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
            })
            swVibrate = SwitchCompat(context).apply {
                setThumbResource(R.drawable.custom_switch_thumb)
                setTrackResource(R.drawable.custom_switch_track)
            }
            rlVibrate.addView(swVibrate, RelativeLayout.LayoutParams((20.556f * w).toInt(), -1).apply {
                addRule(ALIGN_PARENT_END, TRUE)
                addRule(CENTER_VERTICAL, TRUE)
            })
            rl.addView(rlVibrate, RelativeLayout.LayoutParams(-1, (8.889f * w).toInt()).apply {
                leftMargin = (5.556f * w).toInt()
                rightMargin = (5.556f * w).toInt()
                topMargin = (6.667f * w).toInt()
                bottomMargin = (5.556f* w).toInt()
                addRule(BELOW, rlVolume.id)
            })

            addView(rl, LayoutParams(-1, -1))
        }

        override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
            return if (isEnableScroll) super.onInterceptTouchEvent(ev)
            else false
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(ev: MotionEvent?): Boolean {
            return if (isEnableScroll) super.onTouchEvent(ev)
            else false
        }
    }

    inner class ViewPickTimer(context: Context) : RelativeLayout(context) {

        val hourPicker: NumberPicker
        val minutePicker: NumberPicker
        val secondPicker: NumberPicker

        val tvHour: TextView
        val tvMinute: TextView
        val tvSecond: TextView

        init {
            val v = View(context).apply {
                createBackground(
                    intArrayOf(Color.parseColor("#202020")),
                    (2.5f * w).toInt(), -1, -1
                )
            }
            addView(v, LayoutParams(-1, (11.667f * w).toInt()).apply {
                addRule(CENTER_IN_PARENT, TRUE)
            })

            val rlHour = RelativeLayout(context)
            hourPicker = NumberPicker(context).apply {
                id = 1222
                maxValue = 24
                minValue = 0
                value = 0
                selectedTextSize = 4.44f * w
                textSize = 4.44f * w
                typeface = Utils.getTypeFace("display", "display_regular.otf", context)
                dividerColor = Color.TRANSPARENT
                orientation = NumberPicker.VERTICAL
                textColor = Color.WHITE
                selectedTextColor = Color.WHITE
                wheelItemCount = 5
            }
            rlHour.addView(hourPicker, LayoutParams((6.667f * w).toInt(), -1))
            tvHour = TextView(context).apply {
                textCustom(
                    resources.getString(R.string.hour), Color.WHITE, 3.889f * w,
                    "display_medium", context
                )
            }
            rlHour.addView(tvHour, LayoutParams((10.556f * w).toInt(), -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
                addRule(RIGHT_OF, hourPicker.id)
                leftMargin = (1.11f * w).toInt()
            })
            addView(rlHour, LayoutParams(-2, -1).apply {
                leftMargin = (3.33f * w).toInt()
            })

            val rlMinute = RelativeLayout(context)
            minutePicker = NumberPicker(context).apply {
                id = 1223
                maxValue = 60
                minValue = 0
                value = 10
                selectedTextSize = 4.44f * w
                textSize = 4.44f * w
                typeface = Utils.getTypeFace("display", "display_regular.otf", context)
                dividerColor = Color.TRANSPARENT
                orientation = NumberPicker.VERTICAL
                textColor = Color.WHITE
                selectedTextColor = Color.WHITE
                wheelItemCount = 5
            }
            rlMinute.addView(minutePicker, LayoutParams((6.667f * w).toInt(), -1))
            tvMinute = TextView(context).apply {
                textCustom(
                    resources.getString(R.string.minute), Color.WHITE, 3.889f * w,
                    "display_medium", context
                )
            }
            rlMinute.addView(tvMinute, LayoutParams((15f * w).toInt(), -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
                addRule(RIGHT_OF, minutePicker.id)
                leftMargin = (1.11f * w).toInt()
            })
            addView(rlMinute, LayoutParams(-2, -1).apply {
                addRule(CENTER_IN_PARENT, TRUE)
            })

            val rlSecond = RelativeLayout(context)
            secondPicker = NumberPicker(context).apply {
                id = 1223
                maxValue = 60
                minValue = 0
                value = 10
                selectedTextSize = 4.44f * w
                textSize = 4.44f * w
                typeface = Utils.getTypeFace("display", "display_regular.otf", context)
                dividerColor = Color.TRANSPARENT
                orientation = NumberPicker.VERTICAL
                textColor = Color.WHITE
                selectedTextColor = Color.WHITE
                wheelItemCount = 5
            }
            rlSecond.addView(secondPicker, LayoutParams((6.667f * w).toInt(), -1))
            tvSecond = TextView(context).apply {
                textCustom(
                    resources.getString(R.string.second), Color.WHITE, 3.889f * w,
                    "display_medium", context
                )
            }
            rlSecond.addView(tvSecond, LayoutParams((15.833f * w).toInt(), -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
                addRule(RIGHT_OF, secondPicker.id)
                leftMargin = (1.11f * w).toInt()
            })
            addView(rlSecond, LayoutParams(-2, -1).apply {
                addRule(ALIGN_PARENT_END, TRUE)
                rightMargin = (3.33f * w).toInt()
            })
        }
    }
}
