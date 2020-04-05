package com.eudycontreras.boneslibrary.properties

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode.CLAMP
import android.graphics.Shader.TileMode.MIRROR

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 */

internal data class Gradient(
    val colors: Array<MutableColor>,
    val type: Int = TOP_TO_BOTTOM
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Gradient) return false

        if (!colors.contentEquals(other.colors)) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = colors.contentHashCode()
        result = 31 * result + type
        return result
    }

    companion object {
        const val TOP_TO_BOTTOM = 0
        const val BOTTOM_TO_TOP = 1
        const val LEFT_TO_RIGHT = 2
        const val RIGHT_TO_LEFT = 3

        fun getShader(
            gradient: Gradient,
            x: Float,
            y: Float,
            width: Float,
            height: Float
        ): Shader {
            val zero = 0f
            when (gradient.type) {
                TOP_TO_BOTTOM -> return LinearGradient(
                    zero,
                    y,
                    zero,
                    y + height,
                    gradient.colors.map { it.toColor() }
                        .toIntArray(),
                    getPositions(gradient.colors.size),
                    MIRROR
                )
                BOTTOM_TO_TOP -> return LinearGradient(
                    zero,
                    y + height,
                    zero,
                    y,
                    gradient.colors.map { it.toColor() }
                        .toIntArray(),
                    getPositions(gradient.colors.size),
                    CLAMP
                )
                LEFT_TO_RIGHT -> return LinearGradient(
                    x,
                    zero,
                    x + width,
                    zero,
                    gradient.colors.map { it.toColor() }
                        .toIntArray(),
                    getPositions(gradient.colors.size),
                    CLAMP
                )
                RIGHT_TO_LEFT -> return LinearGradient(
                    x + width,
                    zero,
                    x,
                    zero,
                    gradient.colors.map { it.toColor() }
                        .toIntArray(),
                    getPositions(gradient.colors.size),
                    CLAMP
                )
                else -> return LinearGradient(
                    x,
                    y,
                    width,
                    height,
                    gradient.colors.map { it.toColor() }
                        .toIntArray(),
                    getPositions(gradient.colors.size),
                    CLAMP
                )
            }
        }

        private fun getPositions(count: Int): FloatArray {
            val value: Float = 1f / (count - 1)
            val array = ArrayList<Float>()
            var increase = value
            array.add(0f)
            for (i in 0 until count - 1) {
                array.add(increase)
                increase += (value)
            }
            return array.toFloatArray()
        }
    }
}
