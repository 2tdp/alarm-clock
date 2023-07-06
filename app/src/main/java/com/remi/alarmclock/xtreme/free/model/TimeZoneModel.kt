package com.remi.alarmclock.xtreme.free.model

import java.io.Serializable

class TimeZoneModel : Serializable {

    var zoneName = ""
    var countryCode = ""
    var abbreviation = ""
    var timeStart = ""
    var gmtOffset = ""
    var dst = ""
    var isChoose = false
    var isOpenDel = false

    constructor(zoneName: String, countryCode: String, abbreviation: String, timeStart: String, gmtOffset: String, dst: String) {
        this.zoneName = zoneName
        this.countryCode = countryCode
        this.abbreviation = abbreviation
        this.timeStart = timeStart
        this.gmtOffset = gmtOffset
        this.dst = dst
    }

    constructor(zoneName: String, countryCode: String, abbreviation: String, timeStart: String, gmtOffset: String, dst: String, isChoose: Boolean, isOpenDel: Boolean) {
        this.zoneName = zoneName
        this.countryCode = countryCode
        this.abbreviation = abbreviation
        this.timeStart = timeStart
        this.gmtOffset = gmtOffset
        this.dst = dst
        this.isChoose = isChoose
        this.isOpenDel = isOpenDel
    }
}