package com.eudycontreras.boneslibrary.properties

import com.eudycontreras.boneslibrary.properties.Bounds.Side.*
import kotlin.math.abs

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 */

data class Bounds(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float
) {
    constructor() : this(0f, 0f, 0f, 0f)

    val left: Float
        get() = x

    val right: Float
        get() = x + width

    val top: Float
        get() = y

    val bottom: Float
        get() = y + height

    fun toLeftOf(other: Bounds): Bounds {
        return copy(
            x = other.x + other.width
        )
    }

    fun toRightOF(other: Bounds): Bounds {
        return copy(
            x = other.x - width
        )
    }

    fun toTopOf(other: Bounds): Bounds {
        return copy(
            y = other.y - height
        )
    }

    fun toBottomOf(other: Bounds): Bounds {
        return copy(
            y = other.y + other.height
        )
    }

    fun centerX(): Float {
        return (left + right) * 0.5f
    }

    fun centerY(): Float {
        return (top + bottom) * 0.5f
    }

    fun pad(amount: Float, side: Side = ALL) {
        return when (side) {
            TOP ->  y += amount
            LEFT -> y += amount
            RIGHT -> width -= amount
            BOTTOM -> height -= amount
            ALL -> {
                pad(amount, LEFT)
                pad(amount, RIGHT)
                pad(amount, TOP)
                pad(amount, BOTTOM)
            }
        }
    }

    fun padPercent(percentage: Float, side: Side = ALL) {
        return when (side) {
            TOP -> y *= percentage
            LEFT -> x *= percentage
            RIGHT -> width *= (width * percentage)
            BOTTOM -> height *= (height * percentage)
            ALL -> {
                padPercent(percentage, LEFT)
                padPercent(percentage, RIGHT)
                padPercent(percentage, TOP)
                padPercent(percentage, BOTTOM)
            }
        }
    }

    operator fun plus(amount: Float): Bounds {
        return this.copy(
            width = width + amount,
            height = height + amount
        )
    }

    operator fun minus(amount: Float): Bounds {
        return this.copy(
            width = width - amount,
            height = height - amount
        )
    }

    operator fun times(amount: Float): Bounds {
        return this.copy(
            width = width * amount,
            height = height * amount
        )
    }

    fun getVerticalOverlap(other: Bounds): Float {
        val top1 = this.top
        val top2 = other.top

        val bottom1 = this.bottom
        val bottom2 = other.bottom

        return if (top1 > top2) {
            abs(bottom1 - top2)
        } else {
            abs(bottom2 - top1)
        }
    }

    fun intercepts(other: Bounds): Boolean {
        return x < other.x + other.width && x + width > other.x && y < other.y + other.height && y + height > other.y
    }

    fun verticalIntercepts(other: Bounds): Boolean {
        return  y < other.y + other.height && y + height > other.y
    }

    fun horizontalIntercepts(other: Bounds): Boolean {
        return x < other.x + other.width && x + width > other.x
    }

    fun interceptsAny(other: List<Bounds>): Boolean {
        return other.any { it.intercepts(this) }
    }

    fun isInside(x: Float, y: Float): Boolean {
        return (x > left && x < right && y > top && y < bottom)
    }

    fun isInside(bounds: Bounds): Boolean {
        return isInside(bounds.top, bounds.left, bounds.bottom, bounds.right)
    }

    fun isInside(top: Float, left: Float, bottom: Float, right: Float): Boolean {
        return this.top >= top && this.left >= left && this.bottom <= bottom && this.right <= right
    }

    enum class Side {
        TOP, LEFT, RIGHT, BOTTOM, ALL
    }
}
