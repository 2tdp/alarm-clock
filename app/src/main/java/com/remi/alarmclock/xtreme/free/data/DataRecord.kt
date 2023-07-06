package com.remi.alarmclock.xtreme.free.data

import android.content.Context
import com.remi.alarmclock.xtreme.free.model.RingtoneModel
import com.remi.alarmclock.xtreme.free.utils.Utils
import java.io.File
import java.util.*

object DataRecord {

    fun getRecordSave(context: Context, nameFolder: String): ArrayList<RingtoneModel> {
        val lstRecord = ArrayList<RingtoneModel>()

        val filesApp = getTotalFile(context, nameFolder)
        if (!filesApp.isNullOrEmpty()) {
            for (f in filesApp) {
                if (!f.name.contains(".mp3")) continue

                lstRecord.add(RingtoneModel(f.name, f.path, "record", false))
            }
        }

        return lstRecord
    }

    private fun getTotalFile(context: Context, nameFolder: String): Array<File>? {
        val directory = File(Utils.getStore(context) + "/" + nameFolder)
        return directory.listFiles()
    }
}