package com.remi.alarmclock.xtreme.free.viewcustom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck


@SuppressLint("ViewConstructor")
class CustomSeekbar(context: Context, isSwipe: ICallBackCheck) : View(context) {

    companion object {
        var w = 0F
    }

    private val isSwipe: ICallBackCheck
    private var paint: Paint
    private var paintProgress: Paint
    private var progress = 0
    private var max = 0
    private var sizeThumb = 0f
    private var sizeBg = 0f
    private var sizePos = 0f

    var onSeekbarResult: OnSeekbarResult? = null

    init {
        this.isSwipe = isSwipe
        w = resources.displayMetrics.widthPixels / 100F
        sizeThumb = 4.44f * w
        sizeBg = 1.11f * w
        sizePos = 1.11f * w

        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.FILL
        }
        paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.apply {
            clearShadowLayer()
            color = ContextCompat.getColor(context, R.color.black_background2)
            strokeWidth = sizeBg
        }
        canvas.drawLine(sizeThumb / 2, height / 2f, width - sizeThumb / 2, height / 2f, paint)

        paintProgress.apply {
            color = ContextCompat.getColor(context, R.color.main_color)
            strokeWidth = sizePos
        }
        val p = (width - sizeThumb) * progress / max + sizeThumb / 2
        canvas.drawLine(sizeThumb / 2f, height / 2f, p, height / 2f, paintProgress)

        paint.apply {
            color = ContextCompat.getColor(context, R.color.main_color)
            setShadowLayer(sizeThumb / 8, 0f, 0f, ContextCompat.getColor(context, R.color.white2))
        }
        canvas.drawCircle(p, height / 2f, sizeThumb / 2, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isSwipe.check(true)
                onSeekbarResult?.onDown(this)
            }
            MotionEvent.ACTION_MOVE -> {
                progress = ((x - sizeThumb / 2) * max / (width - sizeThumb)).toInt()

                if (progress < 0) progress = 0
                else if (progress > max) progress = max
                invalidate()

                onSeekbarResult?.onMove(this, progress)
            }
            MotionEvent.ACTION_UP -> {
                isSwipe.check(false)
                onSeekbarResult?.onUp(this, progress)
            }
        }
        return true
    }

    fun getProgress(): Int {
        return this.progress
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    fun setMax(max: Int) {
        this.max = max
        invalidate()
    }
}