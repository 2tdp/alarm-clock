package com.remi.alarmclock.xtreme.free.fragment

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import com.remi.alarmclock.xtreme.free.addview.home.ViewHomeTimer
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.fragment.alarm.AudioRingtoneFragment
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.model.RingtoneModel
import com.remi.alarmclock.xtreme.free.model.TimerModel
import com.remi.alarmclock.xtreme.free.service.TimerService
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager
import com.remi.alarmclock.xtreme.free.utils.Utils
import com.remi.alarmclock.xtreme.free.utils.UtilsTimer
import com.remi.alarmclock.xtreme.free.viewcustom.OnSeekbarResult

@SuppressLint("SetTextI18n")
class TimerFragment : BaseFragment() {

    private lateinit var vTimer: ViewHomeTimer

    private var serviceTimer: TimerService? = null
    private lateinit var intentService: Intent
    private var isServiceRunning = false
    private var isBindingService = false

    var audioRingtoneFrag: AudioRingtoneFragment? = null

    private lateinit var timerModel: TimerModel
    private var record: RingtoneModel? = null
    private var isResetTimer = true
    private var volume = 100

    companion object {
        fun newInstance(): TimerFragment {
            val args = Bundle()
            val fragment = TimerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewLayout(): View {
        isServiceRunning = TimerService.IS_SERVICE_RUNNING
        vTimer = ViewHomeTimer(requireContext(), object : ICallBackCheck {
            override fun check(isCheck: Boolean) {
                vTimer.isEnableScroll = !isCheck
            }
        })
        return vTimer
    }

    override fun setUp() {
        intentService = Intent(requireContext(), TimerService::class.java)
        record = RingtoneModel("default 1", "music", "default", true)
        timerModel = TimerModel(0, 0, 0, 0L, record!!,100, true)
        evenCLick()
        if (isServiceRunning) {
            vTimer.actionTimer(ACTION_START_TIMER)
            requireContext().bindService(intentService, connectService, Context.BIND_AUTO_CREATE)
            isBindingService = true
        } else {
            DataLocalManager.getTimer(KEY_TIMER)?.let {
                timerModel = it
            }
            record = timerModel.ringtone
            vTimer.vPickTimer.hourPicker.value = timerModel.hour
            vTimer.vPickTimer.minutePicker.value = timerModel.minute
            vTimer.vPickTimer.secondPicker.value = timerModel.second
            vTimer.vOption.apply {
                vRingtone.tvChoose.text = timerModel.ringtone.nameFile
                sbVolume.setProgress(timerModel.volume)
                tvLvVolume.text = "${timerModel.volume}%"
                swVibrate.isChecked = timerModel.isVibrate
            }
        }

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(broadcastReceiver, IntentFilter(TAG_TIMER))
    }

    private fun evenCLick() {
        vTimer.tvRight.setOnClickListener {
            when (vTimer.tvRight.text) {
                resources.getString(R.string.start) -> startTimer()
                resources.getString(R.string.pause) -> {
                    vTimer.actionTimer(ACTION_PAUSE_TIMER)
                    vTimer.vCounterTime.animator?.pause()
                    serviceTimer?.pauseCounterTime()
                    isResetTimer = true
                }

                resources.getString(R.string.resume) -> {
                    vTimer.actionTimer(ACTION_START_TIMER)
                    serviceTimer?.resumeCounterTime()
                }
            }
        }
        vTimer.tvLeft.setOnClickListener {
            serviceTimer?.stopService()
            cancelTimer()
        }

        vTimer.vOption.sbVolume.onSeekbarResult = object : OnSeekbarResult {
            override fun onDown(v: View) {

            }

            override fun onMove(v: View, value: Int) {
                vTimer.vOption.tvLvVolume.text = "$value%"
                volume = vTimer.vOption.sbVolume.getProgress()
                timerModel.volume = value
            }

            override fun onUp(v: View, value: Int) {

            }
        }

        vTimer.vOption.rlVibrate.setOnClickListener {
            Utils.makeVibrate(requireContext())
            vTimer.vOption.swVibrate.isChecked = !vTimer.vOption.swVibrate.isChecked
            timerModel.isVibrate = !vTimer.vOption.swVibrate.isChecked
        }
        vTimer.vOption.swVibrate.setOnClickListener { Utils.makeVibrate(requireContext()) }

        vTimer.vOption.vRingtone.setOnClickListener {
            audioRingtoneFrag = AudioRingtoneFragment.newInstance(record!!, object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    if (position != -1) {
                        record = ob as RingtoneModel
                        timerModel.ringtone = record!!
                        vTimer.vOption.vRingtone.tvChoose.text = record?.nameFile
                    } else
                        audioRingtoneFrag?.let {
                            if (it.isRecording) it.audioRecordFrag?.stopRecorder()
                            parentFragmentManager.popBackStack("AudioRingtoneFragment", -1)
                            audioRingtoneFrag = null
                        }
                }
            })
            replaceFragment(parentFragmentManager, audioRingtoneFrag!!, true, true)
        }

        vTimer.vPickTimer.hourPicker.setOnValueChangedListener { _, _, newVal ->
            if (newVal > 2) vTimer.vPickTimer.tvHour.text = "Hours"
            else vTimer.vPickTimer.tvHour.text = "Hour"
        }
        vTimer.vPickTimer.minutePicker.setOnValueChangedListener { _, _, newVal ->
            if (newVal > 2) vTimer.vPickTimer.tvMinute.text = "Minutes"
            else vTimer.vPickTimer.tvMinute.text = "Minute"
        }
        vTimer.vPickTimer.secondPicker.setOnValueChangedListener { _, _, newVal ->
            if (newVal > 2) vTimer.vPickTimer.tvSecond.text = "Seconds"
            else vTimer.vPickTimer.tvSecond.text = "Second"
        }
    }

