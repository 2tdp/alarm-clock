package com.remi.alarmclock.xtreme.free.callback

import java.util.Objects

interface ICallBackDimensional {
    fun callBackItem(objects: Objects, callBackItem: ICallBackItem)

    fun callBackCheck(objects: Objects, check: ICallBackCheck)
}