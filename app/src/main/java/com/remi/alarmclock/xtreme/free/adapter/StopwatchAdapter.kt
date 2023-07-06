package com.remi.alarmclock.xtreme.free.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.remi.alarmclock.xtreme.free.addview.item.ViewItemTimezone
import com.remi.alarmclock.xtreme.free.extensions.createBackground
import com.remi.alarmclock.xtreme.free.model.StopwatchModel

class StopwatchAdapter(context: Context): RecyclerView.Adapter<StopwatchAdapter.StopwatchHolder>() {

    private val context: Context
    private var w = 0F
    private var lstStopwatch: MutableList<StopwatchModel>

    init {
        this.context = context

        w = context.resources.displayMetrics.widthPixels / 100F
        lstStopwatch = mutableListOf()
    }

    fun setData(lstStopwatch: MutableList<StopwatchModel>) {

        this.lstStopwatch = lstStopwatch

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopwatchHolder {
        return StopwatchHolder(ViewItemTimezone(context))
    }

    override fun getItemCount(): Int {
        if (lstStopwatch.isNotEmpty()) return lstStopwatch.size
        return 0
    }

    override fun onBindViewHolder(holder: StopwatchHolder, position: Int) {
        holder.onBind(position)
    }

    inner class StopwatchHolder(itemView: ViewItemTimezone): RecyclerView.ViewHolder(itemView) {

        private val vStopwatch: ViewItemTimezone

        init {
            this.vStopwatch = itemView
            this.vStopwatch.createBackground(
                intArrayOf(Color.parseColor("#202020")), (2.5f * w).toInt(), -1, -1
            )
        }

        fun onBind(position: Int) {
            val stopwatch = lstStopwatch[position]

            when(position) {
                0 -> vStopwatch.layoutParams = RelativeLayout.LayoutParams(-1, (14.44f * w).toInt()).apply {
                    leftMargin = (5.556f * w).toInt()
                    rightMargin = (5.556f * w).toInt()
                    topMargin = (4.44f * w).toInt()
                    bottomMargin = (2.22f * w).toInt()
                }
                lstStopwatch.size - 1 -> vStopwatch.layoutParams = RelativeLayout.LayoutParams(-1, (14.44f * w).toInt()).apply {
                    leftMargin = (5.556f * w).toInt()
                    rightMargin = (5.556f * w).toInt()
                    bottomMargin = (4.44f * w).toInt()
                    topMargin = (2.22f * w).toInt()
                }
                else -> vStopwatch.layoutParams = RelativeLayout.LayoutParams(-1, (14.44f * w).toInt()).apply {
                    leftMargin = (5.556f * w).toInt()
                    rightMargin = (5.556f * w).toInt()
                    topMargin = (2.22f * w).toInt()
                    bottomMargin = (2.22f * w).toInt()
                }
            }

            vStopwatch.ivTick.visibility = View.GONE
            vStopwatch.tvName.text = stopwatch.name
            vStopwatch.tvTime.text = stopwatch.time
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange(){
        notifyDataSetChanged()
    }
}