package com.remi.alarmclock.xtreme.free.extensions

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
    return (getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name }
}