package com.remi.alarmclock.xtreme.free.addview.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.extensions.textCustom
import com.remi.alarmclock.xtreme.free.helpers.*
import java.util.*

@SuppressLint("ResourceType")
class ViewHomeClock(context: Context): RelativeLayout(context) {

    companion object {
        var w = 0F
    }

    val vToolbar: ViewToolbarClock
    val vPager: ViewPager2
    private val rlDot: RelativeLayout
    private val ivDot1: ImageView
    private val ivDot2: ImageView

    val rcvTimezone: RecyclerView
    val rcvClock: RecyclerView
    val rlShowSearch: RelativeLayout

    init {
        w = resources.displayMetrics.widthPixels / 100F
        setBackgroundColor(ContextCompat.getColor(context, R.color.black_background))

        vToolbar = ViewToolbarClock(context).apply {
            id = 1221
            tvTitle.text = resources.getString(R.string.clock)
        }
        addView(vToolbar, LayoutParams(-1, (17.22f * w).toInt()).apply {
            topMargin = (12.33f * w).toInt()
        })

        vPager = ViewPager2(context).apply { id = 1222 }
        addView(vPager, LayoutParams(-1, (56.389f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, vToolbar.id)
            topMargin = (3.33f * w).toInt()
        })

        rlDot = RelativeLayout(context).apply { id = 1223 }
        ivDot1 = ImageView(context).apply { setImageResource(R.drawable.ic_dot_selected) }
        rlDot.addView(ivDot1, LayoutParams((2.22f * w).toInt(), (2.22f * w).toInt()))
        ivDot2 = ImageView(context).apply { setImageResource(R.drawable.ic_dot) }
        rlDot.addView(ivDot2, LayoutParams((2.22f * w).toInt(), (2.22f * w).toInt()).apply {
            addRule(ALIGN_PARENT_END, TRUE)
        })
        addView(rlDot, LayoutParams((8.889f * w).toInt(), (2.22f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(BELOW, vPager.id)
            topMargin = (10f * w).toInt()
        })

        rcvTimezone = RecyclerView(context).apply {
            visibility = GONE
            isVerticalScrollBarEnabled = false
        }
        addView(rcvTimezone, LayoutParams(-1, -1).apply { addRule(BELOW, vToolbar.id) })

        rcvClock = RecyclerView(context).apply { isVerticalScrollBarEnabled = false }
        addView(rcvClock, LayoutParams(-1, -1).apply {
            addRule(BELOW, rlDot.id)
            topMargin = (5f * w).toInt()
        })

        rlShowSearch = RelativeLayout(context).apply { visibility = GONE }
        val v = View(context).apply {
            id = 1224
            setBackgroundColor(Color.TRANSPARENT)
        }
        rlShowSearch.addView(v, LayoutParams(-1, (0.33f * w).toInt()).apply {
            addRule(CENTER_IN_PARENT)
        })
        val iv = ImageView(context).apply { setImageResource(R.drawable.ic_search) }
        rlShowSearch.addView(iv, LayoutParams((27.778f * w).toInt(), (27.778f * w).toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            addRule(ABOVE, v.id)
            bottomMargin = (2.778f * w).toInt()
        })
        val tv = TextView(context).apply {
            textCustom(
                resources.getString(R.string.search_for_a_city), Color.parseColor("#6A6A6A"),
                5.556f * w, "display_regular", context
            )
        }
        rlShowSearch.addView(tv, LayoutParams(-2, -2).apply {
            addRule(BELOW, v.id)
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = (2.778f * w).toInt()
        })
        addView(rlShowSearch, LayoutParams(-1, -1).apply {
            addRule(BELOW, vToolbar.id)
        })

        actionSearch(context, TAG_HIDE)
    }

    fun setDotIndicator(position: Int) {
        when(position) {
            0 -> {
                ivDot1.setImageResource(R.drawable.ic_dot_selected)
                ivDot2.setImageResource(R.drawable.ic_dot)
            }
            1 -> {
                ivDot1.setImageResource(R.drawable.ic_dot)
                ivDot2.setImageResource(R.drawable.ic_dot_selected)
            }
        }
    }

    fun actionSearch(context: Context, option: String) {
        when(option) {
            TAG_HIDE -> {
                vToolbar.rlToolbar.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left_small)
                vToolbar.rlToolbar.visibility = VISIBLE

                vToolbar.rlSearch.animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_right)
                vToolbar.rlSearch.visibility = GONE

                rlDot.visibility = VISIBLE
                vPager.visibility = VISIBLE
                rlShowSearch.visibility = GONE
            }
            TAG_SHOW -> {
                vToolbar.rlToolbar.animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                vToolbar.rlToolbar.visibility = GONE

                vToolbar.rlSearch.animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                vToolbar.rlSearch.visibility = VISIBLE

                rlDot.visibility = GONE
                vPager.visibility = GONE
                rlShowSearch.visibility = VISIBLE
            }
        }
    }

    inner class ViewToolbarClock(context: Context): RelativeLayout(context) {

        val tvTitle: TextView
        val ivAdd: ImageView
        val rlToolbar: RelativeLayout

        val ivBack: ImageView
        val edtSearch: EditText
        val ivExit: ImageView
        val rlSearch: RelativeLayout

        init {
            w = resources.displayMetrics.widthPixels / 100F

            rlToolbar = RelativeLayout(context)
            tvTitle = TextView(context).apply {
                textCustom(
                    "",
                    ContextCompat.getColor(context, R.color.white),
                    6.667f * w,
                    "display_heavy",
                    context
                )
            }
            rlToolbar.addView(tvTitle, LayoutParams(-2, -2).apply {
                leftMargin = (5.556f * w).toInt()
                addRule(CENTER_VERTICAL, TRUE)
            })

            ivAdd = ImageView(context).apply {
                setImageResource(R.drawable.ic_add)
            }
            rlToolbar.addView(ivAdd, LayoutParams((8.33f * w).toInt(), (8.33f * w).toInt()).apply {
                addRule(ALIGN_PARENT_END, TRUE)
                addRule(CENTER_VERTICAL, TRUE)
                rightMargin = (5.556f * w).toInt()
            })
            addView(rlToolbar, LayoutParams(-1, -1))

            rlSearch = RelativeLayout(context).apply { visibility = GONE }
            ivBack = ImageView(context).apply {
                id = 1221
                setImageResource(R.drawable.ic_back)
            }
            rlSearch.addView(ivBack, LayoutParams((8.33f * w).toInt(), (8.33f * w).toInt()).apply {
                leftMargin = (5.556f * w).toInt()
                addRule(CENTER_VERTICAL, TRUE)
            })

            edtSearch = EditText(context).apply {
                textCustom(
                    resources.getString(R.string.search_for_a_city),
                    Color.parseColor("#6A6A6A"), Color.WHITE, 4.44f * w,
                    "display_regular", context
                )
                setBackgroundColor(Color.TRANSPARENT)
                gravity = Gravity.CENTER_VERTICAL
            }
            rlSearch.addView(edtSearch, LayoutParams(-1, -1).apply {
                addRule(RIGHT_OF, ivBack.id)
                leftMargin = (3.33f * w).toInt()
            })
            ivExit = ImageView(context).apply {
                visibility = GONE
                setImageResource(R.drawable.ic_exit)
            }
            rlSearch.addView(ivExit, LayoutParams((8.33f * w).toInt(), (8.33f * w).toInt()).apply {
                addRule(ALIGN_PARENT_END, TRUE)
                addRule(CENTER_VERTICAL, TRUE)
                rightMargin = (5.556f * w).toInt()
            })
            addView(rlSearch, LayoutParams(-1, -1))

            val vLine = View(context).apply { setBackgroundColor(Color.parseColor("#6A6A6A")) }
            rlSearch.addView(vLine, LayoutParams(-1, (0.34f * w).toInt()).apply {
                addRule(ALIGN_PARENT_BOTTOM, TRUE)
            })
        }
    }
}