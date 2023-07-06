package com.remi.alarmclock.xtreme.free.addview.addalarm

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.R
import com.remi.alarmclock.xtreme.free.callback.ICallBackCheck
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.utils.Utils
import com.remi.alarmclock.xtreme.free.utils.UtilsBitmap
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("ViewConstructor")
class ViewCustomMinuteAlarm(context: Context, callBack: ICallBackItem, isSwipe: ICallBackCheck) : View(context) {

    companion object {
        var w = 0F
    }

    private val context: Context
    private val callBack: ICallBackItem
    private val isSwipe: ICallBackCheck
    private val paintGradient: Paint
    var isCreate = true
    private var degreeMinute = 0

    private val paintCircle: Paint

    private val paintVector: Paint
    private val bm: Bitmap
    private var rectBm: Rect

    val paint: Paint
    var sizeCircle = 0f
    private val rectText = Rect()
    var currentDegree = 0F
    var moveDegree = 0F
    var offSet = 0f

    private var mRadius = 0F

    init {
        this.context = context
        this.callBack = callBack
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
            typeface = Utils.getTypeFace("display", "display_regular.otf", context)
        }

        paintVector = Paint(Paint.ANTI_ALIAS_FLAG)

        bm = Bitmap.createScaledBitmap(
            UtilsBitmap.getBitmapFromAsset(context, "icon", "ic_vector_down.png")!!,
            (3.33f * w).toInt(), (7.778f * w).toInt(), false
        )
        rectBm = Rect()

        paintCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
        sizeCircle = 11.667f * w
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
            ContextCompat.getColor(context, R.color.black_background),
            ContextCompat.getColor(context, R.color.white3),
            width / 2f - sizeCircle
        )

        canvas.save()
        rectBm.set(
            (width / 2 - bm.width / 2),
            width / 2 + (width / 2f - sizeCircle - 18.889f * w).toInt(),
            width / 2 + bm.width / 2,
            width / 2 + (width / 2f - sizeCircle - 18.889f * w).toInt() + bm.height,
        )
        canvas.drawBitmap(bm, null, rectBm, paintVector)
        canvas.restore()

        drawMinutes(canvas)
    }

    private fun drawMinutes(canvas: Canvas) {
        for (i in 1..60) {
            mRadius = width / 2 - sizeCircle - 4.44f * w
            val degree = offSet.toInt() + (i + 15) * 6f - degreeMinute
            val angle = degree * Math.PI / 180

            if (i % 5 != 0) {
//                canvas.save()
//                canvas.rotate(degree, width / 2f, height / 2f)
//
//                if (degree in (178f - degreeMinute)..(184f - degreeMinute)) {
//                    paint.color = ContextCompat.getColor(context, R.color.main_color)
//                    if (i < 15) callBack.callBack((i + 45).toString(), i)
//                    else callBack.callBack(if ((i - 15) < 10) "0${i - 15}" else (i - 15).toString(), i)
//                } else paint.color = ContextCompat.getColor(context, R.color.white)
//
//                canvas.drawLine(
//                    width / 2F,
//                    2.22f * w + sizeCircle,
//                    width / 2f,
//                    30 + 2.22f * w + sizeCircle, paint
//                )
//                canvas.restore()
                val x = (width / 2 + (cos(angle) * mRadius).toFloat() - rectText.width() / 2)
                val y = (height / 2 + (sin(angle) * mRadius).toFloat() + rectText.height() / 2)

                if (x in (width / 2f - rectText.width() / 2f - 10)..(width / 2f - rectText.width() / 2f + 10)
                    && y in (height - sizeCircle - rectText.height())..(height - sizeCircle + rectText.height())
                ) {
                    paint.color = ContextCompat.getColor(context, R.color.main_color)
//                    if (i < 15) callBack.callBack((i + 45).toString(), i)
//                    else callBack.callBack(if ((i - 15) < 10) "0${i - 15}" else (i - 15).toString(), i)
                    callBack.callBack(if (i < 10) "0$i" else i, i)
                } else paint.color = ContextCompat.getColor(context, R.color.white)

                canvas.drawLine(
                    (width / 2f + cos(angle) * (mRadius + 2.5f * w)).toFloat(),
                    (height / 2f + sin(angle) * (mRadius + 2.5f * w)).toFloat(),
                    (width / 2f + cos(angle) * mRadius).toFloat(),
                    (height / 2f + sin(angle) * mRadius).toFloat(),
                    paint
                )
            } else {
                var tmp = i.toString()
                if (tmp == "60") tmp = "00"
                else if (tmp == "5") tmp = "05"
                paintGradient.getTextBounds(tmp, 0, tmp.length, rectText) // f
                // find the circle-wise (x, y) position as mathematical rule
                val x = (width / 2 + (cos(angle) * mRadius).toFloat() - rectText.width() / 2)
                val y = (height / 2 + (sin(angle) * mRadius).toFloat() + rectText.height() / 2)

                if (x in (width / 2f - rectText.width() / 2f - 10)..(width / 2f - rectText.width() / 2f + 10)
                    && y in (height - sizeCircle - rectText.height())..(height - sizeCircle + rectText.height())
                ) {
                    paint.color = ContextCompat.getColor(context, R.color.main_color)
                    paint.textSize = 5.556f * w
                    canvas.drawText(tmp, x, y, paint)

                    callBack.callBack(tmp, i)
                } else canvas.drawText(tmp, x, y, paintGradient)
            }
        }
    }

    private fun drawCircle(canvas: Canvas, colorCir: Int, colorShadow: Int, radius: Float) {
        paintCircle.apply {
            color = colorCir
            setShadowLayer(15f, 0f, 0f, colorShadow)
        }
        canvas.drawCircle(width / 2f, height / 2f, radius, paintCircle)
    }

    fun setMinutes(minute: Int) {
        this.degreeMinute = 360 + minute * 6

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