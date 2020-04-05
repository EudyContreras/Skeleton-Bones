package com.eudycontreras.boneslibrary.framework

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.widget.RemoteViews.RemoteView
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonProperties
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since March 2020
 */

@RemoteView class BonePropertyHolder @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1
): View(context, attrs, defStyleAttr) {

    init {
        super.setVisibility(GONE)
    }

    internal var propId: Int = NO_ID

    internal val boneProperties: BoneProperties = BoneProperties()

    fun setBonePropId(propId: Int?) {
        requireNotNull(propId)
        this.propId = propId
    }

    fun setBonePropColor(boneColor: Int?) {
        this.boneProperties.color = boneColor?.let { MutableColor(it) }
    }

    fun setBonePropCornerRadius(radius: Float?) {
        this.boneProperties.cornerRadii = radius?.let { CornerRadii(it) }
    }

    fun setBoneMatchBounds(matchBounds: Boolean?) {
        this.boneProperties.matchOwnersBounds = matchBounds ?: false
    }

    fun setBoneToggleViews(toggleView: Boolean?) {
        this.boneProperties.toggleView = toggleView ?: true
    }

    fun setBonePropDissectLargeBones(dissect: Boolean?) {
        this.boneProperties.dissectBones = dissect
    }

    fun setBonePropAllowSavedState(allow: Boolean?) {
        this.boneProperties.allowSavedState = allow ?: false
    }

    fun setBonePropAllowWeakSavedState(allow: Boolean?) {
        this.boneProperties.allowWeakSavedState = allow ?: false
    }

    fun setBonePropMinThickness(thickness: Float?) {
        this.boneProperties.minThickness = thickness ?: return
    }

    fun setBonePropMaxThickness(thickness: Float?) {
        this.boneProperties.maxThickness = thickness ?: return
    }

    fun setBonePropSectionDistance(distance: Float?) {
        this.boneProperties.sectionDistance = distance ?: return
    }

    fun setBonePropShimmerRayColor(color: Int?) {
        this.boneProperties.getRayShimmerProps()?.apply {
            this.shimmerRayColor = MutableColor(color ?: 0)
        }
    }

    fun setBonePropShimmerRayCount(count: Int?) {
        this.boneProperties.getRayShimmerProps()?.apply {
            this.shimmerRayCount = count ?: 0
        }
    }

    fun setBonePropShimmerRaySharedInterpolator(sharedInterpolator: Boolean?) {
        this.boneProperties.getRayShimmerProps()?.apply {
            this.shimmerRaySharedInterpolator = sharedInterpolator ?: true
        }
    }

    fun setBonePropShimmerRayInterpolator(interpolator: Interpolator?) {
        this.boneProperties.getRayShimmerProps()?.apply {
            this.shimmerRayInterpolator = interpolator
        }
    }

    fun setBonePropShimmerRaySpeedMultiplier(speedMultiplier: Float?) {
        this.boneProperties.getRayShimmerProps()?.apply {
            this.shimmerRaySpeedMultiplier = speedMultiplier ?: MAX_OFFSET
        }
    }

    fun setBonePropShimmerRayThickness(thickness: Float?) {
        this.boneProperties.getRayShimmerProps()?.apply {
            this.shimmerRayThickness = thickness
        }
    }

    fun setBonePropShimmerRayThicknessRatio(thicknessRatio: Float?) {
        this.boneProperties.getRayShimmerProps()?.apply {
            this.shimmerRayThicknessRatio = thicknessRatio ?: ShimmerRayProperties.DEFAULT_THICKNESS_RATIO
        }
    }

    fun setBonePropShimmerRayTilt(tilt: Float?) {
        this.boneProperties.getRayShimmerProps()?.apply {
            this.shimmerRayTilt = tilt ?: ShimmerRayProperties.DEFAULT_RAY_TILT
        }
    }

    fun setBonePropTransitionDuration(duration: Long?) {
        this.boneProperties.transitionDuration = duration ?: SkeletonProperties.DEFAULT_TRANSITION_DURATION
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas?) {}

    override fun onDraw(canvas: Canvas?) {}

    override fun setVisibility(visibility: Int) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(0, 0)
    }
}