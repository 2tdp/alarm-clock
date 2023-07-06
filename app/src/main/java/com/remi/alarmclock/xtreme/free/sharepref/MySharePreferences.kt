package com.remi.alarmclock.xtreme.free.sharepref

import android.content.Context

class MySharePreferences(context: Context) {
    private val MY_SHARED_PREFERENCE = "MY_SHARED_PREFERENCE"
    private val context: Context

    init {
        this.context = context
    }

    fun putBooleanValue(key: String?, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanValue(key: String?): Boolean {
        val sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }

    fun putStringWithKey(key: String?, value: String?) {
        val sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringWithKey(key: String?, value: String?): String? {
        val sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, value)
    }

    fun putIntWithKey(key: String?, value: Int) {
        val sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getIntWithKey(key: String?, defaultInt: Int): Int {
        val sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, defaultInt)
    }
}