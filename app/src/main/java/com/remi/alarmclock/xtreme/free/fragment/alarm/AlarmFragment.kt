package com.remi.alarmclock.xtreme.free.fragment.alarm

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.AlarmActivity
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import com.remi.alarmclock.xtreme.free.adapter.AlarmAdapter
import com.remi.alarmclock.xtreme.free.addview.dialog.ViewDialogAudioPermission
import com.remi.alarmclock.xtreme.free.addview.home.ViewHomeAlarm
import com.remi.alarmclock.xtreme.free.callback.ICallBack
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.model.AlarmModel
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager
import com.remi.alarmclock.xtreme.free.utils.UtilsTimer

class AlarmFragment(goNavi: ICallBackCheck) : BaseFragment() {

    private var vAlarm: ViewHomeAlarm? = null
    private var w = 0F

    private val goNavi: ICallBackCheck
    private var alarmAdapter: AlarmAdapter? = null

    companion object {
        fun newInstance(goNavi: ICallBackCheck): AlarmFragment {
            val args = Bundle()

            val fragment = AlarmFragment(goNavi)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        this.goNavi = goNavi
    }

    override fun getViewLayout(): View {
        vAlarm = ViewHomeAlarm(requireContext())
        w = resources.displayMetrics.widthPixels / 100F
        return vAlarm!!
    }

    override fun setUp() {
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, IntentFilter(RELOAD_DATA_ALARM))
        setUpView()
        evenClick()
    }

    private fun evenClick() {
        vAlarm?.vToolbar?.ivNavi?.setOnClickListener { goNavi.check(true) }
        vAlarm?.vToolbar?.ivAdd?.setOnClickListener {
            startIntent(AlarmActivity::class.java.name, false)
        }
    }

    private fun setUpView() {
        alarmAdapter = AlarmAdapter(requireContext(), object : ICallBack {
            override fun callback(ob: Any, position: Int, check: Boolean) {
                val alarm = ob as AlarmModel
                if (position == -1) showDialogDelAlarm(alarm)
                else if (position == -2) {
                    if (check) {
                        alarm.isRun = true
                        enableAlarmInDb(true, alarm)
                        UtilsTimer.sendAlarm(requireContext(), alarm)
                    } else {
                        enableAlarmInDb(false, alarm)
                        UtilsTimer.cancelNotification(requireContext(), alarm.id)
                        LocalBroadcastManager.getInstance(requireContext())
                            .sendBroadcast(Intent().apply {
                                action = TAG_FRAG_ALARM
                            })
                    }
                } else {
                    val intent = Intent(context, AlarmActivity::class.java).apply {
                        putExtra(ALARM_PICKED, Gson().toJsonTree(alarm).asJsonObject.toString())
                    }
                    startActivity(intent)
                }
            }
        })

        reloadData()
        vAlarm?.rcvAlarm?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        vAlarm?.rcvAlarm?.adapter = alarmAdapter
    }

    private fun showDialogDelAlarm(alarm: AlarmModel) {
        val vDel = ViewDialogAudioPermission(requireContext())
        vDel.apply {
            tvTitle.text = getString(R.string.del)
            tvDes.text = getString(R.string.are_you_sure_you_want_to_delete_the_alarm)
        }
        val dialog = AlertDialog.Builder(requireContext(), R.style.SheetDialog).create()
        dialog.apply {
            setView(vDel)
            setCancelable(true)
            show()
        }

        vDel.layoutParams.width = (77.778f * w).toInt()
        vDel.layoutParams.height = (52.22 * w).toInt()

        vDel.tvAllow.setOnClickListener {
            UtilsTimer.cancelNotification(requireContext(), alarm.id)
            delAlarmInDb(alarm)
            dialog.cancel()
        }
        vDel.tvCancel.setOnClickListener { dialog.cancel() }
    }

    private fun delAlarmInDb(alarm: AlarmModel) {
        val lstAlarm = DataLocalManager.getListAlarm(LIST_ALARM)
        val delAlarm = lstAlarm.filter { it.id == alarm.id }
        lstAlarm.remove(delAlarm[0])
        DataLocalManager.setListAlarm(lstAlarm, LIST_ALARM)
        reloadData()
    }

    private fun enableAlarmInDb(isRun: Boolean, alarm: AlarmModel) {
        val lstAlarm = DataLocalManager.getListAlarm(LIST_ALARM)
        val lstTmp = lstAlarm.filter { it.id == alarm.id }
        lstTmp[0].isRun = isRun
        DataLocalManager.setListAlarm(lstAlarm, LIST_ALARM)
    }

    fun reloadData() {
        val lstAlarm = DataLocalManager.getListAlarm(LIST_ALARM)
        if (lstAlarm.isNotEmpty()) {
            alarmAdapter?.setData(lstAlarm)
            vAlarm?.rcvAlarm?.visibility = View.VISIBLE
            vAlarm?.rlNoData?.visibility = View.GONE
        } else {
            vAlarm?.rcvAlarm?.visibility = View.GONE
            vAlarm?.rlNoData?.visibility = View.VISIBLE
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            reloadData()
        }
    }
}