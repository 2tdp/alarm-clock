package com.remi.alarmclock.xtreme.free.fragment

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import com.remi.alarmclock.xtreme.free.adapter.StopwatchAdapter
import com.remi.alarmclock.xtreme.free.addview.home.ViewHomeStopwatch
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.model.StopwatchModel
import com.remi.alarmclock.xtreme.free.service.StopwatchService

class StopwatchFragment : BaseFragment() {

    private lateinit var vStopwatch: ViewHomeStopwatch

    private var stopwatchAdapter: StopwatchAdapter? = null
    private val lstStopwatch = mutableListOf<StopwatchModel>()
    private var count = 0

    private var serviceStopwatch: StopwatchService? = null
    private lateinit var intentService: Intent
    private var isServiceRunning = false

    companion object {
        fun newInstance(): StopwatchFragment {
            val args = Bundle()

            val fragment = StopwatchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewLayout(): View {
        isServiceRunning = StopwatchService.IS_SERVICE_RUNNING
        vStopwatch = ViewHomeStopwatch(requireContext())
        return vStopwatch
    }

    override fun setUp() {
        evenClick()

        intentService = Intent(requireContext(), StopwatchService::class.java)

        if (isServiceRunning) {
            vStopwatch.actionStopwatch(ACTION_START_STOPWATCH)
            requireContext().bindService(intentService, connectService, Context.BIND_AUTO_CREATE)
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, IntentFilter(TAG_STOPWATCH))

        stopwatchAdapter = StopwatchAdapter(requireContext())

        vStopwatch.rcvStopwatch.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        vStopwatch.rcvStopwatch.adapter = stopwatchAdapter
    }

    private fun evenClick() {
        vStopwatch.tvRight.setOnClickListener {
            if (vStopwatch.tvRight.text == resources.getString(R.string.start)) {
                isServiceRunning = StopwatchService.IS_SERVICE_RUNNING
                if (!isServiceRunning) {
                    requireContext().startService(intentService)
                    requireContext().bindService(intentService, connectService, Context.BIND_AUTO_CREATE)
                    vStopwatch.actionStopwatch(ACTION_START_STOPWATCH)
                } else serviceStopwatch?.startCounterTime()
            } else if (vStopwatch.tvRight.text == resources.getString(R.string.pause)) {
                serviceStopwatch?.pauseCounterTime()
                vStopwatch.actionStopwatch(ACTION_PAUSE_STOPWATCH)
            }
        }

        vStopwatch.tvLeft.setOnClickListener {
            if (vStopwatch.tvLeft.text == resources.getString(R.string.lap)) serviceStopwatch?.addLap()
            else if (vStopwatch.tvLeft.text == resources.getString(R.string.reset)) {
                serviceStopwatch?.stopCounterTime()
                stopCounterTime()
            }
            stopwatchAdapter?.setData(lstStopwatch)
        }
    }

    private fun startCounterTime(ob: Any) {
        val duration = ob as Array<*>
        if (duration.size == 4) {
            vStopwatch.tvHour.apply {
                visibility = View.VISIBLE
                text = duration[3].toString()
            }
            vStopwatch.tvDot1.visibility - View.VISIBLE
        } else {
            vStopwatch.tvHour.visibility = View.GONE
            vStopwatch.tvDot1.visibility - View.GONE
        }
        vStopwatch.tvMinute.text = duration[2].toString()
        vStopwatch.tvSecond.text = duration[1].toString()
        vStopwatch.tvMills.text = duration[0].toString()
    }

    private fun stopCounterTime() {
        count = 0
        lstStopwatch.clear()
        vStopwatch.actionStopwatch(ACTION_RESET_STOPWATCH)
        requireContext().unbindService(connectService)
        isServiceRunning = StopwatchService.IS_SERVICE_RUNNING
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            when(intent.getStringExtra(TYPE_NOTIFY_ID)) {
                NOTIFY_ID_START -> {
                    startCounterTime(intent.getStringArrayExtra(NOTIFY_ID_START)!!)
                    vStopwatch.actionStopwatch(ACTION_START_STOPWATCH)
                }
                NOTIFY_ID_PAUSE -> vStopwatch.actionStopwatch(ACTION_PAUSE_STOPWATCH)
                NOTIFY_ID_RESET -> {
                    stopCounterTime()
                    Handler(Looper.getMainLooper()).post { stopwatchAdapter?.setData(lstStopwatch) }
                }
                NOTIFY_ID_LAP -> {
                    lstStopwatch.clear()
                    lstStopwatch.addAll(serviceStopwatch?.lstLap!!)
                    Handler(Looper.getMainLooper()).post { stopwatchAdapter?.setData(lstStopwatch) }
                }
            }
        }

    }

    private val connectService = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as StopwatchService.LocalBinder
            serviceStopwatch = binder.getServiceInstance()
            serviceStopwatch?.startCounterTime()
            lstStopwatch.clear()
            lstStopwatch.addAll(serviceStopwatch?.lstLap!!)
            Handler(Looper.getMainLooper()).post { stopwatchAdapter?.setData(lstStopwatch) }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {

        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}