package com.eudycontreras.boneslibrary.framework.shimmer

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import com.eudycontreras.boneslibrary.AndroidColor
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.MIN_OFFSET
import com.eudycontreras.boneslibrary.common.DrawableShape
import com.eudycontreras.boneslibrary.common.FadeTarget
import com.eudycontreras.boneslibrary.common.RenderTarget
import com.eudycontreras.boneslibrary.common.UpdateTarget
import com.eudycontreras.boneslibrary.framework.bones.BoneDrawable
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.mapRange
import com.eudycontreras.boneslibrary.properties.Bounds
import com.eudycontreras.boneslibrary.properties.Color
import com.eudycontreras.boneslibrary.properties.Gradient
import com.eudycontreras.boneslibrary.properties.MutableColor
import kotlin.math.abs

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 * A visual representation of a shimmer. The shimmer ray
 * is used for portraying a loading state for a Skeleton or BoneDrawable.
 * The **ShimmerRay** is manipulated through the **ShimmerRayProperties**
 * For a more complete description please look
 * at the [Skeleton-Bones](https://github.com/EudyContreras/Skeleton-Bones) library on GitHub.
 *
 * Each instance of a Skeleton or BoneDrawable has a property class.
 * Each property class provides access to a **ShimmerRayProperties** class.
 *
 * @See BoneDrawable
 * @see SkeletonDrawable
 * @see ShimmerRayProperties
 */

class ShimmerRay internal constructor(
    private val properties: ShimmerRayProperties
) : DrawableShape(), RenderTarget, UpdateTarget, FadeTarget {

    internal var startOffset: Float = MIN_OFFSET
    internal var endOffset: Float = MIN_OFFSET

    private var tilt: Float = MIN_OFFSET
    private var maxX: Float = MIN_OFFSET

    private val matrix: Matrix = Matrix()
    private var remaining: List<Int> = emptyList()
    private var gradient: Gradient = Gradient(emptyArray())

    override fun onUpdate(fraction: Float) {
        val value = mapRange(fraction, startOffset, endOffset, MIN_OFFSET, MAX_OFFSET)

        val interpolatedValue = if (!properties.shimmerRaySharedInterpolator) {
            properties.shimmerRayInterpolator?.getInterpolation(value) ?: MAX_OFFSET
        } else value

        val offset = height * abs(tilt)

        bounds.x = ((maxX + offset) * interpolatedValue) - (strokeWidth + offset)

        matrix.reset()

        matrix.postTranslate(bounds.x, bounds.y)

        matrix.setSkew(tilt, tilt, width / 2f, height / 2f)
    }

    override fun onFade(fraction: Float) {
        gradient.colors.forEachIndexed { index, value ->
            val remaining = Color.MAX_COLOR - remaining[index]
            value.subtractAlpha((remaining * fraction).toInt())
        }
    }

    override fun onRender(
        canvas: Canvas,
        paint: Paint,
        path: Path
    ) {
        if (color != null) {
            paint.shader = null
            paint.style = Paint.Style.FILL
            paint.color = color?.toColor() ?: AndroidColor.TRANSPARENT
        }

        paint.shader = Gradient.getShader(gradient, x, y, width, height)
        paint.shader.setLocalMatrix(matrix)

        canvas.drawPath(path, paint)
    }

    internal fun recompute(bounds: Bounds, properties: ShimmerRayProperties) {
        val ratio = properties.shimmerRayThicknessRatio * bounds.width
        val thickness = properties.shimmerRayThickness ?: ratio

        val offset = bounds.height * abs(properties.shimmerRayTilt)

        this.strokeWidth = thickness
        this.width = thickness
        this.height = bounds.height
        this.maxX = bounds.right + (thickness + offset)
        this.tilt = properties.shimmerRayTilt
        this.x = bounds.x - (thickness + offset)
        this.y = bounds.y
    }

    companion object {

        private const val MIN_ALPHA: Float = MIN_OFFSET
        private const val MAX_ALPHA: Float= 0.35f

        private fun build(bounds: Bounds, properties: ShimmerRayProperties): ShimmerRay {
            val ratio = properties.shimmerRayThicknessRatio * bounds.width
            val thickness = properties.shimmerRayThickness ?: ratio
            val rayColor = properties.shimmerRayColor
            val gradient = Gradient(arrayOf(
                MutableColor.fromColor(rayColor).updateAlpha(MIN_ALPHA),
                MutableColor.fromColor(rayColor).updateAlpha(MAX_ALPHA),
                MutableColor.fromColor(rayColor).updateAlpha(MIN_ALPHA)
            ), Gradient.LEFT_TO_RIGHT)

            return ShimmerRay(
                properties
            ).apply {
                val offset = bounds.height * abs(properties.shimmerRayTilt)

                this.strokeWidth = thickness
                this.width = thickness
                this.height = bounds.height
                this.maxX = bounds.right + (thickness + offset)
                this.tilt = properties.shimmerRayTilt
                this.color = MutableColor.fromColor(rayColor)
                this.x = bounds.x - (thickness + offset)
                this.y = bounds.y
                this.gradient = gradient
                this.remaining = gradient.colors.map { it.alpha }
            }
        }

        internal fun createRays(
            bounds: Bounds,
            shimmerRays: MutableList<ShimmerRay>,
            properties: ShimmerRayProperties
        ) {
            val shimmerRayCount = properties.shimmerRayCount

            if (shimmerRayCount <= 0) return

            val offsetIncrease = MAX_OFFSET / shimmerRayCount

            var endOffset = MIN_OFFSET

            for (i in 0 until shimmerRayCount) {
                val startOffset = endOffset / 2
                endOffset = (i * offsetIncrease) + offsetIncrease

                shimmerRays.add(build(bounds, properties).apply {
                    this.startOffset = startOffset
                    this.endOffset = endOffset
                })
            }
        }
    }
}