    private fun startTimer() {
        vTimer.actionTimer(ACTION_START_TIMER)
        timerModel.apply {
            hour = vTimer.vPickTimer.hourPicker.value
            minute = vTimer.vPickTimer.minutePicker.value
            second = vTimer.vPickTimer.secondPicker.value
            duration =
                timerModel.second * 1000L + timerModel.minute * 60 * 1000L + timerModel.hour * 60 * 60 * 1000L
            isVibrate = vTimer.vOption.swVibrate.isChecked
        }
        DataLocalManager.setTimer(KEY_TIMER, timerModel)

        isResetTimer = false
        vTimer.vCounterTime.setSweepAngle(360f, timerModel.duration)
        vTimer.tvTime.text =
            DateFormat.format("HH:mm", UtilsTimer.getTimeCurrent() + timerModel.duration).toString()

        requireContext().startService(intentService)
        isServiceRunning = TimerService.IS_SERVICE_RUNNING
        if (!isBindingService) {
            requireContext().bindService(intentService, connectService, Context.BIND_AUTO_CREATE)
            isBindingService = true
        }
    }

    private fun cancelTimer() {
        vTimer.actionTimer(ACTION_CANCEL_TIMER)
        vTimer.vCounterTime.apply {
            setSweepAngle(0f, 0L)
            destroyAnimator()
        }
        if (isBindingService) {
            requireContext().unbindService(connectService)
            isBindingService = false
        }
        isServiceRunning = TimerService.IS_SERVICE_RUNNING
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when(intent.getStringExtra(TYPE_NOTIFY_ID)) {
                NOTIFY_ID_START -> {
                    val curDuration = intent.getLongExtra(NOTIFY_ID_START, 0L)
                    vTimer.tvHour.text = "%02d".format(curDuration / (1000 * 60 * 60))
                    vTimer.tvMinute.text = "%02d".format((curDuration / (1000 * 60)) % 60)
                    vTimer.tvSecond.text = "%02d".format((curDuration / 1000) % 60)
                    if (curDuration > 0) {
                        if (isResetTimer) {
                            vTimer.tvTime.text = DateFormat.format(
                                "HH:mm",
                                UtilsTimer.getTimeCurrent() + curDuration
                            ).toString()
                            isResetTimer = false
                        }
                        vTimer.actionTimer(ACTION_START_TIMER)
                        vTimer.vCounterTime.setSweepAngle(curDuration * 360f / timerModel.duration, curDuration)
                        if (vTimer.vCounterTime.animator?.isPaused!!)
                            vTimer.vCounterTime.animator?.resume()
                    }
                }

                NOTIFY_ID_CANCEL -> cancelTimer()
                NOTIFY_ID_PAUSE -> {
                    vTimer.actionTimer(ACTION_PAUSE_TIMER)
                    vTimer.vCounterTime.animator?.pause()
                    isResetTimer = true
                }
            }
        }
    }

    private val connectService = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.LocalBinder
            serviceTimer = binder.getServiceInstance()
            serviceTimer?.let {
                if (!isServiceRunning) {
                    it.timeModel = timerModel
                    it.startCounterTime()
                } else {
                    timerModel = it.timeModel
                    record = it.timeModel.ringtone
                    vTimer.vPickTimer.hourPicker.value = timerModel.hour
                    vTimer.vPickTimer.minutePicker.value = timerModel.minute
                    vTimer.vPickTimer.secondPicker.value = timerModel.second
                    vTimer.vOption.apply {
                        vRingtone.tvChoose.text = it.timeModel.ringtone.nameFile
                        sbVolume.setProgress(it.timeModel.volume)
                        tvLvVolume.text = "${it.timeModel.volume}%"
                        swVibrate.isChecked = it.timeModel.isVibrate
                    }
                    if (!it.counterTimer.isRun) {
                        vTimer.actionTimer(ACTION_PAUSE_TIMER)
                        val curDuration = it.counterTimer.duration
                        vTimer.tvHour.text = "%02d".format(curDuration / (1000 * 60 * 60))
                        vTimer.tvMinute.text = "%02d".format((curDuration / (1000 * 60)) % 60)
                        vTimer.tvSecond.text = "%02d".format((curDuration / 1000) % 60)
                        vTimer.vCounterTime.setSweepAngle(curDuration * 360f / it.timeModel.duration, curDuration)
                        vTimer.vCounterTime.animator?.pause()
                    }
                }
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(TAG, "onServiceDisconnected: ")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        DataLocalManager.setTimer(KEY_TIMER, timerModel)
    }
}