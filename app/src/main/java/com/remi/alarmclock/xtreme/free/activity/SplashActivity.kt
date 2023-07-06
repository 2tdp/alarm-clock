package com.remi.alarmclock.xtreme.free.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import com.remi.alarmclock.xtreme.free.activity.base.BaseActivity
import com.remi.alarmclock.xtreme.free.addview.ViewSplash

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(Color.parseColor("#181818"), Color.parseColor("#181818")) {

    private lateinit var viewSplash: ViewSplash
    private var progressStatus = 0

    override fun getViewLayout(): View {
        viewSplash = ViewSplash(this@SplashActivity)
        return viewSplash
    }

    override fun setUp() {


        Thread {
            while (progressStatus < 99) {
                progressStatus += 1
                Handler(Looper.getMainLooper()).post {
                    viewSplash.customSeekbarLoading.setProgress(progressStatus)
                }
                try {
                    Thread.sleep(34)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }.start()
    }


}