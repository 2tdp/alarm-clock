package com.remi.alarmclock.xtreme.free.activity.base

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.activity.base.ActivityView
import com.remi.alarmclock.xtreme.free.activity.base.BaseFragment
import com.remi.alarmclock.xtreme.free.extensions.hideKeyboardMain
import com.remi.alarmclock.xtreme.free.extensions.setAnimExit
import com.remi.alarmclock.xtreme.free.extensions.setStatusBarTransparent
import com.remi.alarmclock.xtreme.free.extensions.showToast
import com.remi.alarmclock.xtreme.free.utils.Utils
import com.remi.alarmclock.xtreme.free.viewcustom.CustomLoadingDialog
import java.util.*

abstract class BaseActivity(
    private val colorStatus: Int,
    private val colorNavi: Int
) : AppCompatActivity(), ActivityView {

    private var TAG_LOADING = CustomLoadingDialog::class.java.name

    private var finish = 0
    private var mIsShowLoading = false

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarTransparent(colorStatus, colorNavi)
        setContentView(getViewLayout())

        setUp()
    }

    protected abstract fun getViewLayout(): View

    protected abstract fun setUp()

    override fun startIntent(nameActivity: String, isFinish: Boolean) {
        val intent = Intent()
        intent.component = ComponentName(this, nameActivity)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        if (isFinish) finish()
    }

    protected fun openNavigation(nameActivity: String, isFinish: Boolean) {
        val intent = Intent()
        intent.component = ComponentName(this, nameActivity)
        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left_small,
                R.anim.slide_out_right
            ).toBundle()
        )
        if (isFinish) finish()
    }

    protected fun onBackPressed(isAsk: Boolean) {
        if (isAsk) {
            if (finish != 0) {
                finish = 0
                finish()
                setAnimExit()
            } else {
                Toast.makeText(this, R.string.finish, Toast.LENGTH_SHORT).show()
                finish++
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        finish = 0
                    }
                }, 1000)
            }
        } else onBackPressedDispatcher.onBackPressed()
    }

    override fun replaceFragment(
        manager: FragmentManager,
        fragment: Fragment,
        isAdd: Boolean,
        addBackStack: Boolean,
        enter: Int,
        exit: Int,
        popEnter: Int,
        popExit: Int
    ) {
        try {
            val fragmentTransaction = manager.beginTransaction()
            fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit)
            if (addBackStack) fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)

            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun replaceFragment(
        manager: FragmentManager,
        fragment: Fragment,
        res: Int,
        isAdd: Boolean,
        addBackStack: Boolean,
        enter: Int,
        exit: Int,
        popEnter: Int,
        popExit: Int
    ) {
        try {
            val fragmentTransaction = manager.beginTransaction()
            fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit)
            if (addBackStack) fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
            if (isAdd) fragmentTransaction.add(res, fragment, fragment.javaClass.simpleName)
            else fragmentTransaction.replace(res, fragment, fragment.javaClass.simpleName)

            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun replaceFragment(
        fragment: Fragment,
        layout: Int,
        isAdd: Boolean,
        addBackStack: Boolean
    ) {
        replaceFragment(
            supportFragmentManager,
            fragment,
            layout,
            isAdd,
            addBackStack,
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left_small,
            R.anim.slide_out_left
        )
    }

    override fun replaceFragment(fragment: Fragment, isAdd: Boolean, addBackStack: Boolean) {
        replaceFragment(
            supportFragmentManager,
            fragment,
            android.R.id.content,
            isAdd,
            addBackStack,
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left_small,
            R.anim.slide_out_left
        )
    }

    override fun replaceFragment(fragment: Fragment, layout: Int, addBackStack: Boolean) {
        replaceFragment(
            supportFragmentManager,
            fragment,
            layout,
            false,
            addBackStack,
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left_small,
            R.anim.slide_out_left
        )
    }

    override fun replaceFragment(fragment: Fragment, addBackStack: Boolean) {
        replaceFragment(
            supportFragmentManager, fragment, android.R.id.content, false, addBackStack,
            R.anim.slide_in_right, R.anim.slide_out_left,
            R.anim.slide_in_left_small, R.anim.slide_out_left
        )
    }

    override fun replaceFragment(
        manager: FragmentManager,
        fragment: Fragment,
        isAdd: Boolean,
        addBackStack: Boolean
    ) {
        replaceFragment(
            manager,
            fragment,
            android.R.id.content,
            isAdd,
            addBackStack,
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left_small,
            R.anim.slide_out_left
        )
    }

    override fun replaceFragment(
        manager: FragmentManager,
        fragment: Fragment,
        layout: Int,
        isAdd: Boolean,
        addBackStack: Boolean
    ) {
        replaceFragment(
            manager,
            fragment,
            layout,
            isAdd,
            addBackStack,
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left_small,
            R.anim.slide_out_left
        )
    }

    override fun replaceFragment(
        manager: FragmentManager,
        fragment: Fragment,
        res: Int,
        isAdd: Boolean,
        addBackStack: Boolean,
        shareElement: View,
        transitionName: String
    ) {
        runOnUiThread {
            try {
                val fragmentTransaction = manager.beginTransaction()
                fragmentTransaction.addSharedElement(shareElement, transitionName)
                if (addBackStack) fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
                if (isAdd) { fragmentTransaction.add(res, fragment, fragment.javaClass.simpleName) }
                else fragmentTransaction.replace(res, fragment, fragment.javaClass.simpleName)
                fragmentTransaction.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun popBackStack(manager: FragmentManager) {
        runOnUiThread { manager.popBackStack() }
    }

    override fun popBackStack() {
        runOnUiThread { supportFragmentManager.popBackStack() }
    }

    override fun popBackStack(manager: FragmentManager, tag: String, inclusive: Int) {
        runOnUiThread { manager.popBackStack(tag, inclusive) }
    }

    override fun popBackStack(tag: String, inclusive: Int) {
        runOnUiThread { supportFragmentManager.popBackStack(tag, inclusive) }
    }

    override fun clearBackStack() {
        runOnUiThread {
            val fm = supportFragmentManager
            val count = fm.backStackEntryCount
            for (i in 0 until count) {
                fm.popBackStack()
            }
        }
    }

    override fun onFragmentDetach(fragment: Fragment) {

    }

    override fun hideKeyboard() {
        if (this.currentFocus != null) hideKeyboardMain()
    }

    override fun showLoading() {
        showLoading(true)
    }

    override fun showLoading(cancelable: Boolean) {
        val loadingDialog = CustomLoadingDialog(this@BaseActivity).apply {
            isCancelable = cancelable
            setDismissOnBackPress(cancelable)
        }
        mIsShowLoading = true
        loadingDialog.show(supportFragmentManager, TAG_LOADING)
    }

    override fun hideLoading() {
        mIsShowLoading = false
        val loadingDialog = supportFragmentManager.findFragmentByTag(TAG_LOADING)
        if (loadingDialog is CustomLoadingDialog && loadingDialog.isVisible && loadingDialog.isAdded)
            loadingDialog.dismiss()

        //cho chắc :(
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing && !mIsShowLoading) {
                val fragment = supportFragmentManager.findFragmentByTag(TAG_LOADING)
                if (fragment is CustomLoadingDialog && fragment.isVisible && fragment.isAdded)
                    fragment.dismiss()
            }
        }, 300)
    }

    override fun showPopupMessage(msg: String, title: String) {

    }

    override fun showPopupMessage(msg: String) {

    }

    override fun showMessage(msg: String) {
        showToast(msg, Gravity.CENTER)
    }

    override fun showMessage(@StringRes stringId: Int) {
        showToast(getString(stringId), Gravity.CENTER)
    }

    override fun refresh() {
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BaseFragment) fragment.refresh()
        }
    }
}