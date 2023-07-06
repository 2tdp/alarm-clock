package com.remi.alarmclock.xtreme.free.addview

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.extensions.textCustom

@SuppressLint("ResourceType")
class ViewRemoveAds(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val ivBack : ImageView
    val tvRemoveAds: TextView

    val vYear: ViewItemAds
    val vMonth: ViewItemAds
    val vLifetime: ViewItemAds

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_background))

        val ivBg2 = ImageView(context).apply {
            id = 1221
            setImageResource(R.drawable.im_ads)
        }
        addView(ivBg2, LayoutParams((55.556f * w).toInt(), (51.11f * w).toInt()).apply {
            topMargin = (18.889f * w).toInt()
            addRule(CENTER_HORIZONTAL, TRUE)
        })

        val tvDes = TextView(context).apply {
            id = 1222
            textCustom(
                resources.getString(R.string.des_ads),
                ContextCompat.getColor(context, R.color.white),
                3.33f * w,
                "display_regular",
                context
            )
            gravity = Gravity.CENTER
        }
        addView(tvDes, LayoutParams(-1, -2).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
            bottomMargin = (5.556 * w).toInt()
            leftMargin = (7.2f * w).toInt()
            rightMargin = (7.2f * w).toInt()
        })

        ivBack = ImageView(context).apply {
            setImageResource(R.drawable.ic_back)
        }
        addView(ivBack, LayoutParams((8.33f * w).toInt(), (8.33f * w).toInt()).apply {
            topMargin = (12.556f * w).toInt()
            leftMargin = (5.556f * w).toInt()
        })

        tvRemoveAds = TextView(context).apply {
            id = 1223
            textCustom(
                resources.getString(R.string.remove_ads),
                ContextCompat.getColor(context, R.color.black),
                5f * w,
                "display_bold",
                context
            )
            createBackground(
                intArrayOf(ContextCompat.getColor(context, R.color.main_color)),
                (2.5f * w).toInt(), -1, -1
            )
            gravity = Gravity.CENTER
        }
        addView(tvRemoveAds, LayoutParams(-1, (15.556f * w).toInt()).apply {
            addRule(ABOVE, tvDes.id)
            bottomMargin = (2.22f * w).toInt()
            leftMargin = (20f * w).toInt()
            rightMargin = (20f * w).toInt()
        })

        val rl = RelativeLayout(context)
        vMonth = ViewItemAds(context).apply {
            id = 1224
            tvDesItem.text = resources.getString(R.string.des_monthly)
            tvPrice.text = resources.getString(R.string.price_monthly)
            tvType.text = resources.getString(R.string.monthly)
        }
        rl.addView(vMonth, LayoutParams(-1, (24.167f * w).toInt()).apply {
            addRule(CENTER_VERTICAL, TRUE)
            leftMargin = (5.556f * w).toInt()
            rightMargin = (2.22f * w).toInt()
        })
        vLifetime = ViewItemAds(context).apply {
            tvPrice.text = resources.getString(R.string.price_life_time)
            tvType.text = resources.getString(R.string.life_time)
        }
        rl.addView(vLifetime, LayoutParams(-1, (24.167f * w).toInt()).apply {
            addRule(ABOVE, vMonth.id)
//            bottomMargin = (2.22f * w).toInt()
            leftMargin = (5.556f * w).toInt()
            rightMargin = (2.22f * w).toInt()
        })
        vYear = ViewItemAds(context).apply {
            tvDesItem.text = resources.getString(R.string.des_yearly)
            tvPrice.text = resources.getString(R.string.price_yearly)
            tvType.text = resources.getString(R.string.yearly)
        }
        rl.addView(vYear, LayoutParams(-1, (24.167f * w).toInt()).apply {
            addRule(BELOW, vMonth.id)
//            topMargin = (2.22f * w).toInt()
            leftMargin = (5.556f * w).toInt()
            rightMargin = (2.22f * w).toInt()
        })
        addView(rl, LayoutParams(-1, -1).apply {
            addRule(BELOW, ivBg2.id)
            addRule(ABOVE, tvRemoveAds.id)
        })
        clickOption(1)
    }

    fun clickOption(option: Int) {
        when(option) {
            0 -> {
                vLifetime.apply {
                    rl.createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.red)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    ivVip.visibility = VISIBLE
                }

                vMonth.apply {
                    rl.createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.gray2)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    ivVip.visibility = GONE
                }
                vYear.apply {
                    rl.createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.gray2)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    ivVip.visibility = GONE
                }
            }
            1 -> {
                vMonth.apply {
                    rl.createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.red)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    ivVip.visibility = VISIBLE
                }

                vLifetime.apply {
                    rl.createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.gray2)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    ivVip.visibility = GONE
                }
                vYear.apply {
                    rl.createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.gray2)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    ivVip.visibility = GONE
                }
            }
            2 -> {
                vYear.apply {
                    rl.createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.red)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    ivVip.visibility = VISIBLE
                }

                vLifetime.apply {
                    rl.createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.gray2)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    ivVip.visibility = GONE
                }
                vMonth.apply {
                    rl.createBackground(
                        intArrayOf(ContextCompat.getColor(context, R.color.gray2)),
                        (2.5f * w).toInt(), -1, -1
                    )
                    ivVip.visibility = GONE
                }
            }
        }
    }

    inner class ViewItemAds(context: Context): RelativeLayout(context) {

        val ivVip : ImageView
        val tvType: TextView
        val tvDesItem: TextView
        val tvPrice: TextView

        val rl: RelativeLayout

        init {
            rl = RelativeLayout(context).apply {
                createBackground(
                    intArrayOf(ContextCompat.getColor(context, R.color.red)),
                    (2.5f * w).toInt(), -1, -1
                )
            }
            tvType = TextView(context).apply {
                textCustom(
                    "",
                    ContextCompat.getColor(context, R.color.white),
                    5f * w,
                    "display_bold",
                    context
                )
            }
            rl.addView(tvType, LayoutParams(-2, -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
                leftMargin = (3.889f * w).toInt()
            })

            tvDesItem = TextView(context).apply {
                id = 1221
                textCustom(
                    "",
                    ContextCompat.getColor(context, R.color.white),
                    3.889f * w,
                    "display_regular",
                    context
                )
            }
            rl.addView(tvDesItem, LayoutParams(-2, -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
                addRule(ALIGN_PARENT_END, TRUE)
                rightMargin = (4.44f * w).toInt()
            })

            tvPrice = TextView(context).apply {
                textCustom(
                    "",
                    ContextCompat.getColor(context, R.color.white),
                    3.889f * w,
                    "display_regular",
                    context
                )
            }
            rl.addView(tvPrice, LayoutParams(-2, -2).apply {
                addRule(CENTER_VERTICAL, TRUE)
                addRule(LEFT_OF, tvDesItem.id)
            })

            addView(rl, LayoutParams(-1, -1).apply {
                topMargin = (5f * w).toInt()
                rightMargin = (3.33f * w).toInt()
            })

            ivVip = ImageView(context).apply { setImageResource(R.drawable.ic_vip) }
            addView(ivVip, LayoutParams((8.889f * w).toInt(), (8.889f * w).toInt()).apply {
                addRule(ALIGN_PARENT_END, TRUE)
            })
        }
    }
}