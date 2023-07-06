package com.remi.alarmclock.xtreme.free.activity

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.base.BaseActivity
import com.remi.alarmclock.xtreme.free.addview.ViewNotifyActivity
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager

class NotifyActivity : BaseActivity(Color.parseColor("#181818"), Color.parseColor("#181818")) {

    private lateinit var vNotify: ViewNotifyActivity
    private var typeNotify = ""

    override fun getViewLayout(): View {
        vNotify = ViewNotifyActivity(this@NotifyActivity)
        return vNotify
    }

    override fun setUp() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )

        intent.getStringExtra(TYPE_NOTIFY_ID)?.let {
            typeNotify = it
        }
        DataLocalManager.setCheck("ACTIVE_ACTIVITY_NOTIFY", true)

        when (typeNotify) {
            TAG_ALARM -> {
                vNotify.tvAction1.text = getString(R.string.snooze)
                vNotify.tvAction2.text = getString(R.string.stop)
            }

            TAG_TIMER -> {
                vNotify.tvAction1.text = getString(R.string.stop)
                vNotify.tvAction2.text = getString(R.string.repeat)
            }
        }

        vNotify.tvAction1.setOnClickListener {
            when (typeNotify) {
                TAG_ALARM -> {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
                        action = TAG_ACTIVITY_NOTIFY_ALARM
                        putExtra(TYPE_NOTIFY_ID, ACTIVITY_NOTIFY_ACTION_1)
                    })
                }

                TAG_TIMER -> {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
                        action = TAG_ACTIVITY_NOTIFY_TIMER
                        putExtra(TYPE_NOTIFY_ID, ACTIVITY_NOTIFY_ACTION_1)
                    })
                }
            }
            onBackPressed(false)
        }
        vNotify.tvAction2.setOnClickListener {
            when (typeNotify) {
                TAG_ALARM -> {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
                        action = TAG_ACTIVITY_NOTIFY_ALARM
                        putExtra(TYPE_NOTIFY_ID, ACTIVITY_NOTIFY_ACTION_2)
                    })
                }

                TAG_TIMER -> {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
                        action = TAG_ACTIVITY_NOTIFY_TIMER
                        putExtra(TYPE_NOTIFY_ID, ACTIVITY_NOTIFY_ACTION_2)
                    })
                }
            }
            onBackPressed(false)
        }
    }
}