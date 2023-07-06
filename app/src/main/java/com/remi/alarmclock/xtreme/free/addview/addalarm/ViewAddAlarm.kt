package com.remi.alarmclock.xtreme.free.addview.addalarm

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.widget.*
import android.widget.RelativeLayout.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.addview.item.ViewItemChoose
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.effectPressRectangle
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import com.remi.alarmclock.xtreme.free.viewcustom.CustomSeekbar

@SuppressLint("ResourceType", "ViewConstructor", "InflateParams", "ClickableViewAccessibility")
class ViewAddAlarm(context: Context, isSwipe: ICallBackCheck): ScrollView(context) {

    companion object {
        var w = 0F
    }

    private val isSwipe: ICallBackCheck
    var isEnableScroll = true

    val vMinuteAlarm: ViewCustomMinuteAlarm
    val vHourAlarm: ViewCustomHourAlarm
    val swTypeTime: SwitchCompat

    val edtName: EditText

    val tvMo: TextView
    val tvTu: TextView
    val tvWe: TextView
    val tvTh: TextView
    val tvFr: TextView
    val tvSa: TextView
    val tvSu: TextView

    var isMo = false
    var isTu = false
    var isWe = false
    var isTh = false
    var isFr = false
    var isSa = false
    var isSu = false

    val vRingtone: ViewItemChoose

    val sbVolume: CustomSeekbar
    val tvLvVolume: TextView

    val rlVibrate: RelativeLayout
    val swVibrate: SwitchCompat

    val vSnooze: ViewItemChoose

    val tvCancel: TextView
    val tvSave: TextView

