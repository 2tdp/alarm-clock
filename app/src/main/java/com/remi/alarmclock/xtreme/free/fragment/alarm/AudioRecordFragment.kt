package com.remi.alarmclock.xtreme.free.fragment.alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import com.remi.alarmclock.xtreme.free.adapter.RingtoneAdapter
import com.remi.alarmclock.xtreme.free.addview.addalarm.ViewAudioRecord
import com.remi.alarmclock.xtreme.free.addview.dialog.ViewDialogAudioPermission
import com.remi.alarmclock.xtreme.free.addview.dialog.ViewDialogSaveFileRecord
import com.remi.alarmclock.xtreme.free.callback.ICallBack
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.data.DataRecord
import com.remi.alarmclock.xtreme.free.helpers.FOLDER_RECORD
import com.remi.alarmclock.xtreme.free.helpers.NUMB_RECORD
import com.remi.alarmclock.xtreme.free.helpers.RECORD_FILE
import com.remi.alarmclock.xtreme.free.model.RingtoneModel
import com.remi.alarmclock.xtreme.free.recorder.CounterTimer
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager
import com.remi.alarmclock.xtreme.free.utils.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream

class AudioRecordFragment(var ringtone: RingtoneModel, callBack: ICallBackItem, isSwipe: ICallBackCheck) : BaseFragment() {

    private lateinit var vAudioRecord: ViewAudioRecord
    private var w = 0F

    private val callBack: ICallBackItem
    private val isSwipe: ICallBackCheck

    private var ringtoneAdapter: RingtoneAdapter? = null
    private var lstRecord: ArrayList<RingtoneModel>

    private lateinit var mediaRecord: MediaRecorder
    private lateinit var timer: CounterTimer
    private var isRecording = false
    private var isPaused = false
    private var dirPath = ""
    private var filename = ""
    private lateinit var amplitudes: ArrayList<Float>

