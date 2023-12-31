package com.remi.alarmclock.xtreme.free.utils

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import com.remi.alarmclock.xtreme.free.callback.ICallBackItem
import com.remi.alarmclock.xtreme.free.extensions.modifyOrientation
import java.io.*

object UtilsBitmap {

    fun drawIconWithPath(canvas: Canvas, path: Path, paint: Paint?, size: Float, x: Int, y: Int) {
        val rectF = RectF()
        path.computeBounds(rectF, true)
        val scale = if (rectF.width() >= rectF.height()) size / rectF.width()
        else size / rectF.height()
        canvas.translate(x.toFloat(), y.toFloat())
        canvas.scale(scale, scale)
        canvas.drawPath(path, paint!!)
    }

    fun toRGBString(color: Int): String {
        // format: #RRGGBB
        var red = Integer.toHexString(Color.red(color))
        var green = Integer.toHexString(Color.green(color))
        var blue = Integer.toHexString(Color.blue(color))
        if (red.length == 1) red = "0$red"
        if (green.length == 1) green = "0$green"
        if (blue.length == 1) blue = "0$blue"
        return "#$red$green$blue"
    }

    fun toARGBString(alpha: Int, color: Int): String {
        // format: #AARRGGBB
        var hex = Integer.toHexString(alpha).uppercase()
        var red = Integer.toHexString(Color.red(color))
        var green = Integer.toHexString(Color.green(color))
        var blue = Integer.toHexString(Color.blue(color))
        if (hex.length == 1) hex = "0$hex"
        if (red.length == 1) red = "0$red"
        if (green.length == 1) green = "0$green"
        if (blue.length == 1) blue = "0$blue"
        return "#$hex$red$green$blue"
    }

    fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
        val drawable = context?.let { ContextCompat.getDrawable(it, drawableId) }
        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        if (bitmap != null) {
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
        return null
    }

    fun getImageSize(context: Context, uri: Uri?): FloatArray {
        try {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            val input = context.contentResolver.openInputStream(uri!!)
            BitmapFactory.decodeStream(input, null, options)
            input!!.close()
            return floatArrayOf(options.outWidth.toFloat(), options.outHeight.toFloat())
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
        return floatArrayOf(0f, 0f)
    }

    fun createByteImage(context: Context, uriPic: String?, callBack: ICallBackItem) {
        val bmp: Bitmap
        try {
            bmp = getBitmapFromUri(context, Uri.parse(uriPic))!!.modifyOrientation(context, Uri.parse(uriPic))
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 84, stream)
            callBack.callBack(stream.toByteArray(), -1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun createByteAudio(uriAudio: String?, callBack: ICallBackItem) {
        try {
            val inputStream = FileInputStream(uriAudio)
            val os = ByteArrayOutputStream()
            val buffer = ByteArray(0xFFFF)
            var len = inputStream.read(buffer)
            while (len != -1) {
                os.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
            callBack.callBack(os.toByteArray(), -1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun saveBitmapToApp(
        context: Context,
        bitmap: Bitmap,
        nameFolder: String,
        nameFile: String
    ): String {
        val directory = Utils.getStore(context) + "/" + nameFolder + "/"
        val myPath = File(directory, "$nameFile.png")
        try {
            val fos = FileOutputStream(myPath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            return myPath.path
        } catch (e: Exception) {
            Log.e("SAVE_IMAGE", e.message, e)
        }
        return ""
    }

    fun getBitmapFromUri(context: Context, selectedFileUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(selectedFileUri!!, "r")
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    fun getBitmapFromAsset(context: Context, nameFolder: String, name: String): Bitmap? {
        val istr: InputStream
        var bitmap: Bitmap? = null
        try {
            istr = context.assets.open("$nameFolder/$name".replace("//", "/"))
            bitmap = BitmapFactory.decodeStream(istr)
        } catch (e: IOException) {
            // handle exception
            e.printStackTrace()
        }
        return bitmap
    }
}