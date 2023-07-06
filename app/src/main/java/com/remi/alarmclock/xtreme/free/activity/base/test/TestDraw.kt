package com.remi.alarmclock.xtreme.free.activity.base.test

import android.content.Context
import android.graphics.*
import android.view.View
import java.text.SimpleDateFormat
import java.util.*

class TestDraw(context: Context) : View(context) {

    companion object {
        const val TAG = "2tdp"
    }

    val calendar = Calendar.getInstance(Locale.ENGLISH)
    private val arrTextTitle = arrayOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
    private var dayCurrent = 0
    private var monthCurrent = 0
    private var day = 1
    private var dayInWeek = 0
    private var totalDayInMonth = 0
    private var totalDayInMonthPre = 0
    private var index = 0
    private var count = 1

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintBgText = Paint(Paint.ANTI_ALIAS_FLAG)
    var rectText = Rect()
    var isCircle = false
    var isRoundRect = false
    var isLine = false
    var size = 0f
    private var numText = 0
    private var numGrid = 0

    private var colorTextInMonth = -1
    private var colorTextNotInMonth = -1
    private var colorTextCurrentDay = -1
    private var colorTextTitle = -1
    private var colorBgDayCurrent = -1
    private var colorBgDayInMonth = -1
    private var colorBgDayNotInMonth = -1
    private var radiusCir = 0F
    private var rectFRound = RectF()
    private var radiusRound = 0f
    private var distance = 0F
    private var colorLine = -1
    private var colorRound = -1

    init {
        calendar[Calendar.DAY_OF_MONTH] = 1
        dayCurrent =
            SimpleDateFormat("dd", Locale.ENGLISH).format(Calendar.getInstance(Locale.ENGLISH).time)
                .toInt()
        dayInWeek = calendar[Calendar.DAY_OF_WEEK] - 1
        totalDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        totalDayInMonthPre = getTotalDayInMonthPre()
        monthCurrent = calendar[Calendar.MONTH] + 1

        if (dayInWeek > 6 && totalDayInMonth == 31) {
            numText = 6
            numGrid = 7
        } else {
            numText = 5
            numGrid = 6
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas)
        drawInSize(canvas)
    }

    private fun drawInSize(canvas: Canvas) {
        paint.textSize = width / 15f
        for (i in 0..numText)
            for (j in 1..7) {
                canvas.save()
                canvas.translate(size * (j - 1), size * i)
                paint.style = Paint.Style.FILL
                if (i == 0) drawTitleText(j - 1, canvas)
                else drawDays(canvas)

                canvas.restore()
            }
    }

