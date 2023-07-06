package com.remi.alarmclock.xtreme.free.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.NotifyActivity
import com.remi.alarmclock.xtreme.free.helpers.ACTION_DISMISS
import com.remi.alarmclock.xtreme.free.helpers.ACTION_SNOOZE
import com.remi.alarmclock.xtreme.free.helpers.ACTIVITY_NOTIFY_ACTION_1
import com.remi.alarmclock.xtreme.free.helpers.ACTIVITY_NOTIFY_ACTION_2
import com.remi.alarmclock.xtreme.free.helpers.ALARM_NOTIFY
import com.remi.alarmclock.xtreme.free.helpers.ID_NOTIFY
import com.remi.alarmclock.xtreme.free.helpers.LIST_ALARM
import com.remi.alarmclock.xtreme.free.helpers.RELOAD_DATA_ALARM
import com.remi.alarmclock.xtreme.free.helpers.TAG_ACTIVITY_NOTIFY_ALARM
import com.remi.alarmclock.xtreme.free.helpers.TAG_ALARM
import com.remi.alarmclock.xtreme.free.helpers.TAG_FRAG_ALARM
import com.remi.alarmclock.xtreme.free.helpers.TIME_BEFORE
import com.remi.alarmclock.xtreme.free.helpers.TYPE_NOTIFY_ID
import com.remi.alarmclock.xtreme.free.model.AlarmModel
import com.remi.alarmclock.xtreme.free.recorder.CounterTimer
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager
import com.remi.alarmclock.xtreme.free.utils.Utils
import com.remi.alarmclock.xtreme.free.utils.UtilsTimer
import org.json.JSONObject
import java.io.IOException
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class AlarmService : Service() {

    companion object {
        var IS_SERVICE_RUNNING = false
    }

    private var idNotify = 0
    private var alarm: AlarmModel? = null
    private lateinit var audioManager : AudioManager
    private lateinit var wakeLock: PowerManager.WakeLock
    private lateinit var fullScreenIntent: Intent
    private var timer: Timer? = null
    lateinit var counterTimer: CounterTimer
    private var mediaPlayer: MediaPlayer? = null
    var isPlayAudio = false
    var isStartActivity = false
    private var isBefore = true

    override fun onCreate() {
        super.onCreate()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(TAG_ACTIVITY_NOTIFY_ALARM))
        LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                stopService()
            }
        }, IntentFilter(TAG_FRAG_ALARM))
        IS_SERVICE_RUNNING = true
        mediaPlayer = MediaPlayer()
        timer = Timer()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getString(R.string.app_name)).apply {
                acquire(1000)
            }
        }

        fullScreenIntent = Intent(this, NotifyActivity::class.java)
        fullScreenIntent.apply {
            putExtra(TYPE_NOTIFY_ID, TAG_ALARM)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action != ACTION_SNOOZE && intent.action != ACTION_DISMISS) {
            intent.getStringExtra(ALARM_NOTIFY)?.let {
                alarm = Gson().fromJson(JSONObject(it).toString(), AlarmModel::class.java)
            }
            idNotify = intent.getIntExtra(ID_NOTIFY, 0)
            alarm?.let {
                createNotification(this, idNotify.toString())
                if (it.isBefore) {
                    isBefore = true
                    startNotificationAlarm(this, it)
                    it.isBefore = false
                    UtilsTimer.createAlarm(
                        this, it,
                        UtilsTimer.convertTimeToTimeStamp(UtilsTimer.getDayCurrentInWeek(), it.hourAlarm, it.minuteAlarm)
                    )
                } else {
                    isBefore = false
                    it.isBefore = true
                    handPlay(it)
                }
            }
        }

        counterTimer = CounterTimer(false, object : CounterTimer.OnTimerTickListener {
            override fun onTimerTick(duration: Array<String>) {
                val curDuration =
                    duration[0].toInt() * 1000L + duration[1].toInt() * 60 * 1000L + duration[2].toInt() * 60 * 60 * 1000L
                if (curDuration == 0L) {
                    if (counterTimer.isRun) counterTimer.stop()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        Utils.createNotification(
                            this@AlarmService,
                            NotificationManager.IMPORTANCE_HIGH,
                            idNotify.toString()
                        )
                    alarm?.let {
                        isStartActivity = false
                        timer = Timer()
                        handPlay(it)
                    }
                } else startNotificationSnooze(
                    this@AlarmService, "${duration[1]}:${duration[0]}", alarm
                )
            }
        })

        when(intent.action) {
            ACTION_SNOOZE -> actionSnooze(alarm)
            ACTION_DISMISS -> {
                UtilsTimer.cancelNotification(this, idNotify)
                Handler(Looper.getMainLooper()).postDelayed({ setRepeatAlarm() }, TIME_BEFORE)
                stopService()
            }
        }

        return START_NOT_STICKY
    }

    fun stopService() {
        if (isPlayAudio) handStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) stopForeground(STOP_FOREGROUND_REMOVE)
        else stopForeground(true)
        stopSelf()
    }

    private fun actionSnooze(alarm: AlarmModel?) {
        if (isPlayAudio) handStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) stopForeground(STOP_FOREGROUND_REMOVE)
        else stopForeground(true)
        alarm?.let { counterTimer.startWithDuration(it.snooze * 60 * 1000L) }
    }

    private fun handPlay(alarm: AlarmModel) {
        audioManager.setStreamVolume(
            AudioManager.STREAM_NOTIFICATION, 0, AudioManager.FLAG_PLAY_SOUND
        )
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (alarm.isVibrate) Handler(Looper.getMainLooper()).post { Utils.makeVibrate(this@AlarmService) }
                if (!Utils.checkScreenOn(this@AlarmService)) {
                    isStartActivity = if (!DataLocalManager.getCheck("ACTIVE_ACTIVITY_NOTIFY")) {
                        startActivity(fullScreenIntent)
                        true
                    } else false
                }
                if (!isStartActivity) startNotification(this@AlarmService, alarm)
            }
        }, 0, 2000)

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * alarm.volume / 100,
            AudioManager.FLAG_PLAY_SOUND)
        try {
            mediaPlayer = MediaPlayer().apply {
                if (alarm.ringtone?.typeRing == "default") {
                    val descriptor = assets.openFd("music/${alarm.ringtone?.nameFile}.mp3")
                    setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
                } else setDataSource(alarm.ringtone?.uri)
                isLooping = true
                setOnPreparedListener {
                    isPlayAudio = true
                    start()
                }
                prepareAsync()
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    private fun handStop() {
        mediaPlayer?.let {
            if (isPlayAudio) {
                it.apply {
                    stop()
                    release()
                }
                isPlayAudio = false
            }
        }
        timer?.let {
            it.purge()
            it.cancel()
        }
    }

    private fun startNotification(context: Context, alarm: AlarmModel?) {
        alarm?.let {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                return

            val fullScreenPendingIntent = PendingIntent.getActivity(
                context, idNotify, fullScreenIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )

            //button snooze
            val snoozeIntent = Intent(context, AlarmService::class.java).apply {
                action = ACTION_SNOOZE
                putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, idNotify)
            }
            val snoozePendingIntent = PendingIntent.getService(
                context, idNotify, snoozeIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
            //button cancel
            val dismissIntent = Intent(context, AlarmService::class.java).apply {
                action = ACTION_DISMISS
                putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, idNotify)
            }
            val dismissPendingIntent = PendingIntent.getService(
                context, idNotify, dismissIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(context, idNotify.toString())
                .setSmallIcon(R.drawable.ic_notify)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setAutoCancel(true)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setContentTitle(it.name)
                .setContentText(UtilsTimer.convertTimeStampToTime(Calendar.getInstance(Locale.getDefault()).timeInMillis))
                .addAction(
                    R.drawable.boder_bg_text_notify,
                    Utils.getActionText(this, context.getString(R.string.snooze), R.color.main_color), snoozePendingIntent)
                .addAction(
                    R.drawable.boder_bg_text_notify,
                    Utils.getActionText(this, context.getString(R.string.dismiss), R.color.green_notify), dismissPendingIntent)

            startForeground(idNotify, builder.build())

            //wake up
            if (wakeLock.isHeld) wakeLock.release()
        }
    }

    private fun startNotificationAlarm(context: Context, alarm: AlarmModel?) {
        alarm?.let {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                return

            //button dismiss
            val dismissIntent = Intent(context, AlarmService::class.java).apply {
                action = ACTION_DISMISS
                putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, idNotify)
            }
            val dismissPendingIntent = PendingIntent.getService(
                context, idNotify, dismissIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(context, idNotify.toString())
                .setSmallIcon(R.drawable.ic_notify)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.upcoming_alarm)
                            + "\n"
                            + UtilsTimer.convertTimeStampToTime(UtilsTimer.convertTimeToTimeStamp(UtilsTimer.getDayInWeek(UtilsTimer.getDayInWeek()), alarm.hourAlarm, alarm.minuteAlarm)))
                .addAction(
                    R.drawable.boder_bg_text_notify,
                    Utils.getActionText(this, context.getString(R.string.dismiss), R.color.green_notify), dismissPendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)

            startForeground(idNotify, builder.build())
        }
    }

    private fun startNotificationSnooze(context: Context, strContent: String, alarm: AlarmModel?) {
        alarm?.let {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                return

            val builder = NotificationCompat.Builder(context, idNotify.toString())
                .setSmallIcon(R.drawable.ic_notify)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Remaining $strContent")

            startForeground(idNotify, builder.build())
        }
    }

    private fun createNotification(context: Context, channel: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val description = context.getString(R.string.channel_description)
            val name = context.getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val c = NotificationChannel(channel, name, importance).apply {
                this.description = description
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(c)
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            DataLocalManager.setCheck("ACTIVE_ACTIVITY_NOTIFY", false)
            when(intent.getStringExtra(TYPE_NOTIFY_ID)) {
                ACTIVITY_NOTIFY_ACTION_1 -> actionSnooze(alarm)
                ACTIVITY_NOTIFY_ACTION_2 -> {
                    UtilsTimer.cancelNotification(this@AlarmService, idNotify)
                    setRepeatAlarm()
                    stopService()
                }
            }
        }
    }

    private fun setRepeatAlarm() {
        val lstAlarm = DataLocalManager.getListAlarm(LIST_ALARM)
        for (alarm in lstAlarm) {
            if (alarm.id == idNotify) {
                if (alarm.repeat.isEmpty()) {
                    alarm.isRun = false
                    DataLocalManager.setListAlarm(lstAlarm, LIST_ALARM)
                    LocalBroadcastManager.getInstance(this@AlarmService)
                        .sendBroadcast(Intent(RELOAD_DATA_ALARM))
                } else UtilsTimer.sendAlarm(this, alarm)
                break
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        IS_SERVICE_RUNNING = false
        super.onDestroy()
    }
}