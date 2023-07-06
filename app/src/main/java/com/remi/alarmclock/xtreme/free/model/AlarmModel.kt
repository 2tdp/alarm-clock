package com.remi.alarmclock.xtreme.free.model

import java.io.Serializable

class AlarmModel : Serializable, Cloneable {

    var id: Int = -1
    var name: String = "alarm"
    var hourAlarm: Int = -1
    var minuteAlarm: Int = -1
    var isTypeTime: Int = -1
    var repeat: MutableList<String> = mutableListOf()
    var ringtone: RingtoneModel? = null
    var volume: Int = -1
    var isVibrate: Boolean = false
    var snooze: Int = -1
    var isRun: Boolean = false
    var isBefore: Boolean = true

    constructor(
        id: Int,
        name: String,
        hourAlarm: Int,
        minuteAlarm: Int,
        isTypeTime: Int,
        repeat: MutableList<String>,
        ringtone: RingtoneModel,
        volume: Int,
        isVibrate: Boolean,
        snooze: Int,
        isRun: Boolean,
        isBefore: Boolean
    ) {
        this.id = id
        this.name = name
        this.hourAlarm = hourAlarm
        this.minuteAlarm = minuteAlarm
        this.isTypeTime = isTypeTime
        this.repeat = repeat
        this.ringtone = ringtone
        this.volume = volume
        this.isVibrate = isVibrate
        this.snooze = snooze
        this.isRun = isRun
        this.isBefore = isBefore
    }

    constructor()

    public override fun clone(): AlarmModel {
        try {
            return super.clone() as AlarmModel
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }
        return AlarmModel()
    }

    override fun toString(): String {
        return super.toString()
    }
}