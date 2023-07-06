package com.remi.alarmclock.xtreme.free.sharepref

import android.content.Context
import com.google.gson.Gson
import com.remi.alarmclock.xtreme.free.model.AlarmModel
import com.remi.alarmclock.xtreme.free.model.TimeZoneModel
import com.remi.alarmclock.xtreme.free.model.TimerModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DataLocalManager {
    private var mySharedPreferences: MySharePreferences? = null

    companion object {
        private var instance: DataLocalManager? = null
        fun init(context: Context) {
            instance = DataLocalManager()
            instance!!.mySharedPreferences = MySharePreferences(context)
        }

        private fun getInstance(): DataLocalManager? {
            if (instance == null) instance = DataLocalManager()
            return instance
        }

        fun setFirstInstall(key: String?, isFirst: Boolean) {
            getInstance()!!.mySharedPreferences!!.putBooleanValue(key, isFirst)
        }

        fun getFirstInstall(key: String?): Boolean {
            return getInstance()!!.mySharedPreferences!!.getBooleanValue(key)
        }

        fun setCheck(key: String?, volumeOn: Boolean) {
            getInstance()!!.mySharedPreferences!!.putBooleanValue(key, volumeOn)
        }

        fun getCheck(key: String?): Boolean {
            return getInstance()!!.mySharedPreferences!!.getBooleanValue(key)
        }

        fun setOption(option: String?, key: String?) {
            getInstance()!!.mySharedPreferences!!.putStringWithKey(key, option)
        }

        fun getOption(key: String?): String? {
            return getInstance()!!.mySharedPreferences!!.getStringWithKey(key, "")
        }

        fun setInt(count: Int, key: String?) {
            getInstance()!!.mySharedPreferences!!.putIntWithKey(key, count)
        }

        fun getInt(key: String?): Int {
            return getInstance()!!.mySharedPreferences!!.getIntWithKey(key, -1)
        }

        fun setListTimeStamp(key: String?, lstTimeStamp: ArrayList<String?>?) {
            val gson = Gson()
            val jsonArray = gson.toJsonTree(lstTimeStamp).asJsonArray
            val json = jsonArray.toString()
            getInstance()!!.mySharedPreferences!!.putStringWithKey(key, json)
        }

        fun getListTimeStamp(key: String?): ArrayList<String> {
            val lstTimeStamp = ArrayList<String>()
            val strJson = getInstance()!!.mySharedPreferences!!.getStringWithKey(key, "")
            try {
                val jsonArray = JSONArray(strJson)
                for (i in 0 until jsonArray.length()) {
                    lstTimeStamp.add(jsonArray[i].toString())
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return lstTimeStamp
        }

        fun setArrStr(arrStr: ArrayList<String>, key: String) {
            val json = Gson().toJsonTree(arrStr).asJsonArray.toString()
            getInstance()!!.mySharedPreferences!!.putStringWithKey(key, json)
        }

        fun getArrStr(key: String): ArrayList<String> {
            val arrStr = ArrayList<String>()
            try {
                if (getInstance()!!.mySharedPreferences!!.getStringWithKey(key, "") == "") return arrStr
                val jsonArray = JSONArray(getInstance()!!.mySharedPreferences!!.getStringWithKey(key, ""))
                for (i in 0 until jsonArray.length()) arrStr.add(jsonArray[i].toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return arrStr
        }

        fun setAlarm(key: String?, alarmModel: AlarmModel) {
            val json = Gson().toJsonTree(alarmModel).asJsonObject.toString()
            getInstance()!!.mySharedPreferences?.putStringWithKey(key, json)
        }

        fun getAlarm(key: String?): AlarmModel? {
            val strJson = getInstance()!!.mySharedPreferences!!.getStringWithKey(key, "")!!
            var alarmModel: AlarmModel? = null
            try {
                alarmModel = Gson().fromJson(JSONObject(strJson).toString(), AlarmModel::class.java)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return alarmModel
        }

        fun setTimer(key: String?, timerModel: TimerModel) {
            getInstance()!!.mySharedPreferences?.putStringWithKey(
                key,
                Gson().toJsonTree(timerModel).asJsonObject.toString()
            )
        }

        fun getTimer(key: String?): TimerModel? {
            val strJson = getInstance()!!.mySharedPreferences!!.getStringWithKey(key, "")!!
            var timerModel: TimerModel? = null
            try {
                timerModel = Gson().fromJson(JSONObject(strJson).toString(), TimerModel::class.java)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return timerModel
        }

        fun setListAlarm(lstAlarm: MutableList<AlarmModel>, key: String) {
            getInstance()!!.mySharedPreferences!!.putStringWithKey(key, Gson().toJsonTree(lstAlarm).asJsonArray.toString())
        }

        fun getListAlarm(key: String): MutableList<AlarmModel> {
            val lstAlarm = mutableListOf<AlarmModel>()
            val strJson = getInstance()!!.mySharedPreferences!!.getStringWithKey(key, "")
            try {
                val jsonArray = JSONArray(strJson)
                for (i in 0 until jsonArray.length()) {
                    lstAlarm.add(Gson().fromJson(jsonArray.getJSONObject(i).toString(), AlarmModel::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return lstAlarm
        }

        fun setListClock(lstAlarm: MutableList<TimeZoneModel>, key: String) {
            getInstance()!!.mySharedPreferences!!.putStringWithKey(key, Gson().toJsonTree(lstAlarm).asJsonArray.toString())
        }

        fun getListClock(key: String): MutableList<TimeZoneModel> {
            val lstClock = mutableListOf<TimeZoneModel>()
            val strJson = getInstance()!!.mySharedPreferences!!.getStringWithKey(key, "")
            try {
                val jsonArray = JSONArray(strJson)
                for (i in 0 until jsonArray.length()) {
                    lstClock.add(Gson().fromJson(jsonArray.getJSONObject(i).toString(), TimeZoneModel::class.java))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return lstClock
        }
    }
}