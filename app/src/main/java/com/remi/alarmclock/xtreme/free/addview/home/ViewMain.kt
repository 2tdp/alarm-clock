package com.remi.alarmclock.xtreme.free.addview.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import com.remi.alarmclock.xtreme.free.helpers.*

@SuppressLint("ResourceType")
class ViewMain(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val vPager: ViewPager2
    val vNavi: ViewNavigationHome

    val vAlarm: ViewItemBottomHome
    val vClock: ViewItemBottomHome
    val vTimer: ViewItemBottomHome
    val vStopWatch: ViewItemBottomHome

    init {
        w = resources.displayMetrics.widthPixels / 100F

        //bottom
        val llBottom = LinearLayout(context).apply {
            id = 1221
            setBackgroundColor(ContextCompat.getColor(context, R.color.black_background2))
        }
        vAlarm = ViewItemBottomHome(context).apply {
            iv.setImageResource(R.drawable.ic_bottom_alarm)
            tv.text = resources.getString(R.string.alarm)
        }
        llBottom.addView(vAlarm, LinearLayout.LayoutParams(-1, -1, 1F))

        vClock = ViewItemBottomHome(context).apply {
            iv.setImageResource(R.drawable.ic_bottom_clock)
            tv.text = resources.getString(R.string.clock)
        }
        llBottom.addView(vClock, LinearLayout.LayoutParams(-1, -1, 1F))

        vTimer = ViewItemBottomHome(context).apply {
            iv.setImageResource(R.drawable.ic_bottom_timer)
            tv.text = resources.getString(R.string.timer)
        }
        llBottom.addView(vTimer, LinearLayout.LayoutParams(-1, -1, 1F))

        vStopWatch = ViewItemBottomHome(context).apply {
            iv.setImageResource(R.drawable.ic_bottom_stopwtach)
            tv.text = resources.getString(R.string.stopwatch)
        }
        llBottom.addView(vStopWatch, LinearLayout.LayoutParams(-1, -1, 1F))

        addView(llBottom, LayoutParams(-1, (15.556f * w).toInt()).apply {
            addRule(ALIGN_PARENT_BOTTOM, TRUE)
        })
        chooseOptionBottom(context, TAG_ALARM)

        vPager = ViewPager2(context)
        addView(vPager, LayoutParams(-1, -1).apply {
            addRule(ABOVE, llBottom.id)
        })

        vNavi = ViewNavigationHome(context).apply { visibility = GONE }
        addView(vNavi, LayoutParams(-1, -1))
    }

    fun actionNavigation(context: Context, option: String) {
        when(option) {
            TAG_SHOW -> {
                vNavi.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left_small)
                vNavi.visibility = VISIBLE
                vNavi.rlBg.visibility = VISIBLE
            }
            TAG_HIDE -> {
                vNavi.rlBg.visibility = GONE
                vNavi.animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                vNavi.visibility = GONE
            }
        }
    }

    fun chooseOptionBottom(context: Context, option: String) {
        when(option) {
            TAG_ALARM -> {
                vAlarm.apply {
                    iv.setImageResource(R.drawable.ic_bottom_alarm_2)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.main_color))
                }

                vClock.apply {
                    iv.setImageResource(R.drawable.ic_bottom_clock)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                vTimer.apply {
                    iv.setImageResource(R.drawable.ic_bottom_timer)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                vStopWatch.apply {
                    iv.setImageResource(R.drawable.ic_bottom_stopwtach)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            TAG_CLOCK -> {
                vClock.apply {
                    iv.setImageResource(R.drawable.ic_bottom_clock_2)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.main_color))
                }

                vAlarm.apply {
                    iv.setImageResource(R.drawable.ic_bottom_alarm)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                vTimer.apply {
                    iv.setImageResource(R.drawable.ic_bottom_timer)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                vStopWatch.apply {
                    iv.setImageResource(R.drawable.ic_bottom_stopwtach)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            TAG_TIMER -> {
                vTimer.apply {
                    iv.setImageResource(R.drawable.ic_bottom_timer_2)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.main_color))
                }

                vAlarm.apply {
                    iv.setImageResource(R.drawable.ic_bottom_alarm)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                vClock.apply {
                    iv.setImageResource(R.drawable.ic_bottom_clock)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                vStopWatch.apply {
                    iv.setImageResource(R.drawable.ic_bottom_stopwtach)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            TAG_STOPWATCH -> {
                vStopWatch.apply {
                    iv.setImageResource(R.drawable.ic_bottom_stopwatch_2)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.main_color))
                }

                vAlarm.apply {
                    iv.setImageResource(R.drawable.ic_bottom_alarm)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                vClock.apply {
                    iv.setImageResource(R.drawable.ic_bottom_clock)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                vTimer.apply {
                    iv.setImageResource(R.drawable.ic_bottom_timer)
                    tv.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
        }
    }

    inner class ViewNavigationHome(context: Context) : RelativeLayout(context) {

        val rlBg: RelativeLayout
        val ivBack: ImageView

        val vRemoveAds: ViewItemNavi
        val vMoreApp: ViewItemNavi
        val vContact: ViewItemNavi
        val vLikeApp: ViewItemNavi
        val vPP: ViewItemNavi

        init {
            isFocusable = true
            isClickable = true

            val rlNavi = RelativeLayout(context).apply {
                id = 1222
                setBackgroundColor(Color.parseColor("#3F3F3F"))
            }
            val ivBg = ImageView(context).apply {
                id = 1221
                setImageResource(R.drawable.im_navi)
            }
            rlNavi.addView(ivBg, LayoutParams(-1, (61.11f * w).toInt()))

            val tvTitle = TextView(context).apply {
                textCustom(
                    resources.getString(R.string.app_name),
                    ContextCompat.getColor(context, R.color.white),
                    6.667f * w,
                    "display_bold",
                    context
                )
            }
            rlNavi.addView(tvTitle, LayoutParams(-2, -2).apply {
                addRule(CENTER_HORIZONTAL, TRUE)
                topMargin = (25f * w).toInt()
            })

            ivBack = ImageView(context).apply { setImageResource(R.drawable.ic_back) }
            rlNavi.addView(ivBack, LayoutParams((6.667f * w).toInt(), (6.667f * w).toInt()).apply {
                addRule(ALIGN_PARENT_END, TRUE)
                topMargin = (15.556f * w).toInt()
                rightMargin = (4.44f * w).toInt()
            })

            vRemoveAds = ViewItemNavi(context).apply {
                id = 1222
                iv.setImageResource(R.drawable.ic_navi_remove_ads)
                tv.text = resources.getString(R.string.remove_ads)
            }
            rlNavi.addView(vRemoveAds, LayoutParams(-1, (8.889f * w).toInt()).apply {
                addRule(BELOW, ivBg.id)
                topMargin = (6.667f * w).toInt()
            })
            vMoreApp = ViewItemNavi(context).apply {
                id = 1223
                iv.setImageResource(R.drawable.ic_navi_more_app)
                tv.text = resources.getString(R.string.more_app)
            }
            rlNavi.addView(vMoreApp, LayoutParams(-1, (8.889f * w).toInt()).apply {
                addRule(BELOW, vRemoveAds.id)
                topMargin = (6.667f * w).toInt()
            })
            vContact = ViewItemNavi(context).apply {
                id = 1224
                iv.setImageResource(R.drawable.ic_navi_contact)
                tv.text = resources.getString(R.string.contact_us)
            }
            rlNavi.addView(vContact, LayoutParams(-1, (8.889f * w).toInt()).apply {
                addRule(BELOW, vMoreApp.id)
                topMargin = (6.667f * w).toInt()
            })
            vLikeApp = ViewItemNavi(context).apply {
                id = 1225
                iv.setImageResource(R.drawable.ic_navi_like_app)
                tv.text = resources.getString(R.string.like_alarm_clock)
            }
            rlNavi.addView(vLikeApp, LayoutParams(-1, (8.889f * w).toInt()).apply {
                addRule(BELOW, vContact.id)
                topMargin = (6.667f * w).toInt()
            })
            vPP = ViewItemNavi(context).apply {
                id = 1226
                iv.setImageResource(R.drawable.ic_navi_pp)
                tv.text = resources.getString(R.string.pp)
            }
            rlNavi.addView(vPP, LayoutParams(-1, (8.889f * w).toInt()).apply {
                addRule(BELOW, vLikeApp.id)
                topMargin = (6.667f * w).toInt()
            })

            addView(rlNavi, LayoutParams((75.556f * w).toInt(), -1))

            rlBg = RelativeLayout(context).apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.black_background3))
            }
            addView(rlBg, LayoutParams(-1, -1).apply {
                addRule(RIGHT_OF, rlNavi.id)
            })
        }

        inner class ViewItemNavi(context: Context): RelativeLayout(context) {

            val iv: ImageView
            val tv: TextView

            init {
                iv = ImageView(context).apply { id = 1221 }
                addView(iv, LayoutParams((8.889f * w).toInt(), (8.889f * w).toInt()).apply {
                    addRule(CENTER_VERTICAL, TRUE)
                    leftMargin = (5.556f * w).toInt()
                })

                tv = TextView(context).apply {
                    textCustom(
                        "",
                        ContextCompat.getColor(context, R.color.white),
                        4.44f * w,
                        "display_regular",
                        context
                    )
                }
                addView(tv, LayoutParams(-2, -2).apply {
                    addRule(CENTER_VERTICAL, TRUE)
                    addRule(RIGHT_OF, iv.id)
                    leftMargin = (6.667f * w).toInt()
                })
            }
        }
    }

    inner class ViewItemBottomHome(context: Context): RelativeLayout(context) {

        val iv: ImageView
        val tv: TextView

        init {
            val rl = RelativeLayout(context)
            iv = ImageView(context).apply { id = 1221 }
            rl.addView(iv, LayoutParams((5.56f * w).toInt(), (5.56f * w).toInt()).apply {
                addRule(CENTER_HORIZONTAL, TRUE)
            })

            tv = TextView(context).apply {
                textCustom(
                    "",
                    ContextCompat.getColor(context, R.color.white),
                    3.33f * w,
                    "display_medium",
                    context
                )
            }
            rl.addView(tv, LayoutParams(-2, -2).apply {
                addRule(BELOW, iv.id)
                addRule(CENTER_HORIZONTAL, TRUE)
            })

            addView(rl, LayoutParams(-2, -2).apply {
                addRule(CENTER_IN_PARENT, TRUE)
            })
        }

    }
}