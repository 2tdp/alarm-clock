package com.remi.alarmclock.xtreme.free.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntduc.swipereveallayout.SwipeRevealLayout
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.addview.item.ViewItemTimezone
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.helpers.TAG_ITEM_TIMEZONE
import com.remi.alarmclock.xtreme.free.model.TimeZoneModel
import com.remi.alarmclock.xtreme.free.utils.Utils
import java.util.*

class TimezoneAdapter(context: Context, resId: Int, callBack: ICallBackItem): RecyclerView.Adapter<TimezoneAdapter.TimezoneHolder>() {

    private val context: Context
    private val resId: Int
    private val callBack: ICallBackItem

    private var w = 0F

    private var lstTimezone: MutableList<TimeZoneModel>
    private var lstOldTimezone: MutableList<TimeZoneModel>
    private var lstClock: MutableList<TimeZoneModel>

    init {
        this.context = context
        this.resId = resId
        this.callBack = callBack

        w = context.resources.displayMetrics.widthPixels / 100F

        lstTimezone = mutableListOf()
        lstOldTimezone = mutableListOf()
        lstClock = mutableListOf()
    }

    fun setDataTimezone(lstTimezone: MutableList<TimeZoneModel>) {
        this.lstTimezone = lstTimezone
        this.lstOldTimezone = ArrayList<TimeZoneModel>(lstTimezone)

        notifyChange()
    }

