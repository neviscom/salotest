package com.aviatest.coreui.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import com.aviatest.coreui.extentions.dp
import javax.inject.Inject

class BitmapUtils @Inject constructor() {

    fun drawTextOnCenterOfDrawable(drawable: Drawable, text: String): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null

        var bitmapConfig = bitmap.config;
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }

        val mutableBitmap = bitmap.copy(bitmapConfig, true)
        bitmap.recycle()

        val canvas = Canvas(mutableBitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 16.dp
        paint.color = Color.WHITE

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = (mutableBitmap.width - bounds.width()) / 2f
        val y = (mutableBitmap.height + bounds.height()) / 2f
        canvas.drawText(text, x, y, paint)

        return mutableBitmap
    }
}