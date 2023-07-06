package com.remi.alarmclock.xtreme.free.activity

import android.annotation.SuppressLint
import android.app.*
import android.graphics.Color
import android.text.format.DateFormat
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.google.gson.Gson
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.base.BaseActivity
import com.remi.alarmclock.xtreme.free.addview.addalarm.ViewAddAlarm
import com.remi.alarmclock.xtreme.free.addview.dialog.ViewDialogPickSnooze
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.fragment.alarm.AudioRingtoneFragment
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.model.AlarmModel
import com.remi.alarmclock.xtreme.free.model.RingtoneModel
import com.remi.alarmclock.xtreme.free.utils.Utils
import com.remi.alarmclock.xtreme.free.utils.UtilsTimer
import com.remi.alarmclock.xtreme.free.viewcustom.OnSeekbarResult
import org.json.JSONObject
import java.util.*
import kotlin.math.abs

@SuppressLint("SetTextI18n")
class AlarmActivity : BaseActivity(Color.parseColor("#181818"), Color.parseColor("#181818")) {

    private lateinit var vAddAlarm: ViewAddAlarm
    private var w = 0F

    private var audioRingtoneFrag: AudioRingtoneFragment? = null

    private lateinit var alarmModel: AlarmModel
    private lateinit var ringtoneModel: RingtoneModel
    private lateinit var calendar: Calendar

    override fun getViewLayout(): View {
        w = resources.displayMetrics.widthPixels / 100F
        vAddAlarm = ViewAddAlarm(this@AlarmActivity, object : ICallBackCheck {
            override fun check(isCheck: Boolean) {
                vAddAlarm.isEnableScroll = !isCheck
            }
        })
        return vAddAlarm
    }