    private fun drawDays(canvas: Canvas) {
        val text: String
        if (index == dayInWeek && day <= totalDayInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, day)
            text = day.toString()
            paint.getTextBounds(text, 0, text.length, rectText)

            if (day == dayCurrent) {
                if (colorTextCurrentDay != -1) paint.color = colorTextCurrentDay
                else paint.color = Color.RED
                drawBgDayCurrent(canvas)
            } else if (colorTextInMonth != -1) {
                paint.color = colorTextInMonth
                drawBgDayInMonth(canvas)
            } else paint.color = Color.BLACK

            day++
            dayInWeek++
        } else {
            drawBgDayNotInMonth(canvas)
            if (colorTextNotInMonth != -1) paint.color = colorTextNotInMonth
            else paint.color = Color.GRAY
            if (index < dayInWeek) {
                calendar.set(Calendar.DAY_OF_MONTH, totalDayInMonthPre - dayInWeek + index + 1)
                text = (totalDayInMonthPre - dayInWeek + index + 1).toString()
                paint.getTextBounds(text, 0, text.length, rectText)
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, count)
                text = count.toString()
                paint.getTextBounds(text, 0, text.length, rectText)
                count++
            }
        }

        canvas.drawText(text, 0, text.length, size / 2f - rectText.width() / 2, size / 2f + rectText.height() / 2, paint)
        index++

        if (isCircle) canvas.drawCircle(size / 2,size / 2,radiusCir, paintBgText)
    }

    private fun drawTitleText(index: Int, canvas: Canvas) {
        if (colorTextTitle != -1) paint.color = colorTextTitle
        else paint.color = Color.BLACK

        val text = arrTextTitle[index]
        paint.getTextBounds(text, 0, text.length, rectText)
        canvas.drawText(
            text,
            0,
            text.length,
            size / 2f - rectText.width() / 2,
            size / 2f + rectText.height() / 2,
            paint
        )
    }

    private fun drawBgDayCurrent(canvas: Canvas) {
        if (isLine) {
            paintBgText.apply {
                style = Paint.Style.FILL
                color = colorBgDayCurrent
            }
            canvas.drawRect(0F, 0F, size, size, paintBgText)
        } else if (isRoundRect) {
            paintBgText.apply {
                style = if (isCircle) Paint.Style.FILL
                else Paint.Style.STROKE
                color = colorBgDayCurrent
            }
            val radiusArr = floatArrayOf(
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound
            )
            val rectFRound = RectF(distance, distance, size - distance, size - distance)

            canvas.drawPath(Path().apply {
                addRoundRect(rectFRound, radiusArr, Path.Direction.CW)
            }, paintBgText)
        }
    }

    private fun drawBgDayInMonth(canvas: Canvas) {
        if (isLine) {
            paintBgText.apply {
                style = Paint.Style.FILL
                color = colorBgDayInMonth
            }
            canvas.drawRect(distance, distance, size - distance, size - distance, paintBgText)
        } else {
            paintBgText.apply {
                style = if (isCircle) Paint.Style.STROKE
                else Paint.Style.FILL
                color = colorBgDayInMonth
            }
            val radiusArr = floatArrayOf(
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound
            )
            val rectFRound = RectF(distance, distance, size - distance, size - distance)

            canvas.drawPath(Path().apply {
                addRoundRect(rectFRound, radiusArr, Path.Direction.CW)
            }, paintBgText)
        }
    }

    private fun drawBgDayNotInMonth(canvas: Canvas) {
        if (isLine) {
            paintBgText.apply {
                style = Paint.Style.FILL
                color = colorBgDayNotInMonth
            }
            canvas.drawRect(distance, distance, size - distance, size - distance, paintBgText)
        } else {
            paintBgText.apply {
                style = if (isCircle) Paint.Style.STROKE
                else Paint.Style.FILL
                color = colorBgDayNotInMonth
            }
            val radiusArr = floatArrayOf(
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound,
                radiusRound
            )
            val rectFRound = RectF(distance, distance, size - distance, size - distance)

            canvas.drawPath(Path().apply {
                addRoundRect(rectFRound, radiusArr, Path.Direction.CW)
            }, paintBgText)
        }
    }

    private fun drawGrid(canvas: Canvas) {
        paintBgText.apply {
            color = colorLine
            style = Paint.Style.STROKE
            strokeWidth = distance
        }
        size = width / 7f
        for (i in 1..numGrid)
            canvas.drawLine(0f, size * i, width.toFloat(), size * i, paintBgText)
        for (i in 0..7)
            canvas.drawLine(size * i, size, size * i, height.toFloat() - size, paintBgText)
    }

    private fun getTotalDayInMonthPre(): Int {
        val c = GregorianCalendar(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] - 1,
            Calendar.DAY_OF_MONTH
        )
        return c.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun setColorText(colorTextInMonth: Int, colorTextNotInMonth: Int, colorTextCurrentDay: Int, colorTextTitle: Int) {
        this.colorTextInMonth = colorTextInMonth
        this.colorTextNotInMonth = colorTextNotInMonth
        this.colorTextCurrentDay = colorTextCurrentDay
        this.colorTextTitle = colorTextTitle

        invalidate()
    }

    fun setBackgroundDayCurrent(colorBg: Int) {
        this.colorBgDayCurrent = colorBg

        invalidate()
    }

    fun setBackgroundDayInMonth(colorBg: Int) {
        this.colorBgDayInMonth = colorBg

        invalidate()
    }

    fun setBackgroundDayNotInMonth(colorBg: Int) {
        this.colorBgDayNotInMonth = colorBg

        invalidate()
    }

    fun setBackgroundRoundRect(distance: Float, radius: Float, isFill: Boolean) {
        this.distance = distance
        this.radiusRound = radius
        this.colorLine = Color.TRANSPARENT

        this.isLine = false
        if (isFill) {
            isRoundRect = true
            isCircle = false
        }
        else {
            isCircle = true
            isRoundRect = false
        }
    }

    fun setBackgroundLine(distance: Float, colorLine: Int) {
        this.colorLine = colorLine
        this.distance = distance
    }
}