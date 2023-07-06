package com.remi.alarmclock.xtreme.free.fragment.alarm

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import com.remi.alarmclock.xtreme.free.adapter.ViewPagerAddFragmentsAdapter
import com.remi.alarmclock.xtreme.free.addview.addalarm.ViewAudioRingtone
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.model.RingtoneModel
import java.io.IOException

class AudioRingtoneFragment(private val ringtone: RingtoneModel, callBack: ICallBackItem): BaseFragment() {

    private lateinit var vAudioRingtone: ViewAudioRingtone

    private val callBack: ICallBackItem
    private var mediaPlayer: MediaPlayer? = null
    private var oldPosition = -1
    private var isPlayAudio = false

    var audioRecordFrag : AudioRecordFragment? = null
    var isRecording = false

    companion object {
        fun newInstance(ringtone: RingtoneModel, callBack: ICallBackItem): AudioRingtoneFragment {
            val args = Bundle()

            val fragment = AudioRingtoneFragment(ringtone, callBack)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        this.callBack = callBack
    }
    override fun getViewLayout(): View {
        vAudioRingtone = ViewAudioRingtone(requireContext())
        return vAudioRingtone
    }

    override fun setUp() {
        setUpPager()

        evenClick()
    }

    private fun setUpPager() {
        val pagerAdapter = ViewPagerAddFragmentsAdapter(parentFragmentManager, lifecycle)

        val audioUnknownFrag = AudioUnknownFragment.newInstance(ringtone, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                val ringtone = ob as RingtoneModel
                if (isPlayAudio) {
                    handStop()
                    if (oldPosition != position) handPlay(ringtone)
                } else handPlay(ringtone)

                oldPosition = position
                callBack.callBack(ringtone, position)
            }
        })
        pagerAdapter.addFrag(audioUnknownFrag)
        audioRecordFrag = AudioRecordFragment.newInstance(ringtone, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                if (position == -2) handStop()
                else {
                    val ringtone = ob as RingtoneModel
                    if (position == -1) {
                        handStop()
                        callBack.callBack(RingtoneModel("default 1", "music", "default", true), position)
                    } else {
                        if (isPlayAudio) {
                            handStop()
                            if (oldPosition != position) handPlay(ringtone)
                        } else handPlay(ringtone)

                        oldPosition = position
                        callBack.callBack(ringtone, position)
                    }
                }
            }},
            object : ICallBackCheck{
            override fun check(isCheck: Boolean) {
                vAudioRingtone.vPager.isUserInputEnabled = isCheck
                isRecording = !isCheck
            }
        })
        pagerAdapter.addFrag(audioRecordFrag!!)

        vAudioRingtone.vPager.adapter = pagerAdapter
        vAudioRingtone.vPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                vAudioRingtone.swipeLayout(if (position == 0) "unknown" else "record")
                if (isPlayAudio) handStop()
            }
        })
    }

    private fun evenClick() {
        vAudioRingtone.tvUnknown.setOnClickListener {
            vAudioRingtone.swipeLayout("unknown")
            vAudioRingtone.vPager.setCurrentItem(0, true)
        }
        vAudioRingtone.tvRecord.setOnClickListener {
            vAudioRingtone.swipeLayout("record")
            vAudioRingtone.vPager.setCurrentItem(1, true)
        }
        vAudioRingtone.ivBack.setOnClickListener { callBack.callBack(-1, -1) }
    }

    fun handPlay(ringtone: RingtoneModel) {
        try {
            mediaPlayer = MediaPlayer().apply {
                if (ringtone.typeRing == "default") {
                    val descriptor = requireContext().assets.openFd("music/${ringtone.nameFile}.mp3")
                    setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
                } else setDataSource(ringtone.uri)
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

    fun handStop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.apply {
                    stop()
                    release()
                }
            }
            isPlayAudio = false
        }
    }

    override fun onDestroy() {
        if (isPlayAudio) handStop()
        super.onDestroy()
    }
}