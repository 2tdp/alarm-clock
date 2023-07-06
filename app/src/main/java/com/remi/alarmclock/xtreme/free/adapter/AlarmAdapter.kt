package com.remi.alarmclock.xtreme.free.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.remi.alarmclock.xtreme.free.addview.item.ViewItemAlarm
import com.remi.alarmclock.xtreme.free.callback.ICallBack
import com.remi.alarmclock.xtreme.free.model.AlarmModel

class AlarmAdapter(context: Context, callBack: ICallBack) :
    RecyclerView.Adapter<AlarmAdapter.AlarmHolder>() {

    private val context: Context
    private val callBack: ICallBack
    private var w = 0f
    private var lstAlarm: MutableList<AlarmModel>

    init {
        this.context = context
        this.callBack = callBack

        lstAlarm = mutableListOf()
        w = context.resources.displayMetrics.widthPixels / 100F
    }

    fun setData(lstAlarm: MutableList<AlarmModel>) {
        this.lstAlarm = lstAlarm
        this.lstAlarm.reverse()

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        return AlarmHolder(ViewItemAlarm(context))
    }

    override fun getItemCount(): Int {
        if (lstAlarm.isNotEmpty()) return lstAlarm.size
        return 0
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        holder.onBind(position)
    }

    inner class AlarmHolder(itemView: ViewItemAlarm) : RecyclerView.ViewHolder(itemView) {

        private val vItem: ViewItemAlarm

        init {
            this.vItem = itemView
        }

        @SuppressLint("SetTextI18n")
        fun onBind(position: Int) {
            val alarm = lstAlarm[position]

            when (position) {
                0 -> vItem.layoutParams =
                    RelativeLayout.LayoutParams(-1, (43.889f * w).toInt()).apply {
                        leftMargin = (5.556f * w).toInt()
                        rightMargin = (5.556f * w).toInt()
                        topMargin = (6.667f * w).toInt()
                        bottomMargin = (3.33f * w).toInt()
                    }

                lstAlarm.size - 1 -> vItem.layoutParams =
                    RelativeLayout.LayoutParams(-1, (43.889f * w).toInt()).apply {
                        leftMargin = (5.556f * w).toInt()
                        rightMargin = (5.556f * w).toInt()
                        topMargin = (3.33f * w).toInt()
                        bottomMargin = (6.667f * w).toInt()
                    }

                else -> vItem.layoutParams =
                    RelativeLayout.LayoutParams(-1, (43.889f * w).toInt()).apply {
                        leftMargin = (5.556f * w).toInt()
                        rightMargin = (5.556f * w).toInt()
                        topMargin = (3.33f * w).toInt()
                        bottomMargin = (3.33f * w).toInt()
                    }
            }

            val hour = if (alarm.hourAlarm < 10) "0${alarm.hourAlarm}" else "${alarm.hourAlarm}"
            val minute =
                if (alarm.minuteAlarm < 10) "0${alarm.minuteAlarm}" else "${alarm.minuteAlarm}"

            vItem.tvTitle.text = alarm.name
            vItem.tvTime.text = "$hour:$minute"
            vItem.tvTypeTime.text = if (alarm.isTypeTime == 0) "am" else "pm"
            vItem.sw.isChecked = alarm.isRun
            vItem.setRepeat(alarm.repeat)

            vItem.sw.setOnClickListener { callBack.callback(alarm, -2, vItem.sw.isChecked) }
            vItem.vDel.setOnClickListener { callBack.callback(alarm, -1, false) }
            vItem.setOnClickListener { callBack.callback(alarm, position, false) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}