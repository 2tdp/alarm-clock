package com.remi.alarmclock.xtreme.free.activity.base.test

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class TestDrawCircleClock(context: Context) : View(context) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val rect = RectF()
    val rectText = Rect()
    var current = 0F

    private var mRadius = 0F
    var disableTouch = 0L

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = Color.BLACK
            textSize = 50f
        }
        canvas.drawRect(rect, paint)

        for (i in 1..60) {
            mRadius = height.coerceAtMost(width) / 2 - 30f
            if (i % 5 != 0) {
                canvas.save()
                canvas.rotate(current + (i - 15) * 6f, width / 2f, height / 2f)
                canvas.drawLine(width / 2F, 10f, width / 2f, 10 + 30f, paint)
                canvas.restore()
            } else {
                val angle = (current + (i - 15) * 6) * Math.PI / 180
                var tmp = i.toString()
                if (tmp == "60") tmp = "00"
                else if (tmp == "5") tmp = "05"
                paint.getTextBounds(tmp, 0, tmp.length, rectText) // f
                // find the circle-wise (x, y) position as mathematical rule
                val x = (width / 2 + cos(angle) * mRadius - rectText.width() / 2)
                val y = (height / 2 + sin(angle) * mRadius + rectText.height() / 2)
                canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> disableTouch = System.currentTimeMillis()
            MotionEvent.ACTION_MOVE -> {
                if (abs(disableTouch - System.currentTimeMillis()) < 100) return false
                val dx = width / 2 - event.x
                val dy = height / 2 - event.y
                current = (atan2(dy, dx) * 180 / Math.PI).toFloat()

                if (current < 0) current = 360 - (current * -1)
                current -= 180f
                if (current < 0) current += 360f

                invalidate()
            }
        }
        return true
    }
}