    companion object {
        fun newInstance(ringtone: RingtoneModel, callBack: ICallBackItem, isSwipe: ICallBackCheck): AudioRecordFragment {
            val args = Bundle()

            val fragment = AudioRecordFragment(ringtone, callBack, isSwipe)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        this.isSwipe = isSwipe
        this.callBack = callBack
        this.lstRecord = ArrayList()
    }

    private val handler = Handler(Looper.getMainLooper()) {
        val lst = it.obj as ArrayList<RingtoneModel>
        if (lst.isNotEmpty()) {
            ringtoneAdapter?.setData(lst)
            vAudioRecord.tvNoFile.visibility = View.GONE
            vAudioRecord.tvAnim.visibility = View.GONE
            vAudioRecord.rcvAudio.visibility = View.VISIBLE
        } else {
            vAudioRecord.tvNoFile.visibility = View.VISIBLE
            vAudioRecord.tvAnim.visibility = View.VISIBLE
            vAudioRecord.rcvAudio.visibility = View.GONE
        }
        true
    }

    override fun getViewLayout(): View {
        w = resources.displayMetrics.widthPixels / 100F
        vAudioRecord = ViewAudioRecord(requireContext())
        return vAudioRecord
    }

    override fun setUp() {
        Thread {
            lstRecord = DataRecord.getRecordSave(requireContext(), FOLDER_RECORD)
            val item = lstRecord.filter { it.nameFile == ringtone.nameFile }
            if (item.isNotEmpty()) item[0].isChoose = true
            handler.sendMessage(Message().apply {
                obj = lstRecord
                what = 0
            })
        }.start()
        Utils.makeFolder(requireContext(), FOLDER_RECORD)
        timer = CounterTimer(true, object : CounterTimer.OnTimerTickListener {
            override fun onTimerTick(duration: Array<String>) {
                vAudioRecord.tvDuration.text =
                    if (duration.size == 4) "${duration[3]}.${duration[2]}.${duration[1]},${duration[0]}"
                    else "${duration[2]}.${duration[1]},${duration[0]}"
                vAudioRecord.waveformView.addAmplitude(mediaRecord.maxAmplitude.toFloat())
            }
        })
        evenClick()
        setUpData()
    }

    private fun evenClick() {
        vAudioRecord.ivAddRecord.setOnClickListener {
            callBack.callBack(-2, -2)
            startRecord()
        }
        vAudioRecord.rlOutRecord.setOnClickListener { stopRecorder() }
        vAudioRecord.ivStopRecord.setOnClickListener { stopRecorder() }
    }

    private fun setUpData() {
        ringtoneAdapter = RingtoneAdapter(requireContext(), object : ICallBack {
            override fun callback(ob: Any, position: Int, check: Boolean) {
                val record = ob as RingtoneModel
                if (check) {
                    delFileRecord(record)
                    callBack.callBack(record, -1)
                } else callBack.callBack(record, position)
            }
        })

        if (lstRecord.isNotEmpty()) ringtoneAdapter?.setData(lstRecord)

        vAudioRecord.rcvAudio.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        vAudioRecord.rcvAudio.adapter = ringtoneAdapter
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun startRecord() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            showDialogPermission()
        else {
            isSwipe.check(false)
            vAudioRecord.bottomSheetRecord("show", requireContext())
            filename = getANumb()
            vAudioRecord.tvNameRecord.text = filename

            mediaRecord = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(requireContext())
            } else MediaRecorder()

            dirPath = Utils.getStore(requireContext()) + "/$FOLDER_RECORD/"

            mediaRecord.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile("$dirPath$filename.mp3")
                try {
                    prepare()
                } catch (e: IOException) {
                    e.printStackTrace()
                    showMessage(resources.getString(R.string.err_record))
                }
                start()
            }

            isRecording = true
            isPaused = false

            timer.start()
        }
    }

    fun stopRecorder() {
        timer.stop()

        mediaRecord.apply {
            stop()
            reset()
            release()
        }

        isPaused = false
        isRecording = false

        vAudioRecord.tvDuration.text = resources.getString(R.string.duration)
        amplitudes = vAudioRecord.waveformView.clear()

        showDialogSave()
    }

    private fun showDialogSave() {
        val vDialogSave = ViewDialogSaveFileRecord(requireContext())
        vDialogSave.edtNameFile.setText(filename)
        val dialog = AlertDialog.Builder(requireContext(), R.style.SheetDialog).create()
        dialog.apply {
            setView(vDialogSave)
            setCancelable(false)
            show()
        }
        vDialogSave.layoutParams.width = (77.778f * w).toInt()
        vDialogSave.layoutParams.height = (52.222f * w).toInt()

        vDialogSave.tvSave.setOnClickListener {
            saveRecord(vDialogSave.edtNameFile.text.toString())
            dialog.cancel()
        }
        vDialogSave.tvCancel.setOnClickListener {
            saveRecord("")
            dialog.cancel()
        }
    }

    private fun saveRecord(nameFile: String) {
        Thread {
            if (nameFile != filename && nameFile != "") {
                val newFile = File("$dirPath$nameFile.mp3")
                File("$dirPath$filename.mp3").renameTo(newFile)
                addNumbRecord(filename)
            }

            val ampsPath =  if (nameFile != "") "$dirPath$nameFile"
            else "$dirPath$filename"
            try {
                val fos = FileOutputStream(ampsPath)
                val out = ObjectOutputStream(fos)
                out.writeObject(amplitudes)
                fos.close()
                out.close()

                lstRecord = DataRecord.getRecordSave(requireContext(), FOLDER_RECORD)
                handler.sendMessage(Message().apply {
                    obj = lstRecord
                    what = 0
                })
                Handler(Looper.getMainLooper()).post {
                    isSwipe.check(true)
                    vAudioRecord.bottomSheetRecord("hide", requireContext())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun delFileRecord(record: RingtoneModel) {
        val file = File(record.uri)
        if (file.exists()) {
            if (file.delete()) {
                showMessage(getString(R.string.done))
                addNumbRecord(record.nameFile)
                lstRecord = DataRecord.getRecordSave(requireContext(), FOLDER_RECORD)
                handler.sendMessage(Message().apply {
                    obj = lstRecord
                    what = 0
                })
            }
            else showMessage(getString(R.string.cant_del))
        } else showMessage(getString(R.string.cant_del))
    }

    private fun showDialogPermission() {
        val vCheckPer = ViewDialogAudioPermission(requireContext())
        val dialog = AlertDialog.Builder(requireContext(), R.style.SheetDialog).create()
        dialog.apply {
            setView(vCheckPer)
            setCancelable(false)
            show()
        }

        vCheckPer.layoutParams.width = (77.778f * w).toInt()
        vCheckPer.layoutParams.height = (52.222f * w).toInt()

        vCheckPer.tvAllow.setOnClickListener {
            dialog.cancel()
            Dexter.withContext(requireContext())
                .withPermission("android.permission.RECORD_AUDIO")
                .withListener(object : PermissionListener{
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        startRecord()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        showMessage(resources.getString(R.string.per_record))
                        openSettingPermission()
                    }

                    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken) {
                        p1.continuePermissionRequest()
                    }

                }).check()
        }
        vCheckPer.tvCancel.setOnClickListener {
            showMessage(resources.getString(R.string.per_record))
            dialog.cancel()
        }
    }

    private fun addNumbRecord(nameFile: String){
        val arrStr = DataLocalManager.getArrStr(NUMB_RECORD)
        var check = false
        for (i in arrStr) {
            if (nameFile == i) {
                check = true
                break
            }
        }
        if (!check) {
            arrStr.add(nameFile.replace(".mp3", ""))
            arrStr.sort()
            DataLocalManager.setArrStr(arrStr, NUMB_RECORD)
        }
    }

    private fun getANumb(): String{
        val arrStr = DataLocalManager.getArrStr(NUMB_RECORD)
        if (arrStr.isEmpty()) {
            var max = 0
            for (i in lstRecord.indices) {
                try {
                    val tmp = lstRecord[i].nameFile.replace(RECORD_FILE, "").replace(".mp3", "").toInt()
                    if (tmp > max) max = tmp
                } catch (_: Exception) { }
            }
            return if (max < lstRecord.size) RECORD_FILE + (max + 1)
            else RECORD_FILE + lstRecord.size
        }
        val numb = arrStr[0]
        arrStr.remove(numb)
        DataLocalManager.setArrStr(arrStr, NUMB_RECORD)
        return numb
    }

    private fun openSettingPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}