    override fun setUp() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (audioRingtoneFrag != null)
                    audioRingtoneFrag?.let {
                        if (it.isRecording) it.audioRecordFrag?.stopRecorder()
                        supportFragmentManager.popBackStack("AudioRingtoneFragment", -1)
                        audioRingtoneFrag = null
                    }
                else finish()
            }
        })

        calendar = Calendar.getInstance(Locale.getDefault())
        if (intent.getStringExtra(ALARM_PICKED) != null) {
            intent.getStringExtra(ALARM_PICKED)?.let {
                val alarm = Gson().fromJson(JSONObject(it).toString(), AlarmModel::class.java)
                if (alarm != null) {
                    this.alarmModel = alarm
                    this.ringtoneModel = alarm.ringtone!!
                }
            }
        } else {
            ringtoneModel = RingtoneModel("default 1", "music", "default", true)
            alarmModel = AlarmModel(
                -1,
                "",
                DateFormat.format("hh", calendar.timeInMillis).toString().toInt(),
                DateFormat.format("mm", calendar.timeInMillis).toString().toInt(),
                if (calendar[Calendar.AM_PM] == 1) 1 else 0,
                mutableListOf(UtilsTimer.getDayInWeek()),
                ringtoneModel,
                50,
                false,
                10,
                true,
                true
            )
        }
        evenClick()
        setUpView()
    }

    private fun setUpView() {
        vAddAlarm.apply {
            vHourAlarm.setHour(alarmModel.hourAlarm)
            vMinuteAlarm.setMinutes(alarmModel.minuteAlarm)
            edtName.setText(alarmModel.name)
            setRepeat(alarmModel.repeat)
            vRingtone.tvChoose.text = ringtoneModel.nameFile
            sbVolume.setProgress(alarmModel.volume)
            tvLvVolume.text = "${alarmModel.volume}%"
            swVibrate.isChecked = alarmModel.isVibrate
            vSnooze.tvChoose.text = "${alarmModel.snooze} minutes"
            swTypeTime.isChecked = alarmModel.isTypeTime != 0
        }
    }

    private fun evenClick() {
        vAddAlarm.tvSave.setOnClickListener { saveAlarm() }
        vAddAlarm.tvCancel.setOnClickListener { onBackPressed(false) }

        vAddAlarm.vRingtone.setOnClickListener {
            audioRingtoneFrag =
                AudioRingtoneFragment.newInstance(ringtoneModel, object : ICallBackItem {
                    override fun callBack(ob: Any, position: Int) {
                        if (position != -1) {
                            val record = ob as RingtoneModel
                            vAddAlarm.vRingtone.tvChoose.text = record.nameFile
                            ringtoneModel = record
                        } else
                            audioRingtoneFrag?.let {
                                if (it.isRecording) it.audioRecordFrag?.stopRecorder()
                                supportFragmentManager.popBackStack("AudioRingtoneFragment", -1)
                                audioRingtoneFrag = null
                            }
                    }
                })
            replaceFragment(supportFragmentManager, audioRingtoneFrag!!, true, true)
        }
        vAddAlarm.sbVolume.onSeekbarResult = object : OnSeekbarResult {
            override fun onDown(v: View) {

            }

            override fun onMove(v: View, value: Int) {
                vAddAlarm.tvLvVolume.text = "$value%"
            }

            override fun onUp(v: View, value: Int) {

            }

        }
        vAddAlarm.vSnooze.setOnClickListener { showDialogPickSnooze() }
        vAddAlarm.rlVibrate.setOnClickListener {
            Utils.makeVibrate(this@AlarmActivity)
            vAddAlarm.swVibrate.isChecked = !vAddAlarm.swVibrate.isChecked
        }
        vAddAlarm.swVibrate.setOnClickListener { Utils.makeVibrate(this@AlarmActivity) }

        vAddAlarm.tvMo.setOnClickListener { vAddAlarm.chooseRepeat("mo") }
        vAddAlarm.tvTu.setOnClickListener { vAddAlarm.chooseRepeat("tu") }
        vAddAlarm.tvWe.setOnClickListener { vAddAlarm.chooseRepeat("we") }
        vAddAlarm.tvTh.setOnClickListener { vAddAlarm.chooseRepeat("th") }
        vAddAlarm.tvFr.setOnClickListener { vAddAlarm.chooseRepeat("fr") }
        vAddAlarm.tvSa.setOnClickListener { vAddAlarm.chooseRepeat("sa") }
        vAddAlarm.tvSu.setOnClickListener { vAddAlarm.chooseRepeat("su") }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogPickSnooze() {
        val vPickSnooze = ViewDialogPickSnooze(this@AlarmActivity)
        val dialog = AlertDialog.Builder(this@AlarmActivity, R.style.SheetDialog).create()
        dialog.apply {
            setView(vPickSnooze)
            setCancelable(true)
            show()
        }

        vPickSnooze.layoutParams.width = (77.778f * w).toInt()
        vPickSnooze.layoutParams.height = (85.556f * w).toInt()

        vPickSnooze.tvOk.setOnClickListener {
            dialog.cancel()
            vAddAlarm.vSnooze.tvChoose.text = "${vPickSnooze.numbPicker.value} minutes"
        }
    }

    private fun saveAlarm() {
        alarmModel.apply {
            if (id == -1) id = abs(Random().nextInt())
            name = vAddAlarm.edtName.text.toString()
            hourAlarm = if (!vAddAlarm.swTypeTime.isChecked) {
                if (vAddAlarm.vHourAlarm.textHour.toInt() == 12) 0
                else vAddAlarm.vHourAlarm.textHour.toInt()
            } else {
                if (vAddAlarm.vHourAlarm.textHour.toInt() < 12)
                    vAddAlarm.vHourAlarm.textHour.toInt() + 12
                else vAddAlarm.vHourAlarm.textHour.toInt()
            }
            minuteAlarm = vAddAlarm.vHourAlarm.textMinutes.toInt()
            isTypeTime = if (!vAddAlarm.swTypeTime.isChecked) 0 else 1
            repeat = vAddAlarm.getRepeat()
            this.ringtone = ringtoneModel
            volume = vAddAlarm.sbVolume.getProgress()
            isVibrate = vAddAlarm.swVibrate.isChecked
            snooze = vAddAlarm.vSnooze.tvChoose.text.toString().replace(" minutes", "").toInt()
            isRun = true
            isBefore = true
        }

        UtilsTimer.sendAlarm(this, alarmModel)

        onBackPressed(false)
    }
}