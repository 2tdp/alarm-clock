package com.remi.alarmclock.xtreme.free.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.addview.item.ViewItemRecord
import com.remi.alarmclock.xtreme.free.callback.ICallBack
import com.remi.alarmclock.xtreme.free.model.RingtoneModel

class RingtoneAdapter(context: Context, callBack: ICallBack): RecyclerView.Adapter<RingtoneAdapter.RecordHolder>() {

    private val context: Context
    private val callBack: ICallBack
    private var lstRingtone: ArrayList<RingtoneModel>
    private var w = 0F

    init {
        this.context = context
        this.callBack = callBack
        this.lstRingtone = ArrayList()

        w = context.resources.displayMetrics.widthPixels / 100F
    }

    fun setData(lstRingtone: ArrayList<RingtoneModel>) {
        this.lstRingtone = lstRingtone
        this.lstRingtone.reverse()

        notifyChange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
        return RecordHolder(ViewItemRecord(context))
    }

    override fun getItemCount(): Int {
        if (lstRingtone.isNotEmpty()) return lstRingtone.size
        return 0
    }

    override fun onBindViewHolder(holder: RecordHolder, position: Int) {
        holder.onBind(position)
    }

    inner class RecordHolder(itemView: ViewItemRecord): RecyclerView.ViewHolder(itemView) {

        val vRingtone: ViewItemRecord

        init {
            this.vRingtone = itemView
        }

        fun onBind(position: Int) {
            val ringtone = lstRingtone[position]

            when(position) {
                0 -> vRingtone.layoutParams =
                    RelativeLayout.LayoutParams(-1, -2).apply {
                        topMargin = (6.667f * w).toInt()
                        bottomMargin = (3.33f * w).toInt()
                    }
                lstRingtone.size - 1 -> vRingtone.layoutParams =
                    RelativeLayout.LayoutParams(-1, -2).apply {
                        bottomMargin = (6.667f * w).toInt()
                        topMargin = (3.33f * w).toInt()
                    }
                else -> vRingtone.layoutParams =
                    RelativeLayout.LayoutParams(-1, -2).apply {
                        bottomMargin = (3.33f * w).toInt()
                        topMargin = (3.33f * w).toInt()
                    }
            }

            if (ringtone.isChoose) vRingtone.ivTick.setImageResource(R.drawable.ic_choose)
            else vRingtone.ivTick.setImageResource(R.drawable.ic_un_choose)

            if (ringtone.typeRing == "defaultDevice" || ringtone.typeRing == "default")
                vRingtone.ivDel.visibility = View.GONE
            else vRingtone.ivDel.visibility = View.VISIBLE

            vRingtone.tvName.text = ringtone.nameFile
            vRingtone.ivDel.setOnClickListener { callBack.callback(ringtone, position, true) }
            vRingtone.setOnClickListener {
                setCurrent(ringtone)
                callBack.callback(ringtone, position, false)
            }
        }
    }

    fun setCurrent(record: RingtoneModel) {
        for (i in lstRingtone) i.isChoose = i.nameFile == record.nameFile

        notifyChange()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyChange() {
        notifyDataSetChanged()
    }
}