package com.remi.alarmclock.xtreme.free.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.base.BaseActivity
import com.remi.alarmclock.xtreme.free.adapter.ViewPagerAddFragmentsAdapter
import com.remi.alarmclock.xtreme.free.addview.dialog.ViewDialogPermission
import com.remi.alarmclock.xtreme.free.addview.dialog.ViewDialogRattingApp
import com.remi.alarmclock.xtreme.free.addview.home.ViewMain
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.extensions.openSettingPermission
import com.remi.alarmclock.xtreme.free.fragment.*
import com.remi.alarmclock.xtreme.free.fragment.alarm.AlarmFragment
import com.remi.alarmclock.xtreme.free.helpers.*
import com.remi.alarmclock.xtreme.free.utils.ActionUtils

@SuppressLint("BatteryLife")
class MainActivity : BaseActivity(Color.parseColor("#181818"), Color.parseColor("#2C2C2C")) {

    private lateinit var viewHome: ViewMain
    private lateinit var alarmFrag: AlarmFragment
    private lateinit var clockFrag: ClockFragment
    private lateinit var timerFrag: TimerFragment
    private lateinit var stopwatchFrag: StopwatchFragment
    private lateinit var vCheckPer: ViewDialogPermission
    private lateinit var dialog: AlertDialog
    private var isBattery = false
    private var isOverlay = false

    private var w = 0F

    override fun getViewLayout(): View {
        w = resources.displayMetrics.widthPixels / 100F
        viewHome = ViewMain(this@MainActivity)
        vCheckPer = ViewDialogPermission(this@MainActivity)
        dialog = AlertDialog.Builder(this@MainActivity, R.style.SheetDialog).create()
        return viewHome
    }

    override fun setUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if ((getSystemService(POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(
                    packageName
                )
            )
                vCheckPer.checkOption(1)
            else if (Settings.canDrawOverlays(this@MainActivity)) vCheckPer.checkOption(2)
            else if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
                vCheckPer.checkOption(0)
            else showDialogPermission()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vCheckPer.checkOption(0)
            if ((getSystemService(POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(
                    packageName
                )
            )
                vCheckPer.checkOption(1)
            else if (Settings.canDrawOverlays(this@MainActivity)) vCheckPer.checkOption(2)
            else showDialogPermission()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewHome.vNavi.visibility == View.VISIBLE)
                    viewHome.actionNavigation(this@MainActivity, TAG_HIDE)
                else if (timerFrag.audioRingtoneFrag != null)
                    timerFrag.audioRingtoneFrag?.let {
                        if (it.isRecording) it.audioRecordFrag?.stopRecorder()
                        supportFragmentManager.popBackStack("AudioRingtoneFragment", -1)
                        timerFrag.audioRingtoneFrag = null
                    }
                else if (clockFrag.vClock.vToolbar.rlSearch.isVisible) clockFrag.actionBack(clockFrag.vClock)
                else onBackPressed(true)
            }
        })

        evenClick()

        viewHome.vPager.isUserInputEnabled = false

        val pagerAdapter = ViewPagerAddFragmentsAdapter(supportFragmentManager, lifecycle)

        alarmFrag = AlarmFragment.newInstance(object : ICallBackCheck {
            override fun check(isCheck: Boolean) {
                if (isCheck) viewHome.actionNavigation(this@MainActivity, TAG_SHOW)
            }
        })
        pagerAdapter.addFrag(alarmFrag)
        clockFrag = ClockFragment.newInstance()
        pagerAdapter.addFrag(clockFrag)
        timerFrag = TimerFragment.newInstance()
        pagerAdapter.addFrag(timerFrag)
        stopwatchFrag = StopwatchFragment.newInstance()
        pagerAdapter.addFrag(stopwatchFrag)

