package com.remi.alarmclock.xtreme.free.service

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.NotifyActivity
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.model.TimerModel
import com.remi.alarmclock.xtreme.free.recorder.CounterTimer
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager
import com.remi.alarmclock.xtreme.free.utils.Utils
import com.remi.alarmclock.xtreme.free.utils.Utils.createNotification
import java.io.IOException
import java.util.*

class TimerService : Service() {

    companion object {
        var IS_SERVICE_RUNNING = false
    }

    private val mBinder = LocalBinder()

    private lateinit var builder: NotificationCompat.Builder
    lateinit var counterTimer: CounterTimer
    var count = 0
    private var idNotify = 0
    private var isPause = true
    var isStartActivity = false

    private lateinit var audioManager: AudioManager
    private lateinit var wakeLock: PowerManager.WakeLock
    private lateinit var timer: Timer
    private lateinit var fullScreenIntent: Intent
    private var mediaPlayer: MediaPlayer? = null
    var isPlayAudio = false
    lateinit var timeModel: TimerModel

    override fun onCreate() {
        super.onCreate()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(TAG_ACTIVITY_NOTIFY_TIMER))
        IS_SERVICE_RUNNING = true
        idNotify = Random().nextInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotification(
                this@TimerService,
                NotificationManager.IMPORTANCE_MIN,
                idNotify.toString()
            )

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
            putExtra(TYPE_NOTIFY_ID, TAG_TIMER)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        counterTimer = CounterTimer(false, object : CounterTimer.OnTimerTickListener {
            override fun onTimerTick(duration: Array<String>) {
                val curDuration =
                    duration[0].toInt() * 1000L + duration[1].toInt() * 60 * 1000L + duration[2].toInt() * 60 * 60 * 1000L
                if (curDuration == 0L) {
                    LocalBroadcastManager.getInstance(this@TimerService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_TIMER
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_START)
                            putExtra(NOTIFY_ID_START, 0L)
                        })
                    stopCounterTime()
                    idNotify = Random().nextInt()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        createNotification(
                            this@TimerService,
                            NotificationManager.IMPORTANCE_HIGH,
                            idNotify.toString()
                        )
                    handPlay()
                } else {
                    LocalBroadcastManager.getInstance(this@TimerService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_TIMER
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_START)
                            putExtra(NOTIFY_ID_START, curDuration)
                        })

