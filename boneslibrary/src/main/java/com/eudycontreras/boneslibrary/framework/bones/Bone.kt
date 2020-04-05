package com.eudycontreras.boneslibrary.framework.bones

import android.graphics.*
import android.view.View
import android.widget.TextView
import com.eudycontreras.boneslibrary.AndroidColor
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.MIN_OFFSET
import com.eudycontreras.boneslibrary.common.*
import com.eudycontreras.boneslibrary.extensions.addCircle
import com.eudycontreras.boneslibrary.extensions.addRoundRect
import com.eudycontreras.boneslibrary.extensions.horizontalPadding
import com.eudycontreras.boneslibrary.extensions.verticalPadding
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties.Companion.OVERFLOW_THRESHOLD
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRay
import com.eudycontreras.boneslibrary.properties.Color
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 */

internal class Bone(
    val owner: View,
    val boneProperties: BoneProperties
) : DrawableShape(), FadeTarget, UpdateTarget, Disposable, ContentLoader {

    private val shimmerRays: MutableList<ShimmerRay> = mutableListOf()

    private var shapeType: ShapeType = ShapeType.RECTANGULAR

    private val overMode: PorterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)

    private fun getLength(owner: View): Float {
        return owner.measuredWidth.toFloat() - if (boneProperties.dissectBones == true) {
            owner.horizontalPadding
        } else MIN_OFFSET
    }

    private fun getThickness(owner: View): Float {
        return owner.measuredHeight.toFloat() - if (boneProperties.dissectBones == true) {
            owner.verticalPadding
        } else MIN_OFFSET
    }

    fun compute(view: View) {
        if (boneProperties.color != null) {
            this.color = MutableColor.fromColor(boneProperties.color)
        }

        this.shapeType = boneProperties.shapeType ?: return
        this.corners = boneProperties.cornerRadii?.clone() ?: CornerRadii()

        if (this.color != null) {
            if (boneProperties.shadeMultiplier != MAX_OFFSET) {
                this.color = this.color?.adjust(boneProperties.shadeMultiplier)
            }
        }

        this.bounds.width = boneProperties.width ?: getLength(owner)
        this.bounds.height = boneProperties.height ?: getThickness(owner)

        this.bounds.x = (view.measuredWidth.toFloat() / 2) - this.bounds.width / 2
        this.bounds.y = (view.measuredHeight.toFloat() / 2) - this.bounds.height / 2

        boneProperties.getRayShimmerProps()?.let {
            shimmerRays.clear()
            ShimmerRay.createRays(
                bounds = bounds,
                shimmerRays = shimmerRays,
                properties = it
            )
        }
    }

    override fun restore() { }

    override fun concealContent() { }

    override fun revealContent() { }

    override fun prepareContentFade() {
        owner.alpha = MIN_OFFSET
        owner.visibility = View.VISIBLE
    }

    override fun fadeContent(fraction: Float) {
        owner.alpha = MAX_OFFSET * fraction
    }

    override fun onUpdate(fraction: Float) {
        if (boneProperties.disposed) return

        if (shimmerRays.size > 0) {
            for (ray in shimmerRays) {
                ray.onUpdate(fraction)
            }
        }
    }

    override fun onFade(fraction: Float) {
        if (boneProperties.disposed) return

        opacity = Color.MAX_COLOR - (Color.MAX_COLOR * fraction).toInt()

        if (shimmerRays.size > 0) {
            for (ray in shimmerRays) {
                ray.onFade(fraction)
            }
        }
    }

    override fun dispose() {
        shimmerRays.clear()
    }

    fun onRender(
        canvas: Canvas,
        paint: Paint,
        path: Path,
        rayPath: Path
    ) {
        if (boneProperties.disposed) return

        buildPaths(path, rayPath)

        if (this.color != null) {
            paint.shader = null
            paint.color = this.color?.toColor() ?: AndroidColor.TRANSPARENT
            paint.style = Paint.Style.FILL

            canvas.drawPath(path, paint)
        }

        paint.xfermode = overMode

        if (shimmerRays.size > 0) {
            for (ray in shimmerRays) {
                ray.onRender(canvas, paint, rayPath)
            }
        }
    }

    private fun buildPaths(path: Path, rayPath: Path) {
        when (shapeType) {
            ShapeType.RECTANGULAR -> {
                val isTextView = owner is TextView
                val maxThickness = boneProperties.maxThickness
                val highRatio = height >= maxThickness * OVERFLOW_THRESHOLD
                val dissectBones = boneProperties.height == null && boneProperties.dissectBones == true

                if (dissectBones && isTextView && highRatio) {
                    val part = boneProperties.sectionDistance + maxThickness
                    val times = ((bounds.height - maxThickness) / part).toInt()
                    var padding = maxThickness / 2

                    for (it in 0..times) {
                        val top = bounds.top + (it * maxThickness) + padding
                        val bottom = top + maxThickness

                        path.addRoundRect(bounds, radii, top = top, bottom = bottom)
                        rayPath.addRoundRect(bounds, radii, top = top, bottom = bottom)
                        padding += boneProperties.sectionDistance
                    }
                } else {
                    path.addRoundRect(bounds, radii)
                    rayPath.addRoundRect(bounds, radii)
                }
            }
            ShapeType.CIRCULAR -> {
                path.addCircle(radius, radius, radius)
                rayPath.addCircle(radius, radius, radius)
            }
        }
    }

    companion object {
        @JvmStatic
        fun build(view: View, properties: BoneProperties, manager: BoneManager): Bone {
            return Bone(view, properties).apply {
                this.elevation = view.elevation
                this.boneProperties.enabledListener = {
                    if (!it) {
                        manager.dispose(this)
                    }
                }
            }
        }
    }
}
