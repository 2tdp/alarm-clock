package com.remi.alarmclock.xtreme.free.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.model.StopwatchModel
import com.remi.alarmclock.xtreme.free.recorder.CounterTimer
import com.remi.alarmclock.xtreme.free.utils.Utils
import java.util.*

class StopwatchService : Service() {

    companion object {
        var IS_SERVICE_RUNNING = false
    }

    private val mBinder = LocalBinder()

    private lateinit var builder: NotificationCompat.Builder
    private lateinit var action1PendingIntent: PendingIntent
    private lateinit var action2PendingIntent: PendingIntent
    val lstLap = mutableListOf<StopwatchModel>()
    lateinit var timer: CounterTimer
    var count = 0
    private var idNotify = 0
    private var isLap = true
    private var isPause = true

    override fun onCreate() {
        super.onCreate()
        IS_SERVICE_RUNNING = true
        idNotify = Random().nextInt()
        createNotification(this@StopwatchService, idNotify.toString())
        timer = CounterTimer(true, object : CounterTimer.OnTimerTickListener {
            override fun onTimerTick(duration: Array<String>) {
                LocalBroadcastManager.getInstance(this@StopwatchService)
                    .sendBroadcast(Intent().apply {
                        action = TAG_STOPWATCH
                        putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_START)
                        putExtra(NOTIFY_ID_START, duration)
                    })
                val strContent =
                    if (duration.size == 4) "${duration[3]}.${duration[2]}.${duration[1]}}"
                    else "${duration[2]}.${duration[1]}"
                isLap = true
                isPause = true
                startNotification(
                    this@StopwatchService,
                    strContent,
                    resources.getString(R.string.lap),
                    resources.getString(R.string.pause)
                )
            }
        })

        //button action 1
        val action1Intent = Intent(this, this@StopwatchService::class.java).apply {
            action = NOTIFY_STOPWATCH_ACTION_1
            putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, idNotify)
        }
        action1PendingIntent = PendingIntent.getService(
            this, idNotify, action1Intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )
        //button action 2
        val action2Intent = Intent(this, this@StopwatchService::class.java).apply {
            action = NOTIFY_STOPWATCH_ACTION_2
            putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, idNotify)
        }
        action2PendingIntent = PendingIntent.getService(
            this, idNotify, action2Intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        when (intent.action) {
            NOTIFY_STOPWATCH_ACTION_1 -> {
                if (isLap) addLap()
                else {
                    LocalBroadcastManager.getInstance(this@StopwatchService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_STOPWATCH
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_RESET)
                            putExtra(NOTIFY_ID_RESET, timer.format())
                        })
                    stopCounterTime()
                }
            }

            NOTIFY_STOPWATCH_ACTION_2 -> {
                if (isPause) {
                    pauseCounterTime()
                    LocalBroadcastManager.getInstance(this@StopwatchService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_STOPWATCH
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_PAUSE)
                            putExtra(NOTIFY_ID_PAUSE, timer.format())
                        })
                } else {
                    startCounterTime()
                    LocalBroadcastManager.getInstance(this@StopwatchService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_STOPWATCH
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_START)
                            putExtra(NOTIFY_ID_START, timer.format())
                        })
                }
            }
        }
        return START_NOT_STICKY
    }

    inner class LocalBinder : Binder() {
        fun getServiceInstance() = this@StopwatchService
    }

    fun startCounterTime() {
        isLap = true
        isPause = true
        timer.start()
    }

    fun pauseCounterTime() {
        isLap = false
        isPause = false
        timer.pause()
        val duration = timer.format()
        Handler(Looper.getMainLooper()).postDelayed({
            startNotification(
                this@StopwatchService,
                if (duration.size == 4) "${duration[3]}.${duration[2]}.${duration[1]}"
                else "${duration[2]}.${duration[1]}",
                resources.getString(R.string.reset),
                resources.getString(R.string.start)
            )
        }, 500L)
    }

    fun stopCounterTime() {
        isLap = true
        isPause = true
        timer.stop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) stopForeground(STOP_FOREGROUND_REMOVE)
        else stopForeground(true)
        stopSelf()
    }

    fun addLap() {
        if (!timer.isRun) return
        count++
        val strTime = timer.format()
        lstLap.add(
            0, StopwatchModel(
                "Lap $count",
                if (strTime.size == 4) "${strTime[3]}.${strTime[2]}.${strTime[1]},${strTime[0]}"
                else "${strTime[2]}.${strTime[1]},${strTime[0]}"
            )
        )

        LocalBroadcastManager.getInstance(this@StopwatchService).sendBroadcast(Intent().apply {
            action = TAG_STOPWATCH
            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_LAP)
            putExtra(NOTIFY_ID_LAP, Gson().toJsonTree(lstLap).asJsonArray.toString())
        })
    }

    private fun startNotification(
        context: Context,
        strContent: String,
        strBtn1: String,
        strBtn2: String
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        )
            return

        builder = NotificationCompat.Builder(context, idNotify.toString())
            .setSmallIcon(R.drawable.ic_notify)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentTitle(context.resources.getString(R.string.stopwatch))
            .setContentText("$strContent ${if (lstLap.isNotEmpty()) "Lap ${lstLap.size}" else ""}")
            .addAction(
                R.drawable.boder_bg_text_notify,
                Utils.getActionText(this, strBtn1, R.color.main_color), action1PendingIntent
            )
            .addAction(
                R.drawable.boder_bg_text_notify,
                Utils.getActionText(this, strBtn2, R.color.green_notify), action2PendingIntent
            )

        startForeground(idNotify, builder.build())
    }

    private fun createNotification(context: Context, channel: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val description = context.getString(R.string.channel_description)
            val name = context.getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_MIN
            val c = NotificationChannel(channel, name, importance).apply {
                this.description = description
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(c)
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when(intent.getStringExtra(TYPE_NOTIFY_ID)) {
                NOTIFY_ID_START -> {
                    if (!timer.isRun) return
                    startCounterTime()
                    LocalBroadcastManager.getInstance(this@StopwatchService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_STOPWATCH
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_START)
                            putExtra(NOTIFY_ID_START, timer.format())
                        })
                }
                NOTIFY_ID_PAUSE -> {
                    if (!timer.isRun) return

                    pauseCounterTime()
                    LocalBroadcastManager.getInstance(this@StopwatchService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_STOPWATCH
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_PAUSE)
                            putExtra(NOTIFY_ID_PAUSE, timer.format())
                        })
                }
                NOTIFY_ID_RESET -> {
                    if (!timer.isRun) return
                    LocalBroadcastManager.getInstance(this@StopwatchService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_STOPWATCH
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_RESET)
                            putExtra(NOTIFY_ID_RESET, timer.format())
                        })
                    stopCounterTime()
                }
                NOTIFY_ID_LAP -> if (timer.isRun) addLap()
            }
        }

    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onDestroy() {
        IS_SERVICE_RUNNING = false
        super.onDestroy()
    }
}