                    val strContent =
                        if (duration[2] != "00") "${duration[2]}:${duration[1]}:${duration[0]}"
                        else "${duration[1]}:${duration[0]}"
                    isPause = true
                    startNotificationTimer(
                        this@TimerService,
                        strContent,
                        resources.getString(R.string.pause)
                    )
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            NOTIFY_TIMER_ACTION_1 -> {
                if (isPause) {
                    pauseCounterTime()
                    LocalBroadcastManager.getInstance(this@TimerService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_TIMER
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_PAUSE)
                            putExtra(NOTIFY_ID_PAUSE, counterTimer.format())
                        })
                } else {
                    val duration = counterTimer.format()
                    val curDuration =
                        duration[0].toInt() * 1000L + duration[1].toInt() * 60 * 1000L + duration[2].toInt() * 60 * 60 * 1000L
                    LocalBroadcastManager.getInstance(this@TimerService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_TIMER
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_START)
                            putExtra(NOTIFY_ID_START, curDuration)
                        })
                    resumeCounterTime()
                }
            }

            NOTIFY_TIMER_ACTION_2 -> {
                LocalBroadcastManager.getInstance(this@TimerService).sendBroadcast(Intent().apply {
                    action = TAG_TIMER
                    putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_CANCEL)
                    putExtra(NOTIFY_ID_CANCEL, counterTimer.format())
                })
                stopService()
            }

            NOTIFY_MUSIC_ACTION_1 -> {
                LocalBroadcastManager.getInstance(this@TimerService).sendBroadcast(Intent().apply {
                    action = TAG_TIMER
                    putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_CANCEL)
                    putExtra(NOTIFY_ID_CANCEL, counterTimer.format())
                })
                stopService()
            }
        }
        return START_NOT_STICKY
    }

    inner class LocalBinder : Binder() {
        fun getServiceInstance() = this@TimerService
    }

    fun startCounterTime() {
        isPause = true
        counterTimer.startWithDuration(timeModel.duration)
    }

    fun resumeCounterTime() {
        isPause = true
        counterTimer.start()
    }

    fun pauseCounterTime() {
        isPause = false
        counterTimer.pause()
        val duration = counterTimer.format()
        Handler(Looper.getMainLooper()).postDelayed({
            startNotificationTimer(
                this@TimerService,
                if (duration[2] != "00") "${duration[2]}:${duration[1]}:${duration[0]}"
                else "${duration[1]}:${duration[0]}",
                resources.getString(R.string.resume)
            )
        }, 500)
    }

    fun stopCounterTime() {
        isPause = true
        if (counterTimer.isRun) counterTimer.stop()
    }

    fun stopService() {
        DataLocalManager.setCheck("ACTIVE_ACTIVITY_NOTIFY", false)
        if (counterTimer.isRun) stopCounterTime()
        if (isPlayAudio) handStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) stopForeground(STOP_FOREGROUND_REMOVE)
        else stopForeground(true)
        stopSelf()
    }

    fun handPlay() {
        audioManager.setStreamVolume(
            AudioManager.STREAM_NOTIFICATION, 0, AudioManager.FLAG_PLAY_SOUND
        )
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (!Utils.checkScreenOn(this@TimerService)) {
                    if (!DataLocalManager.getCheck("ACTIVE_ACTIVITY_NOTIFY")) {
                        startActivity(fullScreenIntent)
                        isStartActivity = true
                    }
                } else {
                    if (!isStartActivity) startNotificationMusic(this@TimerService)
                }
            }
        }, 0, 2000)

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * timeModel.volume / 100,
            AudioManager.FLAG_PLAY_SOUND
        )

        try {
            mediaPlayer = MediaPlayer().apply {
                if (timeModel.ringtone.typeRing == "default") {
                    val descriptor = assets.openFd("music/${timeModel.ringtone.nameFile}.mp3")
                    setDataSource(
                        descriptor.fileDescriptor,
                        descriptor.startOffset,
                        descriptor.length
                    )
                } else setDataSource(timeModel.ringtone.uri)
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
        timer.purge()
        timer.cancel()
    }

    private fun startNotificationMusic(context: Context) {

        //button action 1
        val action1Intent = Intent(this, this@TimerService::class.java).apply {
            action = NOTIFY_MUSIC_ACTION_1
            putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, idNotify)
        }
        val action1PendingIntent = PendingIntent.getService(
            this, idNotify, action1Intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, idNotify, fullScreenIntent,
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
            .setContentTitle(resources.getString(R.string.timer))
            .addAction(
                R.drawable.boder_bg_text_notify,
                resources.getString(R.string.stop),
                action1PendingIntent
            )

        startForeground(idNotify, builder.build())

        //wake up
        if (wakeLock.isHeld) wakeLock.release()
    }

    private fun startNotificationTimer(context: Context, strContent: String, strBtn1: String) {

        //button action 1
        val action1Intent = Intent(this, this@TimerService::class.java).apply {
            action = NOTIFY_TIMER_ACTION_1
            putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, idNotify)
        }
        val action1PendingIntent = PendingIntent.getService(
            this, idNotify, action1Intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )
        //button action 2
        val action2Intent = Intent(this, this@TimerService::class.java).apply {
            action = NOTIFY_TIMER_ACTION_2
            putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, idNotify)
        }
        val action2PendingIntent = PendingIntent.getService(
            this, idNotify, action2Intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )

        builder = NotificationCompat.Builder(context, idNotify.toString())
            .setSmallIcon(R.drawable.ic_notify)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentTitle(context.resources.getString(R.string.timer))
            .setContentText(strContent)
            .addAction(
                R.drawable.boder_bg_text_notify,
                Utils.getActionText(this, strBtn1, R.color.green_notify), action1PendingIntent
            )
            .addAction(R.drawable.boder_bg_text_notify, "Cancel", action2PendingIntent)

        startForeground(idNotify, builder.build())
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(TYPE_NOTIFY_ID)) {
                ACTIVITY_NOTIFY_ACTION_1 -> {
                    LocalBroadcastManager.getInstance(this@TimerService)
                        .sendBroadcast(Intent().apply {
                            action = TAG_TIMER
                            putExtra(TYPE_NOTIFY_ID, NOTIFY_ID_CANCEL)
                            putExtra(NOTIFY_ID_CANCEL, counterTimer.format())
                        })
                    stopService()
                }

                ACTIVITY_NOTIFY_ACTION_2 -> {
                    DataLocalManager.setCheck("ACTIVE_ACTIVITY_NOTIFY", false)
                    handStop()
                    timer = Timer()
                    startCounterTime()
                }
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