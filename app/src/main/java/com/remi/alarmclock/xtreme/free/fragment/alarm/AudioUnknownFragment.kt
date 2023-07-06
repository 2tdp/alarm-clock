package com.remi.alarmclock.xtreme.free.fragment.alarm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import com.remi.alarmclock.xtreme.free.adapter.RingtoneAdapter
import com.remi.alarmclock.xtreme.free.addview.addalarm.ViewAudioUnknown
import com.remi.alarmclock.xtreme.free.callback.ICallBack
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.data.DataRingtoneDefault
import com.remi.alarmclock.xtreme.free.model.RingtoneModel

class AudioUnknownFragment(private val ringtoneModel: RingtoneModel, val callback: ICallBackItem): BaseFragment() {

    private lateinit var vAudioUnknown: ViewAudioUnknown

    private var ringtoneAdapter: RingtoneAdapter? = null
    private var lstRingtone = mutableListOf<RingtoneModel>()

    companion object {
        fun newInstance(ringtoneModel: RingtoneModel, callback: ICallBackItem): AudioUnknownFragment{
            val args = Bundle()

            val fragment = AudioUnknownFragment(ringtoneModel, callback)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewLayout(): View {
        vAudioUnknown = ViewAudioUnknown(requireContext())

        return vAudioUnknown
    }

    private val handler = Handler(Looper.getMainLooper()) {
        val lst = it.obj as ArrayList<RingtoneModel>
        if (lst.isNotEmpty())
            ringtoneAdapter?.setData(lst)
        true
    }

    override fun setUp() {
        Thread {
            lstRingtone = DataRingtoneDefault.getListRingtones(requireActivity())
            val item = lstRingtone.filter { it.nameFile == ringtoneModel.nameFile }
            if (item.isNotEmpty()) item[0].isChoose = true
            handler.sendMessage(Message().apply {
                obj = lstRingtone
                what = 0
            })
        }.start()

        ringtoneAdapter = RingtoneAdapter(requireContext(), object : ICallBack {
            override fun callback(ob: Any, position: Int, check: Boolean) {
                callback.callBack(ob, position)
            }
        })

        vAudioUnknown.rcvRingtones.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        vAudioUnknown.rcvRingtones.adapter = ringtoneAdapter
    }
}