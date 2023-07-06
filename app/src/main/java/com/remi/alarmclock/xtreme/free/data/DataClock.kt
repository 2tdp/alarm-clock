package com.remi.alarmclock.xtreme.free.data

import android.content.Context
import com.remi.alarmclock.xtreme.free.model.TimeZoneModel

object DataClock {

    fun getAllTimezone(context: Context): MutableList<TimeZoneModel> {
        val lstClock = mutableListOf<TimeZoneModel>()
        var nameCheck = ""
        val strJson = context.assets.open("clock/time_zone.csv").bufferedReader().use { it.readText() }
        val line = strJson.split("\n")
        for (s in line) {
            if (s == "") continue
            val str = s.split(",")
            if (str[0] != nameCheck) {
                lstClock.add(TimeZoneModel(str[0], str[1], str[2], str[3], str[4], str[5], false, false))
                nameCheck = str[0]
            }
        }

        return lstClock
    }
}