package com.eudycontreras.boneslibrary.framework.skeletons

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import com.eudycontreras.boneslibrary.Action
import com.eudycontreras.boneslibrary.AndroidColor
import com.eudycontreras.boneslibrary.common.*
import com.eudycontreras.boneslibrary.extensions.*
import com.eudycontreras.boneslibrary.framework.BoneBoundsHandler
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRay
import com.eudycontreras.boneslibrary.properties.Color

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 */

internal class Skeleton(
    private val manager: SkeletonManager,
    private val properties: SkeletonProperties
) : DrawableShape(), RenderTarget, UpdateTarget, FadeTarget, Disposable, ContentLoader {

    private var owner: ViewGroup? = null

    private val rayPath: Path = Path()

    private val bonePath: Path = Path()

    private val shadowPath: Path = Path()

    private val bones: MutableMap<Int, SkeletonBone> = mutableMapOf()

    private val shimmerRays: MutableList<ShimmerRay> = mutableListOf()

    override fun concealContent() {
        for ((_, value) in bones) {
            value.concealContent()
        }
    }

    override fun revealContent() {
        for ((_, value) in bones) {
            value.revealContent()
        }
    }

    override fun prepareContentFade() {
        for ((_, value) in bones) {
            value.prepareContentFade()
        }
    }

    override fun restore() {
        for ((_, value) in bones) {
            value.restore()
        }
    }

    override fun fadeContent(fraction: Float) {}

    override fun onFade(fraction: Float) {
        opacity = Color.MAX_COLOR - (Color.MAX_COLOR * fraction).toInt()

        for ((_, value) in bones) {
            value.onFade(fraction)
            value.fadeContent(fraction)
        }
        for (value in shimmerRays) {
            value.onFade(fraction)
        }
    }

    override fun onUpdate(fraction: Float) {
        for ((_, value) in bones) {
            value.onUpdate(fraction)
        }
        for (ray in shimmerRays) {
            ray.onUpdate(fraction)
        }
    }

    override fun onRender(
        canvas: Canvas,
        paint: Paint,
        path: Path
    ) {

        if (color != null) {
            paint.shader = null
            paint.color = this.color?.toColor() ?: AndroidColor.TRANSPARENT
            paint.style = Paint.Style.FILL

            canvas.drawPath(path, paint)
        }

        rayPath.reset()

        for ((_, value) in bones) {
            bonePath.reset()
            shadowPath.reset()
            value.onRender(canvas, paint, bonePath, shadowPath, rayPath)
            shadowPath.close()
            bonePath.close()
        }

        if (properties.allowBoneGeneration) {
            for (value in shimmerRays) {
                value.onRender(canvas, paint, rayPath)
            }
        } else {
            for (value in shimmerRays) {
                value.onRender(canvas, paint, path)
            }
        }
    }

    override fun dispose() {
        bones.values.forEach { it.dispose() }
        properties.clearDisposedIds()
        shimmerRays.clear()
        bones.clear()
    }

    fun compute(viewGroup: ViewGroup, onCompute: Action) {
        var descendants: List<View> = emptyList()

        var foundInvalid = false

        if (properties.allowBoneGeneration) {
            descendants = viewGroup.descendantViews {
                !properties.isIgnored(ownerId = it.id) && !properties.isDisposed(ownerId = it.id)
            }
            for (child in descendants) {
                val id = child.generateId()
                val props = properties.getBoneProperties(id)
                if (!child.hasValidBounds()) {
                    foundInvalid = true
                    BoneBoundsHandler.applyTemporaryBounds(child, props, properties.animateRestoredBounds)
                }
            }
        }

        if (foundInvalid) {
            viewGroup.doOnLayout {
                applyAndBuild(viewGroup, descendants)
                onCompute()
            }
        } else {
            applyAndBuild(viewGroup, descendants)
            onCompute()
        }
    }

    private fun applyAndBuild(
        viewGroup: ViewGroup,
        descendants: List<View>
    ) {
        this.owner = viewGroup
        this.opacity = Color.MAX_COLOR
        this.elevation = viewGroup.elevation
        this.width = viewGroup.measuredWidth.toFloat()
        this.height = viewGroup.measuredHeight.toFloat()
        this.color = properties.skeletonBackgroundColor ?: color
        this.corners = properties.skeletonCornerRadii ?: corners

        shimmerRays.clear()

        ShimmerRay.createRays(
            bounds = this.bounds,
            shimmerRays = shimmerRays,
            properties = properties.shimmerRayProperties
        )

        for (child in descendants) {
            val id = child.generateId()
            val props = properties.getBoneProperties(id).apply {
                background = child.background?.clone()
                backgroundColor = child.getBackgroundColor()
            }
            if (!child.hasDrawableBounds(props)) {
                continue
            }
            val bone = bones[id] ?: SkeletonBone.build(
                view = child,
                properties = props,
                skeletonProperties = properties,
                manager = manager
            ).apply {
                this.id = id
                bones[id] = this
            }

            bone.compute(viewGroup, child)
        }
    }
}
