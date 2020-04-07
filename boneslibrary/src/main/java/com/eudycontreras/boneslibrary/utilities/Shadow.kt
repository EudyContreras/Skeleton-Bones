package com.eudycontreras.boneslibrary.utilities

import android.graphics.BlurMaskFilter
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.MIN_OFFSET
import com.eudycontreras.boneslibrary.extensions.dp
import com.eudycontreras.boneslibrary.mapRange
import com.eudycontreras.boneslibrary.properties.MutableColor
import kotlin.math.*

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since March 2020
 */

internal object Shadow {
    internal const val DEFAULT_COLOR = 0x30000000

    private const val SHADOW_RADIUS_MULTIPLIER = -0.25f
    private const val SHADOW_OFFSET_MULTIPLIER = 0.35f

    private const val SHADOW_TOP_OFFSET = -0.34f
    private const val SHADOW_LEFT_OFFSET = 0.15f
    private const val SHADOW_RIGHT_OFFSET = -0.15f
    private const val SHADOW_BOTTOM_OFFSET = 0.90f

    private const val MIN_SHADOW_ALPHA = 10f
    private const val MAX_SHADOW_ALPHA = 50f

    private const val MIN_SHADOW_RADIUS = 1f
    private const val MAX_SHADOW_RADIUS = 60f

    private val MIN_ELEVATION = 0.dp
    private val MAX_ELEVATION = 24.dp

    internal val COLOR: MutableColor
        get() = MutableColor(DEFAULT_COLOR)


    fun getShadowFilter(elevation: Float): BlurMaskFilter? {
        if (elevation <= 0) return null

        val radius = mapRange(elevation, MIN_ELEVATION, MAX_ELEVATION, MIN_SHADOW_RADIUS, MAX_SHADOW_RADIUS)

        return BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
    }

    fun getShadowColor(color: MutableColor, elevation: Float): MutableColor? {
        if (elevation <= 0) return null

        val delta = mapRange(elevation, MIN_ELEVATION, MAX_ELEVATION, MIN_OFFSET, MAX_OFFSET)

        val alpha = (MAX_SHADOW_ALPHA - abs(MAX_SHADOW_ALPHA * delta) + (MIN_SHADOW_ALPHA * delta))
        val shadow = alpha.roundToInt()

        return color.clone().updateAlpha(shadow)
    }

    private fun getShadowOffsetX(elevation: Float, translationZ: Float = MIN_OFFSET, shadowCompatRotation: Double = 0.0): Int {
        val depth = elevation + translationZ
        val shadowCompatOffset = ceil(depth * SHADOW_OFFSET_MULTIPLIER).toInt()
        return (shadowCompatOffset * sin(Math.toRadians(shadowCompatRotation))).toInt()
    }

    private fun getShadowOffsetY(elevation: Float, translationZ: Float = MIN_OFFSET, shadowCompatRotation: Double = 0.0): Int {
        val depth = elevation + translationZ
        val shadowCompatOffset = ceil(depth * SHADOW_OFFSET_MULTIPLIER).toInt()
        return (shadowCompatOffset * cos(Math.toRadians(shadowCompatRotation))).toInt()
    }

    fun getTopOffset(elevation: Float): Float {
        return (elevation * SHADOW_TOP_OFFSET)
    }

    fun getRadiusTopOffset(elevation: Float): Float {
        return (elevation * SHADOW_OFFSET_MULTIPLIER)
    }

    fun getBottomOffset(elevation: Float): Float {
        return (elevation * SHADOW_BOTTOM_OFFSET)
    }

    fun getLeftOffset(elevation: Float): Float {
        val shift = elevation - (elevation * SHADOW_LEFT_OFFSET)
        return (shift * SHADOW_LEFT_OFFSET)
    }

    fun getRightOffset(elevation: Float): Float {
        val shift = elevation - (elevation * SHADOW_RIGHT_OFFSET)
        return (shift * SHADOW_RIGHT_OFFSET)
    }

    fun getRadiusMultiplier(elevation: Float): Float {
        return (elevation * SHADOW_RADIUS_MULTIPLIER)
    }
}
