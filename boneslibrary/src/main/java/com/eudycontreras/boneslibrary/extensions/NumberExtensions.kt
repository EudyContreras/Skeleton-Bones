package com.eudycontreras.boneslibrary.extensions

import android.content.res.Resources

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 */

val Int.f: Float
    get() = this.toFloat()

val Float.i: Int
    get() = this.toInt()

/**
 * Use the float as density independent pixels and return the pixel value
 */
val Int.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density

val Float.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density

/**
 * Use the float as scale independent pixels and return the pixel value
 */
val Int.sp: Float
    get() = this * Resources.getSystem().displayMetrics.scaledDensity

val Float.sp: Float
    get() = this * Resources.getSystem().displayMetrics.scaledDensity


fun getDps(value: Float): Float {
    return value.dp
}

internal fun Float.clamp(min: Float? = null, max: Float? = null): Float {
    if (min == null && max == null) return this
    return when {
        this < min ?: this -> min ?: this
        this > max ?: this -> max ?: this
        else -> this
    }
}