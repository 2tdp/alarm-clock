package com.remi.alarmclock.xtreme.free.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.remi.alarmclock.xtreme.free.helpers.LIST_ALARM
import com.remi.alarmclock.xtreme.free.helpers.TAG
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager
import com.remi.alarmclock.xtreme.free.utils.UtilsTimer

class RebootBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val lstAlarm = DataLocalManager.getListAlarm(LIST_ALARM)
        if (lstAlarm.isNotEmpty()) {
            for (alarm in lstAlarm) {
                if (alarm.isRun) UtilsTimer.sendAlarm(context, alarm)
            }
        }
    }
}