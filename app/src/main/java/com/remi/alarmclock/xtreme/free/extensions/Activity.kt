package com.remi.alarmclock.xtreme.free.extensions

import android.app.Activity
import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.IntegerRes
import com.bumptech.glide.util.Util.isOnMainThread
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.base.BaseActivity
import java.io.File

fun BaseActivity.getTempFile(child: String): File? {
    val folder = File(cacheDir, child)
    if (!folder.exists()) {
        if (!folder.mkdir()) {
            showToast(getString(R.string.unknown_error_occurred), Gravity.CENTER)
            return null
        }
    }

    return folder
}

fun BaseActivity.setIntent(nameActivity: String, isFinish: Boolean) {
    val intent = Intent()
    intent.component = ComponentName(this, nameActivity)
    startActivity(
        intent,
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
    )
    if (isFinish) finish()
}

fun BaseActivity.setAnimExit() {
    overridePendingTransition(R.anim.slide_in_left_small, R.anim.slide_out_right)
}

fun BaseActivity.showToast(msg: String, gravity: Int) {
    val toast: Toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    toast.setGravity(gravity, 0, 0)
    toast.show()
}

fun BaseActivity.hideKeyboardMain() {
    if (isOnMainThread()) hideKeyboardSync()
    else {
        Handler(Looper.getMainLooper()).post {
            hideKeyboardSync()
        }
    }
}

fun BaseActivity.hideKeyboardSync() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    currentFocus?.clearFocus()
}

fun BaseActivity.showKeyboard(et: EditText) {
    et.requestFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
}

fun BaseActivity.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun BaseActivity.setStatusBarTransparent(colorStatus: Int,colorNavi: Int) {
    val decorView = window.decorView
    window.statusBarColor = colorStatus
    window.navigationBarColor = colorNavi

    val flags = (SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    decorView.systemUiVisibility = flags

//    window.statusBarColor = Color.TRANSPARENT
//    window.navigationBarColor = Color.TRANSPARENT
//    if (Build.VERSION.SDK_INT in 21..29) {
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
//    } else if (Build.VERSION.SDK_INT >= 30)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
}

fun BaseActivity.openSettingPermission() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}