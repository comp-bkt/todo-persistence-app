package com.example.todolistapp

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point

object PictureUtils {
    fun getScaledBitmap(path: String?, destWidth: Int, destHeight: Int): Bitmap {
        // get image dimensions from image on disk
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()

        // calculate scale factor
        var inSampleSize = 1
        if (srcHeight > destHeight || srcWidth > destWidth) {
            val heightScale = srcHeight / destHeight
            val widthScale = srcWidth / destWidth
            inSampleSize = Math.round(Math.max(heightScale, widthScale))
        }
        options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize

        // read in and create the scaled bitmap
        return BitmapFactory.decodeFile(path, options)
    }

    fun getScaledBitmap(path: String?, activity: Activity?): Bitmap {
        val size = Point()
        activity!!.windowManager.defaultDisplay.getSize(size)
        return getScaledBitmap(path, size.x, size.y)
    }
}