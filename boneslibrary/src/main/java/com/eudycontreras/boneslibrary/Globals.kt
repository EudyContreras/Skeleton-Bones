package com.eudycontreras.boneslibrary

import androidx.core.math.MathUtils
import com.eudycontreras.boneslibrary.properties.Coordinate
import java.lang.NullPointerException
import kotlin.math.hypot

internal const val MIN_OFFSET = 0.0f
internal const val MAX_OFFSET = 1.0f

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 */

/**
 * Maps the given value from the specified minimum to the specified
 * minimum and from the specified maximum to the specified maximum
 * value. Ex:
 * ```
 *  var value = 40f
 *
 *  var fromMin = 0f
 *  var fromMax = 100f
 *
 *  var toMin = 0f
 *  var toMax = 1f
 *
 *  var result = 0.4f
 * ```
 * @param value the value to be transformed
 * @param fromMin the minimum value to map from
 * @param fromMax the maximum value to map from
 * @param toMin the minimum value to map to
 * @param toMax the maximum value to map to
 */
internal fun mapRange(value: Float, fromMin: Float, fromMax: Float, toMin: Float, toMax: Float): Float {
    return mapRange(value, fromMin, fromMax, toMin, toMax, toMin, toMax)
}

/**
 * Maps the given value from the specified minimum to the specified
 * minimum and from the specified maximum to the specified maximum using
 * clamping.
 * value. Ex:
 * ```
 *  var value = 40f
 *
 *  var fromMin = 0f
 *  var fromMax = 100f
 *
 *  var toMin = 0f
 *  var toMax = 1f
 *
 *  var result = 0.4f
 * ```
 * @param value the value to be transformed
 * @param fromMin the minimum value to map from
 * @param fromMax the maximum value to map from
 * @param toMin the minimum value to map to
 * @param toMax the maximum value to map to
 * @param clampMin the minimum value that the function can return
 * @param clampMax the maximum value that the function can return
 */
internal fun mapRange(value: Float, fromMin: Float, fromMax: Float, toMin: Float, toMax: Float, clampMin: Float, clampMax: Float): Float {
    return MathUtils.clamp(
        (value - fromMin) * (toMax - toMin) / (fromMax - fromMin) + toMin,
        clampMin,
        clampMax
    )
}

/**
 * Returns the distance between two coordinates.
 * @param x1 The x axis coordinates of the first point
 * @param y1 The y axis coordinates of the first point
 * @param x2 The x axis coordinates of the second point
 * @param y2 The y axis coordinates of the second point
 */
internal fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Double {
    val x = (x2 - x1)
    val y = (y2 - y1)
    return hypot(x.toDouble(), y.toDouble())
}

/**
 * Returns the distance between two coordinates.
 * @param locationOne The [Coordinate] of the first point
 * @param locationTwo The [Coordinate] of the second point
 */
internal fun distance(locationOne: Coordinate, locationTwo: Coordinate): Double {
    return distance(locationOne.x, locationOne.y, locationTwo.x, locationTwo.y)
}

internal fun tryWith(block: () -> Unit) {
    try {
        block()
    } catch (_: Exception) {
    }
}

internal fun <T> tryGet(block: () -> T): T? {
    return try {
        block()
    } catch (ex: Exception) {
        null
    }
}

internal infix fun <T> T?.or(block: () -> T): T {
    return try {
        if (this == null) {
            throw NullPointerException()
        }
        this
    } catch (ex: Exception) {
        block()
    }
}

internal infix fun <T> T?.or(target: T): T {
    return try {
        if (this == null) {
            throw NullPointerException()
        }
        this
    } catch (ex: Exception) {
        target
    }
}

internal infix fun <T> Boolean?.take(target: T?): T? {
    return if (this == true) {
        target
    } else null
}

internal infix fun <T> T?.tryTake(other: T?): T? {
    return other ?: this
}

internal inline fun <T> doWith(receiver: T, block: (T) -> Unit) {
    block(receiver)
}

internal inline fun <reified T, X> T.map(block: (T) -> X): X {
    return block(this)
}