    fun setDataClock(lstClock: MutableList<TimeZoneModel>){
        this.lstClock = lstClock

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimezoneHolder {
        return TimezoneHolder(if (resId == TAG_ITEM_TIMEZONE) ViewItemTimezone(context)
        else LayoutInflater.from(context).inflate(R.layout.layout_item_clock, parent, false))
    }

    override fun getItemCount(): Int {
        if (resId == TAG_ITEM_TIMEZONE) {
            if (lstTimezone.isNotEmpty()) return lstTimezone.size
        } else {
            if (lstClock.isNotEmpty()) return lstClock.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: TimezoneHolder, position: Int) {
        if (resId == TAG_ITEM_TIMEZONE) holder.onBindTimezone(position)
        else holder.onBindClock(position)
    }

    @SuppressLint("SetTextI18n")
    inner class TimezoneHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var calendar : Calendar

        private lateinit var vItemTimezone: ViewItemTimezone
        private lateinit var vSwipe: SwipeRevealLayout
        private lateinit var swipeDel: LinearLayout
        private lateinit var swipeMain: RelativeLayout
        private lateinit var tvName: TextView
        private lateinit var tvTime: TextView
        private lateinit var tvDifference: TextView

        init {
            if (resId == TAG_ITEM_TIMEZONE) {
                this.vItemTimezone = itemView as ViewItemTimezone
                vItemTimezone.layoutParams = RelativeLayout.LayoutParams(-1, (16.667f * w).toInt())
            } else {
                this.vSwipe = itemView.findViewById(R.id.vSwipe)
                this.swipeDel = itemView.findViewById(R.id.swipeDel)
                this.swipeMain = itemView.findViewById(R.id.swipeMain)
                this.tvName = itemView.findViewById(R.id.tvName)
                this.tvTime = itemView.findViewById(R.id.tvTime)
                this.tvDifference = itemView.findViewById(R.id.tvDifference)

                this.swipeDel.background =
                    Utils.createBackground(
                        intArrayOf(Color.parseColor("#FFB9B9")),
                        (2.8f * w).toInt(), -1, -1
                    )
                this.swipeMain.background =
                    Utils.createBackground(
                        intArrayOf(Color.parseColor("#202020")),
                        (2.5f * w).toInt(), -1, -1
                    )
                this.tvName.apply {
                    marqueeRepeatLimit = -1
                    setHorizontallyScrolling(true)
                    isSingleLine = true
                    isSelected = true
                    ellipsize =  TextUtils.TruncateAt.MARQUEE
                }
            }
        }

        fun onBindTimezone(position: Int) {
            val timezone = lstTimezone[position]
            calendar = Calendar.getInstance()

            if (timezone.isChoose)
                vItemTimezone.apply {
                    ivTick.visibility = View.VISIBLE
                    setBackgroundColor(Color.parseColor("#202020"))
                }
            else vItemTimezone.apply {
                ivTick.visibility = View.GONE
                setBackgroundColor(Color.TRANSPARENT)
            }

            vItemTimezone.tvName.text = timezone.zoneName

            timezone.zoneName.let {
                calendar.timeZone = TimeZone.getTimeZone(timezone.zoneName)
                val hour =
                    if (calendar[Calendar.HOUR_OF_DAY] < 10) "0${calendar[Calendar.HOUR_OF_DAY]}"
                    else calendar[Calendar.HOUR_OF_DAY]
                val minute =
                    if (calendar[Calendar.MINUTE] < 10) "0${calendar[Calendar.MINUTE]}"
                    else calendar[Calendar.MINUTE]
                vItemTimezone.tvTime.text = "$hour:$minute"
            }

            vItemTimezone.setOnClickListener {
                setChoose(timezone, true)
                callBack.callBack(timezone, position)
            }
        }

        fun onBindClock(position: Int) {
            val clock = lstClock[position]
            calendar = Calendar.getInstance()

            when(position) {
                0 -> vSwipe.layoutParams = RelativeLayout.LayoutParams(-1, (23.33f * w).toInt()).apply {
                    leftMargin = (5.556f * w).toInt()
                    rightMargin = (5.556f * w).toInt()
                    topMargin = (5f * w).toInt()
                    bottomMargin = (2.22f * w).toInt()
                }
                lstClock.size - 1 -> vSwipe.layoutParams = RelativeLayout.LayoutParams(-1, (23.33f * w).toInt()).apply {
                    leftMargin = (5.556f * w).toInt()
                    rightMargin = (5.556f * w).toInt()
                    bottomMargin = (8.889f * w).toInt()
                    topMargin = (2.22f * w).toInt()
                }
                else -> vSwipe.layoutParams = RelativeLayout.LayoutParams(-1, (23.33f * w).toInt()).apply {
                    leftMargin = (5.556f * w).toInt()
                    rightMargin = (5.556f * w).toInt()
                    topMargin = (2.22f * w).toInt()
                    bottomMargin = (2.22f * w).toInt()
                }
            }

            if (clock.isOpenDel) vSwipe.open(false)
            else vSwipe.close(false)

            tvName.text = clock.zoneName

            clock.zoneName.let {
                calendar.timeZone = TimeZone.getTimeZone(clock.zoneName)

                val hour =
                    if (calendar[Calendar.HOUR_OF_DAY] < 10) "0${calendar[Calendar.HOUR_OF_DAY]}"
                    else calendar[Calendar.HOUR_OF_DAY]
                val minute =
                    if (calendar[Calendar.MINUTE] < 10) "0${calendar[Calendar.MINUTE]}"
                    else calendar[Calendar.MINUTE]
                tvTime.text = "$hour:$minute"
                tvDifference.text = calDifference(clock.gmtOffset.toInt())
            }

            swipeDel.setOnClickListener { if (vSwipe.isOpened) callBack.callBack(clock, position)}
            vSwipe.setSwipeListener(object : SwipeRevealLayout.SwipeListener{
                override fun onClosed(view: SwipeRevealLayout?) {
//                    clock.isOpenDel = false
                }

                override fun onOpened(view: SwipeRevealLayout?) {
//                    clock.isOpenDel = true
                }

                override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
                    clock.isOpenDel = slideOffset > 0.5
                }

            })
        }
    }

    fun setChoose(timeZone: TimeZoneModel, isAdd: Boolean) {
        for (time in lstOldTimezone) {
            if (timeZone.zoneName == time.zoneName) time.isChoose = isAdd
        }
    }

    fun filter(text: String) {
        Thread {
            lstTimezone.clear()
            if (text.isEmpty()) lstTimezone.addAll(lstOldTimezone)
            else {
                for (timezone in lstOldTimezone) {
                    if (timezone.zoneName.lowercase().contains(text.lowercase()))
                        lstTimezone.add(timezone)
                }
            }
            Handler(Looper.getMainLooper()).post { notifyChange() }
        }.start()
    }

    @SuppressLint("SimpleDateFormat")
    fun calDifference(gmt: Int): String {
        return "${gmt / 60 / 60 - Calendar.getInstance(Locale.getDefault())[Calendar.ZONE_OFFSET] / 1000 / 60 / 60}h00"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}