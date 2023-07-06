package com.remi.alarmclock.xtreme.free.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.remi.alarmclock.xtreme.free.helpers.ALARM_NOTIFY
import com.remi.alarmclock.xtreme.free.helpers.ID_NOTIFY
import com.remi.alarmclock.xtreme.free.helpers.LIST_ALARM
import com.remi.alarmclock.xtreme.free.helpers.TAG
import com.remi.alarmclock.xtreme.free.helpers.TIME_BEFORE
import com.remi.alarmclock.xtreme.free.model.AlarmModel
import com.remi.alarmclock.xtreme.free.service.AlarmService
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

object UtilsTimer {

    val arrDate = arrayOf("mo", "tu", "we", "th", "fr", "sa", "su")

    fun getTimeCurrent(): Long {
        return System.currentTimeMillis()
    }

    fun getDayInWeek(): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = getTimeCurrent()
        return when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> "mo"
            Calendar.TUESDAY -> "tu"
            Calendar.WEDNESDAY -> "we"
            Calendar.THURSDAY -> "th"
            Calendar.FRIDAY -> "fr"
            Calendar.SATURDAY -> "sa"
            Calendar.SUNDAY -> "su"
            else -> ""
        }
    }

    fun getDayInWeek(diw: String): Int {
        return when (diw) {
            "mo" -> Calendar.MONDAY
            "tu" -> Calendar.TUESDAY
            "we" -> Calendar.WEDNESDAY
            "th" -> Calendar.THURSDAY
            "fr" -> Calendar.FRIDAY
            "sa" -> Calendar.SATURDAY
            "su" -> Calendar.SUNDAY
            else -> -1
        }
    }

    fun getDayInWeek(diw: Int): String {
        return when (diw) {
            Calendar.MONDAY -> "mo"
            Calendar.TUESDAY -> "tu"
            Calendar.WEDNESDAY -> "we"
            Calendar.THURSDAY -> "th"
            Calendar.FRIDAY -> "fr"
            Calendar.SATURDAY -> "sa"
            Calendar.SUNDAY -> "su"
            else -> ""
        }
    }

    fun getDayCurrentInWeek(): Int {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = getTimeCurrent()
        return calendar[Calendar.DAY_OF_WEEK]
    }

    fun getTriggerTime(hour: String, minute: String, isAm: Boolean, calendar: Calendar): Long {
        val currentHour: Int
        val currentMinute: Int
        var h = ""
        val time = if (hour == "") {
            currentMinute = DateFormat.format("mm", calendar.timeInMillis).toString().toInt()
            currentMinute - minute.toInt()
        } else {
            currentHour = DateFormat.format("HH", calendar.timeInMillis).toString().toInt()
            currentMinute = DateFormat.format("mm", calendar.timeInMillis).toString().toInt()
            h = if (isAm) (hour.toInt() + 12).toString()
            else hour
            if (minute == "") (currentHour - h.toInt()) * 60
            else abs((h.toInt() - currentHour) * 60 + (minute.toInt() - currentMinute))
        }
        return calendar.timeInMillis + time * 60000
    }

    fun getTriggerTime(time: Long, calendar: Calendar): Long {
        return time - calendar.timeInMillis
    }

    fun convertTimeToTimeStamp(dayInWeek: Int, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance(Locale.getDefault()).apply {
            timeInMillis = getTimeCurrent()
            set(Calendar.DAY_OF_WEEK, dayInWeek)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        return calendar.timeInMillis
    }

    fun convertTimeStampToTime(timestamp: Long): String {
        return DateFormat.format("EEE HH:mm", timestamp).toString()
    }

    fun cancelNotification(context: Context, idNotify: Int) {
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getService(
            context, idNotify, Intent(context, AlarmService::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }

    fun sendAlarm(context: Context, alarm: AlarmModel) {

        if (alarm.repeat.isNotEmpty()) {
            var isCreated = false
            for (date in alarm.repeat) {
                val dateInWeek = getDayInWeek(date)

                if (alarm.repeat.size == 1) {
                    if (dateInWeek < getDayCurrentInWeek()) {
                        val timeAlarm =
                            convertTimeToTimeStamp(dateInWeek, alarm.hourAlarm, alarm.minuteAlarm)
                        createAlarm(context, alarm, 7 * 24 * 60 * 60 * 1000 + timeAlarm)
                        Log.d(TAG, "sendAlarm1: date $date - ${7 * 24 * 60 * 60 * 1000 + timeAlarm}")
                        break
                    } else if (dateInWeek > getDayCurrentInWeek()) {
                        val timeAlarm =
                            convertTimeToTimeStamp(dateInWeek, alarm.hourAlarm, alarm.minuteAlarm)
                        createAlarm(context, alarm, timeAlarm)
                        Log.d(TAG, "sendAlarm2: date $date - $timeAlarm")
                        break
                    } else {
                        val t =
                            convertTimeToTimeStamp(dateInWeek, alarm.hourAlarm, alarm.minuteAlarm)
                        if (t - getTimeCurrent() <= 0) {
                            val timeAlarm =
                                convertTimeToTimeStamp(
                                    dateInWeek,
                                    alarm.hourAlarm,
                                    alarm.minuteAlarm
                                )
                            createAlarm(context, alarm, 7 * 24 * 60 * 60 * 1000 + timeAlarm)
                            Log.d(
                                TAG,
                                "sendAlarm3: date $date - ${7 * 24 * 60 * 60 * 1000 + timeAlarm}"
                            )
                            break
                        } else {
                            val timeAlarm = convertTimeToTimeStamp(
                                dateInWeek,
                                alarm.hourAlarm,
                                alarm.minuteAlarm
                            )
                            createAlarm(context, alarm, timeAlarm)
                            Log.d(TAG, "sendAlarm4: date $date - $timeAlarm")
                            break
                        }
                    }
                } else {
                    if (dateInWeek < getDayCurrentInWeek()) {
                        if (alarm.repeat.indexOf(date) < alarm.repeat.size - 1) continue
                        else isCreated = true
                    } else if (dateInWeek == getDayCurrentInWeek()) {
                        val t =
                            convertTimeToTimeStamp(dateInWeek, alarm.hourAlarm, alarm.minuteAlarm)
                        if (date == alarm.repeat[alarm.repeat.size - 1]) {
                            if (t - getTimeCurrent() <= 0) {
                                val timeAlarm =
                                    convertTimeToTimeStamp(
                                        getDayInWeek(alarm.repeat[0]),
                                        alarm.hourAlarm,
                                        alarm.minuteAlarm
                                    )
                                createAlarm(context, alarm, 7 * 24 * 60 * 60 * 1000 + timeAlarm)
                                Log.d(
                                    TAG,
                                    "sendAlarm5: date $date - ${7 * 24 * 60 * 60 * 1000 + timeAlarm}"
                                )
                                break
                            } else {
                                val timeAlarm = convertTimeToTimeStamp(
                                    dateInWeek,
                                    alarm.hourAlarm,
                                    alarm.minuteAlarm
                                )
                                createAlarm(context, alarm, timeAlarm)
                                Log.d(TAG, "sendAlarm6: date $date - $timeAlarm")
                                break
                            }
                        } else {
                            if (t - getTimeCurrent() <= 0) continue
                            else {
                                val timeAlarm =
                                    convertTimeToTimeStamp(
                                        dateInWeek,
                                        alarm.hourAlarm,
                                        alarm.minuteAlarm
                                    )
                                createAlarm(context, alarm, timeAlarm)
                                Log.d(TAG, "sendAlarm7: date $date - $timeAlarm")
                                break
                            }
                        }
                    } else {
                        if (dateInWeek > getDayCurrentInWeek()) {
                            val timeAlarm =
                                convertTimeToTimeStamp(dateInWeek, alarm.hourAlarm, alarm.minuteAlarm)
                            createAlarm(context, alarm, timeAlarm)
                            Log.d(TAG, "sendAlarm8: date $date - $timeAlarm - $dateInWeek")
                            break
                        } else isCreated = true
                    }
                }
            }
            if (isCreated) {
                val timeAlarm =
                    convertTimeToTimeStamp(
                        getDayInWeek(alarm.repeat[0]),
                        alarm.hourAlarm,
                        alarm.minuteAlarm
                    )
                createAlarm(context, alarm, 7 * 24 * 60 * 60 * 1000 + timeAlarm)
                Log.d(
                    TAG,
                    "sendAlarm9: date ${alarm.repeat[0]} - ${7 * 24 * 60 * 60 * 1000 + timeAlarm}"
                )
            }
            if (alarm.isBefore) addAlarmToDb(alarm)
        } else {
            val t = convertTimeToTimeStamp(
                getDayInWeek(getDayInWeek()),
                alarm.hourAlarm,
                alarm.minuteAlarm
            )
            val timeAlarm =
                if (t - getTimeCurrent() <= 0)
                    convertTimeToTimeStamp(
                        getDayCurrentInWeek() + 1,
                        alarm.hourAlarm,
                        alarm.minuteAlarm
                    )
                else
                    convertTimeToTimeStamp(
                        getDayInWeek(getDayInWeek()),
                        alarm.hourAlarm,
                        alarm.minuteAlarm
                    )
            createAlarm(context, alarm, timeAlarm)
            if (alarm.isBefore) addAlarmToDb(alarm)
        }
    }

    fun createAlarm(context: Context, alarm: AlarmModel, timeAlarm: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getService(
            context, alarm.id,
            Intent(context, AlarmService::class.java).apply {
                putExtra(ALARM_NOTIFY, Gson().toJsonTree(alarm).asJsonObject.toString())
                putExtra(ID_NOTIFY, alarm.id)
            },
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (alarm.isBefore) {
            if (timeAlarm - getTimeCurrent() > TIME_BEFORE)
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        timeAlarm - TIME_BEFORE,
                        pendingIntent
                    ), pendingIntent
                )
            else alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    getTimeCurrent(),
                    pendingIntent
                ), pendingIntent
            )
        } else alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(timeAlarm, pendingIntent),
            pendingIntent
        )
    }

    private fun addAlarmToDb(alarmModel: AlarmModel) {
        val lstAlarm = DataLocalManager.getListAlarm(LIST_ALARM)
        val lstTmp = lstAlarm.filter { it.id == alarmModel.id }
        if (lstTmp.isEmpty()) lstAlarm.add(alarmModel)
        else {
            lstAlarm.remove(lstTmp[0])
            lstAlarm.add(alarmModel)
        }
        DataLocalManager.setListAlarm(lstAlarm, LIST_ALARM)
    }
}