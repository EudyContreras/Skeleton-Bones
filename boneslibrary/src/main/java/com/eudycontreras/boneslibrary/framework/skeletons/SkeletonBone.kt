package com.eudycontreras.boneslibrary.framework.skeletons

import android.graphics.*
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eudycontreras.boneslibrary.AndroidColor
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.MIN_OFFSET
import com.eudycontreras.boneslibrary.common.*
import com.eudycontreras.boneslibrary.extensions.*
import com.eudycontreras.boneslibrary.framework.BoneBoundsHandler
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties.Companion.HEIGHT_THRESHOLD
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties.Companion.OVERFLOW_THRESHOLD
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRay
import com.eudycontreras.boneslibrary.properties.Color
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.ShapeType
import com.eudycontreras.boneslibrary.utilities.Shadow
import kotlin.math.abs

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 */

internal class SkeletonBone(
    val owner: View,
    val boneProperties: BoneProperties,
    private val skeletonProperties: SkeletonProperties
) : DrawableShape(), FadeTarget, UpdateTarget, Disposable, ContentLoader {

    private val shimmerRays: MutableList<ShimmerRay> = mutableListOf()

    private var shapeType: ShapeType = ShapeType.RECTANGULAR

    private var shadowFilter: BlurMaskFilter? = null

    private var offsetViewBounds = Rect()

    private fun getLength(owner: View): Float {
        val ownerWidth = owner.width.toFloat() - (owner.horizontalMargin)
        return boneProperties.width ?: ownerWidth
    }

    private fun getThickness(owner: View): Float {
        val paddedOffset = owner.verticalPadding

        val distance = boneProperties.sectionDistance

        val originalHeight = owner.measuredHeight.toFloat()

        if (boneProperties.matchOwnersBounds) {
            return originalHeight - paddedOffset
        }

        val computedHeight = owner.measuredHeight.toFloat().clamp(
            min = boneProperties.minThickness,
            max = boneProperties.maxThickness
        )

        val maxThickness = boneProperties.maxThickness

        if (originalHeight > maxThickness) {
            if (originalHeight > ((maxThickness + distance) * HEIGHT_THRESHOLD)) {
                return if (owner is TextView && boneProperties.dissectBones != true) {
                    (computedHeight - paddedOffset).clamp(
                        min = boneProperties.minThickness
                    )
                } else {
                    originalHeight
                }
            }
        }
        return computedHeight
    }

    fun compute(parentView: ViewGroup, view: View) {
        view.getDrawingRect(offsetViewBounds)

        parentView.offsetDescendantRectToMyCoords(view, offsetViewBounds)

        val relativeLeft: Float = offsetViewBounds.centerX().toFloat()
        val relativeTop: Float = offsetViewBounds.centerY().toFloat()
        val allowShadows = drawShadows && skeletonProperties.allowShadows && boneProperties.toggleView

        this.shapeType = boneProperties.shapeType ?: shapeType

        this.opacity = Color.MAX_COLOR

        if (allowShadows) {
            this.shadowAlpha = skeletonProperties.shadowColor.alpha
            this.shadowColor = Shadow.getShadowColor(skeletonProperties.shadowColor, elevation)
            this.shadowFilter = Shadow.getShadowFilter(elevation)
        }

        this.color = boneProperties.color?.clone()
        this.backgroundColor = boneProperties.backgroundColor?.clone()

        this.corners = boneProperties.cornerRadii?.clone() ?: CornerRadii()

        this.bounds.width = boneProperties.width ?: getLength(view)
        this.bounds.height = boneProperties.height ?: getThickness(view)

        if (boneProperties.shadeMultiplier != MAX_OFFSET) {
            this.color = this.color?.adjust(boneProperties.shadeMultiplier)
        }

        this.bounds.x = (relativeLeft - (bounds.width / 2)) + boneProperties.translationX
        this.bounds.y = (relativeTop - (bounds.height / 2)) + boneProperties.translationY

        if (boneProperties.matchOwnersBounds) {
            this.bounds.y = this.bounds.y + view.paddingTop
        }
        boneProperties.shimmerRayProperties?.let {
            shimmerRays.clear()
            ShimmerRay.createRays(
                bounds = bounds,
                shimmerRays = shimmerRays,
                properties = it
            )
        }
    }

    override fun restore() {
        if (boneProperties.disposed) return

        BoneBoundsHandler.restoreBounds(
            view = owner,
            boneProps = boneProperties,
            animateRestoration = skeletonProperties.animateRestoredBounds,
            animDuration = skeletonProperties.stateTransitionDuration
        )
    }

    override fun concealContent() {
        if (boneProperties.disposed) return

        if (boneProperties.toggleView) {
            owner.alpha = MIN_OFFSET
            owner.visibility = View.INVISIBLE
        }
    }

    override fun revealContent() {
        if (boneProperties.disposed) return

        if (boneProperties.toggleView) {
            owner.alpha = MAX_OFFSET
            owner.visibility = View.VISIBLE
        }
    }

    override fun prepareContentFade() {
        if (boneProperties.disposed) return

        if (boneProperties.toggleView) {
            owner.alpha = MIN_OFFSET
            owner.visibility = View.VISIBLE
        }
    }

    override fun fadeContent(fraction: Float) {
        if (boneProperties.disposed) return

        if (boneProperties.toggleView) {
            owner.alpha = MAX_OFFSET * fraction
        }
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

        if (drawShadows && skeletonProperties.allowShadows && boneProperties.toggleView) {
            val difference = Color.MAX_COLOR - abs(Color.MAX_COLOR - shadowAlpha)
            val shadowOpacity = shadowAlpha - (difference * fraction).toInt()
            shadowColor?.updateAlpha(shadowOpacity)
        }

        opacity = Color.MAX_COLOR - (Color.MAX_COLOR * fraction).toInt()

        if (shimmerRays.size > 0) {
            for (ray in shimmerRays) {
                ray.onFade(fraction)
            }
        }
    }

    override fun dispose() {
        shimmerRays.clear()
        boneProperties.disposed = false
    }

    fun onRender(
        canvas: Canvas,
        paint: Paint,
        bonePath: Path,
        shadowPath: Path,
        rayPath: Path
    ) {
        if (boneProperties.disposed) return

        val allowShadows = drawShadows && skeletonProperties.allowShadows && boneProperties.toggleView

        buildPaths(allowShadows, shadowPath, bonePath, rayPath)

        drawPaths(allowShadows, paint, canvas, shadowPath, bonePath)
    }

    private fun buildPaths(
        allowShadows: Boolean,
        shadowPath: Path,
        bonePath: Path,
        rayPath: Path
    ) {
        when (shapeType) {
            ShapeType.RECTANGULAR -> {
                val isTextView = owner is TextView
                val maxThickness = boneProperties.maxThickness
                val highRatio = height >= maxThickness * OVERFLOW_THRESHOLD
                val dissectBones =
                    boneProperties.height == null && (boneProperties.dissectBones ?: false)

                if (dissectBones && isTextView && highRatio) {
                    val part = boneProperties.sectionDistance + maxThickness
                    val times = ((bounds.height - maxThickness) / part).toInt()
                    var padding = maxThickness / 2

                    for (it in 0..times) {
                        val top = bounds.top + (it * maxThickness) + padding
                        val bottom = top + maxThickness

                        if (allowShadows) {
                            shadowPath.addShadowBounds(bounds, radii, elevation, top = top, bottom = bottom)
                        }
                        bonePath.addRoundRect(bounds, radii, top = top, bottom = bottom)
                        rayPath.addRoundRect(bounds, radii, top = top, bottom = bottom)
                        padding += boneProperties.sectionDistance
                    }
                } else {
                    if (allowShadows) {
                        shadowPath.addShadowBounds(bounds, radii, elevation)
                    }
                    bonePath.addRoundRect(bounds, radii)
                    rayPath.addRoundRect(bounds, radii)
                }
            }
            ShapeType.CIRCULAR -> {
                val centerX = offsetViewBounds.centerX().toFloat()
                val centerY = offsetViewBounds.centerY().toFloat()

                if (allowShadows) {
                    shadowPath.addShadowOval(centerX, centerY, radius, elevation)
                }
                bonePath.addCircle(centerX, centerY, radius)
                rayPath.addCircle(centerX, centerY, radius)
            }
        }
    }

    private fun drawPaths(
        allowShadows: Boolean,
        paint: Paint,
        canvas: Canvas,
        shadowPath: Path,
        bonePath: Path
    ) {
        if (allowShadows) {
            paint.shader = null
            paint.maskFilter = shadowFilter
            paint.color = shadowColor?.toColor() ?: Shadow.DEFAULT_COLOR
            canvas.drawPath(shadowPath, paint)
        }

        if (shader != null) {
            paint.maskFilter = null
            paint.shader = shader
        } else if (color != null || backgroundColor != null) {
            val color = backgroundColor ?: this.color
            paint.shader = null
            paint.maskFilter = null
            paint.style = Paint.Style.FILL
            paint.color = color?.toColor() ?: AndroidColor.TRANSPARENT
        }

        if (boneProperties.background != null && boneProperties.toggleView) {
            if (backgroundColor != null) {
                canvas.drawPath(bonePath, paint)
            } else {
                boneProperties.background?.draw(canvas)
            }
        } else {
            canvas.drawPath(bonePath, paint)
        }

        if (shimmerRays.size > 0) {
            for (ray in shimmerRays) {
                ray.onRender(canvas, paint, bonePath)
            }
        }
    }

    companion object {
        @JvmStatic
        fun build(
            view: View,
            properties: BoneProperties,
            skeletonProperties: SkeletonProperties,
            manager: SkeletonManager
        ): SkeletonBone {
            return SkeletonBone(
                owner = view,
                boneProperties = properties,
                skeletonProperties = skeletonProperties
            ).apply {
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
