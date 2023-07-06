package com.remi.alarmclock.xtreme.free.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import com.remi.alarmclock.xtreme.free.adapter.TimezoneAdapter
import com.remi.alarmclock.xtreme.free.adapter.ViewPagerAddFragmentsAdapter
import com.remi.alarmclock.xtreme.free.addview.addtimezone.Item2FragClock
import com.remi.alarmclock.xtreme.free.addview.addtimezone.ItemFragClock
import com.remi.alarmclock.xtreme.free.addview.home.ViewHomeClock
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.data.DataClock
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.model.TimeZoneModel
import com.remi.alarmclock.xtreme.free.sharepref.DataLocalManager
import com.remi.alarmclock.xtreme.free.utils.Utils
import java.util.*

class ClockFragment: BaseFragment() {

    lateinit var vClock: ViewHomeClock
    private var timezoneAdapter: TimezoneAdapter? = null
    private var clockAdapter: TimezoneAdapter? = null

    companion object {
        fun newInstance(): ClockFragment {
            val args = Bundle()

            val fragment = ClockFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewLayout(): View {
        vClock = ViewHomeClock(requireContext())
        return vClock
    }

    override fun setUp() {
        setUpViewPager()
        setUpClock()
        setUpSearch()
        evenClick()

        Thread {
            val lstTimezone = DataClock.getAllTimezone(requireContext())
            Handler(Looper.getMainLooper()).post {
                timezoneAdapter?.setDataTimezone(lstTimezone)
            }
        }.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViewPager() {
        val adapter = ViewPagerAddFragmentsAdapter(parentFragmentManager, lifecycle)

        adapter.addFrag(ItemFragClock.newInstance())
        adapter.addFrag(Item2FragClock.newInstance())

        vClock.vPager.adapter = adapter

        vClock.vPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                vClock.setDotIndicator(position)
            }
        })
    }

    private fun evenClick() {
        vClock.vToolbar.ivAdd.setOnClickListener {
            setChoose()
            vClock.rcvClock.visibility = View.GONE
            vClock.actionSearch(requireContext(), TAG_SHOW)
        }

        vClock.vToolbar.ivBack.setOnClickListener { actionBack(it) }

        vClock.vToolbar.ivExit.setOnClickListener {
            vClock.vToolbar.edtSearch.setText("")
            vClock.rcvTimezone.visibility = View.GONE
            vClock.vToolbar.ivExit.visibility = View.GONE
        }

        vClock.vToolbar.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    vClock.vToolbar.ivExit.visibility = View.VISIBLE
                    vClock.rcvTimezone.visibility = View.VISIBLE
                    vClock.rlShowSearch.visibility = View.GONE
                } else {
                    vClock.vToolbar.ivExit.visibility = View.GONE
                    vClock.rcvTimezone.visibility = View.GONE
                    vClock.rlShowSearch.visibility = View.VISIBLE
                }
                timezoneAdapter?.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    fun actionBack(view: View) {
        vClock.vToolbar.edtSearch.setText("")
        vClock.actionSearch(requireContext(), TAG_HIDE)
        vClock.rcvClock.visibility = View.VISIBLE
        vClock.rcvTimezone.visibility = View.GONE
        Utils.hideKeyboard(requireContext(), view)

        clockAdapter?.setDataClock(DataLocalManager.getListClock(LIST_CLOCK))
    }

    private fun setUpClock() {
        clockAdapter = TimezoneAdapter(requireContext(), TAG_ITEM_CLOCK, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                val clock = ob as TimeZoneModel
                timezoneAdapter?.setChoose(clock, false)
                removeClock(clock)
            }
        })

        clockAdapter?.setDataClock(DataLocalManager.getListClock(LIST_CLOCK))
        Timer().schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post {
                    clockAdapter?.notifyChange()
                }
            }
        }, 0, 1000)
        vClock.rcvClock.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        vClock.rcvClock.adapter = clockAdapter
    }

    private fun setUpSearch() {
        timezoneAdapter = TimezoneAdapter(requireContext(), TAG_ITEM_TIMEZONE, object : ICallBackItem{
            override fun callBack(ob: Any, position: Int) {
                addClock(ob as TimeZoneModel)
                vClock.vToolbar.edtSearch.setText("")
            }
        })

        vClock.rcvTimezone.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        vClock.rcvTimezone.adapter = timezoneAdapter
    }

    private fun addClock(clock: TimeZoneModel) {
        Thread {
            val lstClock = DataLocalManager.getListClock(LIST_CLOCK)
            var isExist = false
            if (lstClock.isNotEmpty()) {
                for (c in lstClock)
                    if (c.zoneName == clock.zoneName) isExist = true
                if (!isExist) lstClock.add(clock)
            } else lstClock.add(clock)
            DataLocalManager.setListClock(lstClock, LIST_CLOCK)

            Handler(Looper.getMainLooper()).post {
                vClock.actionSearch(requireContext(), TAG_HIDE)
                vClock.rcvClock.visibility = View.VISIBLE
                vClock.rcvTimezone.visibility = View.GONE
                Utils.hideKeyboard(requireContext(), vClock)

                clockAdapter?.setDataClock(DataLocalManager.getListClock(LIST_CLOCK))
            }
        }.start()
    }

    private fun removeClock(clock: TimeZoneModel) {
        Thread {
            val lstClock = DataLocalManager.getListClock(LIST_CLOCK)
            val lstDel = lstClock.filter { it.zoneName == clock.zoneName }
            lstClock.remove(lstDel[0])
            DataLocalManager.setListClock(lstClock, LIST_CLOCK)

            Handler(Looper.getMainLooper()).post {
                clockAdapter?.setDataClock(DataLocalManager.getListClock(LIST_CLOCK))
            }
        }.start()
    }

    private fun setChoose() {
        Thread {
            val lstClock = DataLocalManager.getListClock(LIST_CLOCK)
            for (clock in lstClock)
                Handler(Looper.getMainLooper()).post { timezoneAdapter?.setChoose(clock, true) }
        }.start()
    }
}