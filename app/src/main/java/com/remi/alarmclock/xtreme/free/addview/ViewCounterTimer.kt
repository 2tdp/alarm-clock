package com.remi.alarmclock.xtreme.free.addview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R

class ViewCounterTimer(context: Context): View(context) {

    companion object {
        var w = 0f
    }

    private val context: Context
    private val paint: Paint
    private var rectF: RectF

    private var strokeCir = 0f
    private var radiusCir = 0f
    private var sweepAngle = 360f

    private var isFirst = true

    var animator : ValueAnimator? = null

    init {
        this.context = context
        w = resources.displayMetrics.widthPixels / 100F

        strokeCir = 1.667f * w


        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.apply {
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            strokeWidth = strokeCir
        }
        rectF = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.apply {
            color = Color.parseColor("#3B3B3B")
            style = Paint.Style.STROKE
        }
        radiusCir = height / 2f - strokeCir / 2f
        canvas.drawCircle(width / 2f, height / 2f, radiusCir, paint)

        paint.apply {
            color = ContextCompat.getColor(context, R.color.main_color)
            style = Paint.Style.STROKE
        }
        rectF.set(
            width / 2f - radiusCir,
            strokeCir / 2,
            width / 2f + radiusCir,
            height.toFloat() - strokeCir / 2f
        )
        canvas.drawArc(rectF, -90f, sweepAngle, false, paint)
    }

    fun setSweepAngle(sweep: Float, totalTime: Long) {
        if (isFirst) {
            animator = ValueAnimator.ofFloat(sweep, 0f).apply {
                duration = totalTime
                addUpdateListener {anim ->
                    sweepAngle = anim.animatedValue as Float
                    invalidate()
                }
                start()
            }
            isFirst = false
        }
    }

    fun destroyAnimator() {
        animator?.cancel()
        isFirst = true
    }
}