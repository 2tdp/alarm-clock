package com.remi.alarmclock.xtreme.free

import android.app.Application
import android.content.Context
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager

class MyApp : Application() {
    override fun onCreate() {
        DataLocalManager.init(applicationContext)
        super.onCreate()
    }
}