    init {
        this.isSwipe = isSwipe
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_background))
        isVerticalScrollBarEnabled = false

        val rl = RelativeLayout(context)
        vHourAlarm = ViewCustomHourAlarm(context, isSwipe)
        vMinuteAlarm = ViewCustomMinuteAlarm(context, object : ICallBackItem{
            override fun callBack(ob: Any, position: Int) {
                vHourAlarm.textMinutes = ob.toString()
                vHourAlarm.invalidate()
            }
        }, isSwipe).apply { id = 1221 }
        rl.addView(vMinuteAlarm, LayoutParams((134.44f * w).toInt(), (134.44f * w).toInt()).apply {
            leftMargin = (-17.22f * w).toInt()
            rightMargin = (-17.22f * w).toInt()
            topMargin = (-32.49f * w).toInt()
        })

        rl.addView(vHourAlarm, RelativeLayout.LayoutParams((79.44f * w).toInt(), (79.44f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL)
            topMargin = (-5.66f * w).toInt()
        })

        swTypeTime = SwitchCompat(context).apply {
            isChecked = false
            setThumbResource(R.drawable.custom_switch_thumb_time)
            setTrackResource(R.drawable.custom_switch_track_time)
        }
        rl.addView(swTypeTime, RelativeLayout.LayoutParams((22.22f * w).toInt(), (8.889f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (35.556f * w).toInt()
        })

        val tvName = TextView(context).apply {
            id = 1222
            textCustom(
                resources.getString(R.string.name),
                ContextCompat.getColor(context, R.color.gray_text),
                4.44f * w,
                "display_bold",
                context
            )
        }
        rl.addView(tvName, RelativeLayout.LayoutParams(-2, -2).apply {
            addRule(BELOW, vMinuteAlarm.id)
//            topMargin = -(2.22 * w).toInt()
            leftMargin = (5.556f * w).toInt()
        })

        edtName = EditText(context).apply {
            id = 1223
            maxLines = 1
            setLines(1)
            isSingleLine = true
            filters = arrayOf<InputFilter>(LengthFilter(100))
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            textCustom(
                "",
                ContextCompat.getColor(context, R.color.white2),
                ContextCompat.getColor(context, R.color.white),
                4.44f * w,
                "display_regular",
                context
            )
            setPadding((4.44f * w).toInt(), 0, (4.44f * w).toInt(), 0)
        }
        rl.addView(edtName, RelativeLayout.LayoutParams(-1, (15.555f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
            topMargin = (4.44f * w).toInt()
            addRule(BELOW, tvName.id)
        })
        edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length > 100) return
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        val tvRepeat = TextView(context).apply {
            id = 1224
            textCustom(
                resources.getString(R.string.repeat),
                ContextCompat.getColor(context, R.color.gray_text),
                4.44f * w,
                "display_bold",
                context
            )
        }
        rl.addView(tvRepeat, RelativeLayout.LayoutParams(-2, -2).apply {
            addRule(BELOW, edtName.id)
            topMargin = (6.667 * w).toInt()
            leftMargin = (5.556f * w).toInt()
        })

        val ll = LinearLayout(context).apply {
            id = 1225
            orientation = LinearLayout.HORIZONTAL
        }
        tvMo = TextView(context).apply {
            textCustom(
                resources.getString(R.string.mo),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "display_regular",
                context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvMo, LinearLayout.LayoutParams((10f * w).toInt(), -1, 1F).apply {
            rightMargin = (1.667f * w).toInt()
        })
        tvTu = TextView(context).apply {
            textCustom(
                resources.getString(R.string.tu),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "display_regular",
                context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvTu, LinearLayout.LayoutParams((10f * w).toInt(), -1, 1F).apply {
            rightMargin = (1.667f * w).toInt()
            leftMargin = (1.667f * w).toInt()
        })
        tvWe = TextView(context).apply {
            textCustom(
                resources.getString(R.string.we),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "display_regular",
                context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvWe, LinearLayout.LayoutParams((10f * w).toInt(), -1, 1F).apply {
            rightMargin = (1.667f * w).toInt()
            leftMargin = (1.667f * w).toInt()
        })
        tvTh = TextView(context).apply {
            textCustom(
                resources.getString(R.string.th),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "display_regular",
                context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvTh, LinearLayout.LayoutParams((10f * w).toInt(), -1, 1F).apply {
            rightMargin = (1.667f * w).toInt()
            leftMargin = (1.667f * w).toInt()
        })
        tvFr = TextView(context).apply {
            textCustom(
                resources.getString(R.string.fr),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "display_regular",
                context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvFr, LinearLayout.LayoutParams((10f * w).toInt(), -1, 1F).apply {
            rightMargin = (1.667f * w).toInt()
            leftMargin = (1.667f * w).toInt()
        })
        tvSa = TextView(context).apply {
            textCustom(
                resources.getString(R.string.sa),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "display_regular",
                context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvSa, LinearLayout.LayoutParams((10f * w).toInt(), -1, 1F).apply {
            rightMargin = (1.667f * w).toInt()
            leftMargin = (1.667f * w).toInt()
        })
        tvSu = TextView(context).apply {
            textCustom(
                resources.getString(R.string.su),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "display_regular",
                context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        ll.addView(tvSu, LinearLayout.LayoutParams((10f * w).toInt(), -1, 1F).apply {
            leftMargin = (1.667f * w).toInt()
        })
        rl.addView(ll, RelativeLayout.LayoutParams(-1, (10f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            rightMargin = (4.44f * w).toInt()
            topMargin = (4.44f * w).toInt()
            addRule(BELOW, tvRepeat.id)
        })

        vRingtone = ViewItemChoose(context).apply {
            id = 1226
            tvTitle.text = resources.getString(R.string.ringtone)
            tvChoose.text = resources.getString(R.string.df)
            setBackgroundResource(effectPressRectangle(context).resourceId)
        }
        rl.addView(vRingtone, RelativeLayout.LayoutParams(-1, (6.667f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
            topMargin = (6.667f * w).toInt()
            addRule(BELOW, ll.id)
        })

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
        rlVibrate.addView(swVibrate, RelativeLayout.LayoutParams((20.556f * w).toInt(), (11.11f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
            addRule(CENTER_VERTICAL, TRUE)
        })
        rl.addView(rlVibrate, RelativeLayout.LayoutParams(-1, (11.11f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
            topMargin = (6.667f * w).toInt()
            addRule(BELOW, rlVolume.id)
        })

        vSnooze = ViewItemChoose(context).apply {
            id = 1232
            tvTitle.text = resources.getString(R.string.snooze)
            tvChoose.text = resources.getString(R.string.time_snooze)
        }
        rl.addView(vSnooze, RelativeLayout.LayoutParams(-1, (6.667f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            rightMargin = (5.556f * w).toInt()
            topMargin = (6.667f * w).toInt()
            addRule(BELOW, rlVibrate.id)
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
        tvSave = TextView(context).apply {
            id = 1334
            textCustom(
                resources.getString(R.string.save),
                ContextCompat.getColor(context, R.color.black_background),
                4.44f * w, "display_bold", context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        rl.addView(tvCancel, RelativeLayout.LayoutParams((41.11f * w).toInt(), (15.278f * w).toInt()).apply {
            leftMargin = (5.556f * w).toInt()
            rightMargin = (3.33f * w).toInt()
            bottomMargin = (16.11f * w).toInt()
            topMargin = (10f * w).toInt()
            addRule(BELOW, vSnooze.id)
        })
        rl.addView(tvSave, RelativeLayout.LayoutParams((41.11f * w).toInt(), (15.278f * w).toInt()).apply {
            rightMargin = (5.556f * w).toInt()
            leftMargin = (3.33f * w).toInt()
            bottomMargin = (16.11f * w).toInt()
            topMargin = (10f * w).toInt()
            addRule(BELOW, vSnooze.id)
            addRule(ALIGN_PARENT_END, TRUE)
        })

        addView(rl, LayoutParams(-1, -1))
    }

    fun chooseRepeat(option: String) {
        when(option) {
            "mo" -> {
                tvMo.apply {
                    if (!isMo) {
                        setTextColor(ContextCompat.getColor(context, R.color.black_background))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    }
                    isMo = !isMo
                }
            }

            "tu" -> {
                tvTu.apply {
                    if (!isTu) {
                        setTextColor(ContextCompat.getColor(context, R.color.black_background))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    }
                    isTu = !isTu
                }
            }
            "we" -> {
                tvWe.apply {
                    if (!isWe) {
                        setTextColor(ContextCompat.getColor(context, R.color.black_background))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    }
                    isWe = !isWe
                }
            }

            "th" -> {
                tvTh.apply {
                    if (!isTh) {
                        setTextColor(ContextCompat.getColor(context, R.color.black_background))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    }
                    isTh = !isTh
                }
            }

            "fr" -> {
                tvFr.apply {
                    if (!isFr) {
                        setTextColor(ContextCompat.getColor(context, R.color.black_background))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    }
                    isFr = !isFr
                }
            }
            "sa" -> {
                tvSa.apply {
                    if (!isSa) {
                        setTextColor(ContextCompat.getColor(context, R.color.black_background))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    }
                    isSa = !isSa
                }
            }
            "su" -> {
                tvSu.apply {
                    if (!isSu) {
                        setTextColor(ContextCompat.getColor(context, R.color.black_background))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    } else {
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        createBackground(
                            intArrayOf(ContextCompat.getColor(context, R.color.black_background2)),
                            (2.5f * w).toInt(), -1, -1
                        )
                    }
                    isSu = !isSu
                }
            }
        }
    }

    fun setRepeat(lstRepeat: MutableList<String>) {
        for (repeat in lstRepeat) chooseRepeat(repeat)
    }

    fun getRepeat(): MutableList<String> {
        val repeat = mutableListOf<String>()
        if (isSu) repeat.add("su")
        if (isMo) repeat.add("mo")
        if (isTu) repeat.add("tu")
        if (isWe) repeat.add("we")
        if (isTh) repeat.add("th")
        if (isFr) repeat.add("fr")
        if (isSa) repeat.add("sa")

        return repeat
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