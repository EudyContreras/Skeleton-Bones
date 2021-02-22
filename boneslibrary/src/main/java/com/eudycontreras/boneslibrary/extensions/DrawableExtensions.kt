package com.eudycontreras.boneslibrary.extensions

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since March 2020
 */

internal fun Drawable.clone(): Drawable? {
    return this.mutate().constantState?.newDrawable()
}

internal fun Drawable.copy(resources: Resources, width: Int = 1, height: Int = 1): Drawable? {
    val bitmap = this.mutate().drawableToBitmap(width, height)
    return bitmap?.let { BitmapDrawable(resources, bitmap) }
}

internal fun Drawable.drawableToBitmap(width: Int = 1, height: Int = 1): Bitmap? {
    if (this is BitmapDrawable) {
        if (this.bitmap != null) {
            return this.bitmap
        }
    }
    val bitmap = if (width <= 0 || height <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
    }
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}