        viewHome.vPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changeLayout(position)
                if (clockFrag.vClock.vToolbar.rlSearch.isVisible) clockFrag.actionBack(clockFrag.vClock)
            }
        })

        viewHome.vPager.adapter = pagerAdapter
    }

    private fun evenClick() {
        viewHome.vAlarm.setOnClickListener { changeLayout(0) }
        viewHome.vClock.setOnClickListener { changeLayout(1) }
        viewHome.vTimer.setOnClickListener { changeLayout(2) }
        viewHome.vStopWatch.setOnClickListener { changeLayout(3) }

        viewHome.vNavi.rlBg.setOnClickListener {
            viewHome.actionNavigation(
                this@MainActivity,
                TAG_HIDE
            )
        }
        viewHome.vNavi.ivBack.setOnClickListener {
            viewHome.actionNavigation(
                this@MainActivity,
                TAG_HIDE
            )
        }
        viewHome.vNavi.vRemoveAds.setOnClickListener {
            viewHome.actionNavigation(this@MainActivity, TAG_HIDE)
            val removeAdsFrag = RemoveAdsFragment.newInstance()
            replaceFragment(supportFragmentManager, removeAdsFrag, true, true)
        }
        viewHome.vNavi.vLikeApp.setOnClickListener { showDialogLikeApp() }
        viewHome.vPager.offscreenPageLimit = 4
    }

    private fun showDialogLikeApp() {
        viewHome.actionNavigation(this@MainActivity, TAG_HIDE)
        val viewDialogRattingApp = ViewDialogRattingApp(this@MainActivity)

        val dialog = AlertDialog.Builder(this@MainActivity, R.style.SheetDialog).create()
        dialog.setCancelable(true)
        dialog.setView(viewDialogRattingApp)
        dialog.show()

        viewDialogRattingApp.layoutParams.height = (99.167f * w).toInt()
        viewDialogRattingApp.layoutParams.width = (77.778f * w).toInt()

        //rate = 5 - rating
        viewDialogRattingApp.tvSend.setOnClickListener {
            val rate = 5 - viewDialogRattingApp.ratingBar.rating
            if (rate > 3.5) ActionUtils.rateApp(this@MainActivity)
            else ActionUtils.sendFeedback(this@MainActivity)
        }
    }

    private fun showDialogPermission() {
        dialog.apply {
            setView(vCheckPer)
            setCancelable(false)
            show()
        }

        vCheckPer.layoutParams.width = (77.778f * w).toInt()
        vCheckPer.layoutParams.height = (79.167f * w).toInt()

        vCheckPer.tvAllow.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            )
                Dexter.withContext(this@MainActivity)
                    .withPermission("android.permission.POST_NOTIFICATIONS")
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            vCheckPer.checkOption(0)
                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                            showMessage(resources.getString(R.string.per_record))
                            openSettingPermission()
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken
                        ) {
                            p1.continuePermissionRequest()
                        }

                    }).check()
            else if (Build.VERSION.SDK_INT >= 23 && !(getSystemService(POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(packageName)) {
                requestPermission.launch(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:$packageName")
                })
                isBattery = true
            } else if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this@MainActivity)) {
                requestPermission.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                    data = Uri.parse("package:$packageName")
                })
                isOverlay = true
            }
        }
        vCheckPer.tvCancel.setOnClickListener {
            showMessage(resources.getString(R.string.per_record))
            dialog.cancel()
        }
    }

    private var requestPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (isBattery) {
                if (Build.VERSION.SDK_INT >= 23 && (getSystemService(POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(packageName)) {
                    vCheckPer.checkOption(1)
                    isBattery = false
                } else showMessage(resources.getString(R.string.per_battery))
            } else if (isOverlay) {
                if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(this@MainActivity)) {
                    vCheckPer.checkOption(2)
                    isOverlay = false
                } else showMessage(resources.getString(R.string.per_overlay))
            }

            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
                && (Build.VERSION.SDK_INT >= 23 && ((getSystemService(POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(
                    packageName
                ) && Settings.canDrawOverlays(this@MainActivity)))
            ) dialog.cancel()
        }

    private fun changeLayout(option: Int) {
        when (option) {
            0 -> {
                viewHome.chooseOptionBottom(this@MainActivity, TAG_ALARM)
                viewHome.vPager.setCurrentItem(option, true)
            }

            1 -> {
                viewHome.chooseOptionBottom(this@MainActivity, TAG_CLOCK)
                viewHome.vPager.setCurrentItem(option, true)
            }

            2 -> {
                viewHome.chooseOptionBottom(this@MainActivity, TAG_TIMER)
                viewHome.vPager.setCurrentItem(option, true)
            }

            3 -> {
                viewHome.chooseOptionBottom(this@MainActivity, TAG_STOPWATCH)
                viewHome.vPager.setCurrentItem(option, true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        alarmFrag.reloadData()

        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            && (Build.VERSION.SDK_INT >= 23 && ((getSystemService(POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(
                packageName
            ) && Settings.canDrawOverlays(this@MainActivity)))
        ) dialog?.cancel()
    }
}