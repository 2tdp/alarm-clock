package com.remi.alarmclock.xtreme.free.addview.addalarm

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.utils.Utils
import com.remi.alarmclock.xtreme.free.utils.UtilsBitmap
import kotlin.math.*

@SuppressLint("ViewConstructor")
class ViewCustomHourAlarm(context: Context, isSwipe: ICallBackCheck) : View(context) {

    companion object {
        var w = 0F
    }

    private val context: Context
    private val isSwipe: ICallBackCheck
    var textHour = "06"
    var textMinutes = "00"

    private val paintGradient: Paint
    var isCreate = true
    private var degreeHour = 0

    private val paintCircle: Paint

    private val paintVector: Paint
    private val bm: Bitmap
    private var rectBm: Rect

    val paint: Paint
    var sizeCircle1 = 0f
    var sizeCircle2 = 0f
    private val rectText = Rect()
    var currentDegree = 0F
    private var moveDegree = 0F
    var offSet = 0f

    private var mRadius = 0F

    init {
        this.context = context
        this.isSwipe = isSwipe
        w = resources.displayMetrics.widthPixels / 100F
        isClickable = true
        isFocusable = true

        paintGradient = Paint().apply {
            isAntiAlias
            textSize = 5.556f * w
            typeface = Utils.getTypeFace("display", "display_regular.otf", context)
        }
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 0.33f * w
//            typeface = Utils.getTypeFace("display", "display_regular.otf", context)
        }

        paintVector = Paint(Paint.ANTI_ALIAS_FLAG)

        bm = Bitmap.createScaledBitmap(
            UtilsBitmap.getBitmapFromAsset(context, "icon", "ic_vector_down.png")!!,
            (3.33f * w).toInt(), (7.778f * w).toInt(), false
        )
        rectBm = Rect()

        paintCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            typeface = Utils.getTypeFace("display", "display_regular.otf", context)
        }
        sizeCircle1 = 20.833f * w
        sizeCircle2 = 2.778f * w
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (isCreate) {
            isCreate = false
            paintGradient.shader = LinearGradient(
                0f, 0f, 0f,
                height.toFloat(),
                ContextCompat.getColor(context, R.color.white2),
                ContextCompat.getColor(context, R.color.white),
                Shader.TileMode.MIRROR
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawCircle(
            canvas,
            ContextCompat.getColor(context, R.color.black_background5),
            ContextCompat.getColor(context, R.color.black_background4),
            width / 2f - sizeCircle2)
        drawHours(canvas)

        canvas.save()
        rectBm.set(
            width / 2 - bm.width / 2,
            width / 2 + (width / 2f - sizeCircle1).toInt(),
            width / 2 + bm.width / 2,
            width / 2 + (width / 2f - sizeCircle1).toInt() + bm.height,
        )
        canvas.drawBitmap(bm, null, rectBm, paintVector)
        canvas.restore()

        drawCircle(
            canvas,
            ContextCompat.getColor(context, R.color.black_background2),
            ContextCompat.getColor(context, R.color.black_background4),
            width / 2f - sizeCircle1)


        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        val time = "$textHour:$textMinutes"
        paint.apply {
            color = Color.WHITE
            textSize = 8.889f * w
            getTextBounds(time, 0, time.length, rectText)
        }

        canvas.drawText(time, width / 2f - rectText.width() / 2f, height / 2f - rectText.height() / 2f, paint)
    }

    private fun drawHours(canvas: Canvas) {
        for (i in 1..12) {
            mRadius = width / 2 - sizeCircle2 - 4.44f * w
            val degree = (offSet.toInt() + (i - 3) * 30f - degreeHour) % 360

            val angle = degree * Math.PI / 180
            val tmp = i.toString()
            paintGradient.getTextBounds(tmp, 0, tmp.length, rectText) // f
            // find the circle-wise (x, y) position as mathematical rule
            val x = (width / 2 + (cos(angle) * mRadius).toFloat() - rectText.width() / 2)
            val y = (height / 2 + (sin(angle) * mRadius).toFloat() + rectText.height() / 2)

            if (x in (width / 2f - rectText.width() / 2f - 10)..(width / 2f - rectText.width() / 2f + 10)
                && y in (height - sizeCircle2 - rectText.height())..(height - sizeCircle2 + rectText.height())) {
                paint.color = ContextCompat.getColor(context, R.color.main_color)
                paint.textSize = 5.556f * w
                canvas.drawText(tmp, x, y, paint)

                textHour = if (i < 10) "0$i"
                else i.toString()
            } else canvas.drawText(tmp, x, y, paintGradient)
        }
    }

    private fun drawCircle(canvas: Canvas, colorCir: Int, colorShadow: Int, radius: Float) {
        paintCircle.apply {
            color = colorCir
            setShadowLayer(15f, 0f, 0f, colorShadow)
        }
        canvas.drawCircle(width / 2f, height / 2f, radius, paintCircle)
    }

    fun setHour(hour: Int) {
        this.degreeHour = 360 - (6 - hour) * 30

        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isSwipe.check(true)
                val dx = width / 2 - event.x
                val dy = height / 2 - event.y
                currentDegree = (atan2(dy, dx) * 180 / Math.PI).toFloat() - offSet

                if (currentDegree < 0) currentDegree = 360 - (currentDegree * -1)
                currentDegree -= 180f
                if (currentDegree < 0) currentDegree += 360f
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = width / 2 - event.x
                val dy = height / 2 - event.y
                moveDegree = (atan2(dy, dx) * 180 / Math.PI).toFloat()

                if (moveDegree < 0) moveDegree = 360 - (moveDegree * -1)
                moveDegree -= 180f
                if (moveDegree < 0) moveDegree += 360f

                if (moveDegree == currentDegree) return false
                offSet = moveDegree - currentDegree

                invalidate()
            }
            MotionEvent.ACTION_UP -> isSwipe.check(false)
        }
        return true
    }


}