package com.remi.alarmclock.xtreme.free.activity.base.test

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View

class AntiCounterView(context: Context) : View(context) {
    var text = "hahahaha"
    var transparent = 20
    var isShuffle = true
    var tempShuffle = false
    var angle = -45f
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val rectText = Rect()

    var isCreateRect = true
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val marginW = width / 10
        val marginH = width / 10
        paint.apply {
            textSize = width / 200f * 10
            color = Color.BLACK
            alpha = 255 * transparent / 100
        }
        if (isCreateRect) {
            isCreateRect = false
            paint.getTextBounds(text, 0, text.length, rectText)
        }
        canvas.save()
        canvas.rotate(angle, width / 2f, height / 2f)

        var startX = -width.toFloat()
        var startY = -height.toFloat()

        while (startY < height * 2) {
            while (startX < width * 2) {
                 canvas.drawText(text, startX, startY, paint)
                startX += marginW + rectText.width()
                Log.d("~~~", "onDraw: $startX ---$startY")
            }
            tempShuffle = !tempShuffle
            if (isShuffle && tempShuffle)
                startX = -width.toFloat()
            else
                startX = -width.toFloat()+rectText.width()/2
            startY +=marginH+rectText.height()
        }
        canvas.restore()
    }
}