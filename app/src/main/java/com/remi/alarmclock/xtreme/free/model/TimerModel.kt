package com.remi.alarmclock.xtreme.free.model

class TimerModel(
    var hour: Int,
    var minute: Int,
    var second: Int,
    var duration: Long,
    var ringtone: RingtoneModel,
    var volume: Int,
    var isVibrate: Boolean
) : Cloneable {

    override fun clone(): TimerModel {
        return super.clone() as TimerModel
    }
}