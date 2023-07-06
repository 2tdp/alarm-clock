package com.remi.alarmclock.xtreme.free.data

import android.app.Activity
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.provider.MediaStore
import com.remi.alarmclock.xtreme.free.model.RingtoneModel

object DataRingtoneDefault {

    fun getListRingtones(activity: Activity): MutableList<RingtoneModel> {
        val lstRingtone = mutableListOf<RingtoneModel>()

        val f = activity.assets.list("music")
        for (s in f!!) {
            lstRingtone.add(RingtoneModel(s.replace(".mp3", ""), "music", "default", false))
        }

        lstRingtone.addAll(getListRingtonesDefault(activity))
        lstRingtone.reverse()

        return lstRingtone
    }

    private fun getListRingtonesDefault(activity: Activity): MutableList<RingtoneModel> {
        val lstRingtoneDefault = mutableListOf<RingtoneModel>()
        val ringtoneManager = RingtoneManager(activity).apply {
            setType(RingtoneManager.TYPE_RINGTONE)
        }
        val cursor = ringtoneManager.cursor
        while (cursor.moveToNext()) {
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val contentUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
            val id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
            lstRingtoneDefault.add(RingtoneModel(title, getRingtonePathFromContentUri(activity, Uri.parse("$contentUri/$id"))!!, "defaultDevice", false))
        }

        return lstRingtoneDefault
    }

    private fun getRingtonePathFromContentUri(context: Context, contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val ringtoneCursor =
            context.contentResolver.query(contentUri, proj, null, null, null)
        ringtoneCursor!!.moveToFirst()
        val path = ringtoneCursor.getString(ringtoneCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
        ringtoneCursor.close()
        return path
    }
}