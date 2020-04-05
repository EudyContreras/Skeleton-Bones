package com.eudycontreras.boneslibrary.properties

import com.eudycontreras.boneslibrary.common.Cloneable

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 *
 * Class which holds information about corner radius for all
 * 4 corners of a rounded rectangle
 */

class CornerRadii(
    topLeft: Float = 0f,
    topRight: Float = 0f,
    bottomRight: Float = 0f,
    bottomLeft: Float = 0f
): Cloneable<CornerRadii> {

    val topLeft: Float
        get() = corners[0]

    val topRight: Float
        get() = corners[2]

    val bottomRight: Float
        get() = corners[4]

    val bottomLeft: Float
        get() = corners[6]

    val corners = FloatArray(SIZE)

    val size: Int
        get() = corners.size

    init {
        apply(topLeft, topRight, bottomRight, bottomLeft)
    }

    constructor(radii: FloatArray) : this(radii[0], radii[2], radii[4], radii[6])

    constructor(radii: Float) : this(radii, radii, radii, radii)

    fun apply(cornerRadii: CornerRadii) {
        for (index in 0 until cornerRadii.size) {
            corners[index] = cornerRadii[index]
        }
    }

    fun apply(cornerRadii: FloatArray) {
        for (index in cornerRadii.indices) {
            corners[index] = cornerRadii[index]
        }
    }

    fun apply(
        topLeft: Float = 0f,
        topRight: Float = 0f,
        bottomRight: Float = 0f,
        bottomLeft: Float = 0f
    ) {
        corners[0] = topLeft
        corners[1] = topLeft

        corners[2] = topRight
        corners[3] = topRight

        corners[4] = bottomRight
        corners[5] = bottomRight

        corners[6] = bottomLeft
        corners[7] = bottomLeft
    }

    fun setTopLeft(radii: Float) {
        corners[0] = radii
        corners[1] = radii
    }

    fun setTopRight(radii: Float) {
        corners[2] = radii
        corners[3] = radii
    }

    fun setBottomRight(radii: Float) {
        corners[4] = radii
        corners[5] = radii
    }

    fun setBottomLeft(radii: Float) {
        corners[6] = radii
        corners[7] = radii
    }

    override fun clone(): CornerRadii {
        return CornerRadii(
            topLeft = topLeft,
            topRight = topRight,
            bottomLeft = bottomLeft,
            bottomRight = bottomRight
        )
    }

    operator fun get(index: Int): Float {
        return corners[index]
    }

    operator fun set(index: Int, value: Float) {
        corners[index] = value
    }

    override fun toString(): String {
        return "TL: ${corners[0]} TR: ${corners[2]} BR: ${corners[4]} BL: ${corners[6]}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CornerRadii) return false

        if (!corners.contentEquals(other.corners)) return false

        return true
    }

    override fun hashCode(): Int {
        return corners.contentHashCode()
    }

    companion object {
        const val SIZE = 8
        fun create(corners: Float) = CornerRadii(corners)

        @JvmStatic
        fun create(
            topLeft: Float,
            topRight: Float,
            bottomRight: Float,
            bottomLeft: Float
        ) = CornerRadii(topLeft, topRight, bottomRight, bottomLeft)
    }
}
