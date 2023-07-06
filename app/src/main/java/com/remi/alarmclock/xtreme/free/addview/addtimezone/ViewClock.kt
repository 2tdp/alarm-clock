package com.remi.alarmclock.xtreme.free.addview.addtimezone

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.utils.UtilsBitmap
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

class ViewClock(context: Context): View(context) {

    companion object {
        var w = 0F
    }

    private lateinit var calendar: Calendar
    private var bmBg: Bitmap? = null
    private val paintBm: Paint
    private val paint: Paint
    private var rect: Rect

    private var mRadius = 0F
    private var mRadius2 = 0F

    init {
        w = resources.displayMetrics.widthPixels / 100F
        bmBg = UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.im_clock)

        paintBm = Paint(Paint.FILTER_BITMAP_FLAG)
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#8E98A1")
            style = Paint.Style.FILL
            strokeWidth = 4f
        }
        rect = Rect()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bmBg?.let {
            rect.set(0, 0, width, height)
            canvas.drawBitmap(it, null, rect, paintBm)
        }

        calendar = Calendar.getInstance(Locale.getDefault())
        var hour = calendar[Calendar.HOUR_OF_DAY]
        hour = if (hour > 12) hour - 12 else hour
        drawHour(canvas, (hour + calendar[Calendar.MINUTE] / 60f) * 5)
        drawMinute(canvas, calendar[Calendar.MINUTE])

        canvas.drawCircle(width / 2f, height / 2f, 1.389f * w, paint)

        drawSecond(canvas, calendar[Calendar.SECOND])
        canvas.drawCircle(width / 2f, height / 2f, 0.833f * w, paint)

        invalidate()
    }

    private fun drawSecond(canvas: Canvas, second: Int) {
        paint.apply {
            color = Color.parseColor("#FF9F0A")
            style = Paint.Style.FILL
            strokeWidth = 0.556f * w
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        mRadius = width / 2 - 10.2778f * w
        mRadius2 = width / 2 - 18.889f * w
        val angle = Math.PI * second / 30 - Math.PI / 2
        canvas.drawLine(
            (width / 2f - cos(angle) * mRadius2).toFloat(),
            (height / 2f - sin(angle) * mRadius2).toFloat(),
            (width / 2f + cos(angle) * mRadius).toFloat(),
            (height / 2f + sin(angle) * mRadius).toFloat(),
            paint
        )

        mRadius = width / 2 - 31.389f * w
        paint.apply {
            color = Color.parseColor("#FF9F0A")
            style = Paint.Style.FILL
            strokeWidth = 1.11f * w
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        canvas.drawLine(
            (width / 2f - cos(angle) * mRadius2).toFloat(),
            (height / 2f - sin(angle) * mRadius2).toFloat(),
            (width / 2f + cos(angle) * mRadius).toFloat(),
            (height / 2f + sin(angle) * mRadius).toFloat(),
            paint
        )
    }

    private fun drawMinute(canvas: Canvas, minute: Int) {
        paint.apply {
            color = Color.parseColor("#8E98A1")
            style = Paint.Style.FILL
            strokeWidth = 0.833f * w
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        mRadius = width / 2 - 11.389f * w
        mRadius2 = width / 2 - 21.667f * w
        val angle = Math.PI * minute / 30 - Math.PI / 2
        canvas.drawLine(
            (width / 2f - cos(angle) * mRadius2).toFloat(),
            (height / 2f - sin(angle) * mRadius2).toFloat(),
            (width / 2f + cos(angle) * mRadius).toFloat(),
            (height / 2f + sin(angle) * mRadius).toFloat(),
            paint
        )
    }

    private fun drawHour(canvas: Canvas, hour: Float) {
        paint.apply {
            color = Color.parseColor("#8E98A1")
            style = Paint.Style.FILL
            strokeWidth = 0.833f * w
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        mRadius = width / 2 - 14.44f * w
        mRadius2 = width / 2 - 21.667f * w
        val angle = Math.PI * hour / 30 - Math.PI / 2
        canvas.drawLine(
            (width / 2f - cos(angle) * mRadius2).toFloat(),
            (height / 2f - sin(angle) * mRadius2).toFloat(),
            (width / 2f + cos(angle) * mRadius).toFloat(),
            (height / 2f + sin(angle) * mRadius).toFloat(),
            paint
        )
    }
}