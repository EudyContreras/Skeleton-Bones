package com.eudycontreras.boneslibrary.bindings

import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.doWith
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.properties.Color
import com.eudycontreras.boneslibrary.properties.MutableColor

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since March 2020
 */

object SkeletonShimmerRayBindings {
    /**
     * **Binding Property**: app:skeletonShimmerRayColor
     */
    const val SKELETON_SHIMMER_RAY_COLOR = "skeletonShimmerRayColor"
    /**
     * **Binding Property**: app:skeletonShimmerRayTilt
     */
    const val SKELETON_SHIMMER_RAY_TILT = "skeletonShimmerRayTilt"
    /**
     * **Binding Property**: app:skeletonShimmerRayCount
     */
    const val SKELETON_SHIMMER_RAY_COUNT = "skeletonShimmerRayCount"
    /**
     * **Binding Property**: app:skeletonShimmerRayThickness
     */
    const val SKELETON_SHIMMER_RAY_THICKNESS = "skeletonShimmerRayThickness"
    /**
     * **Binding Property**: app:skeletonShimmerRayThicknessRatio
     */
    const val SKELETON_SHIMMER_RAY_THICKNESS_RATIO = "skeletonShimmerRayThicknessRatio"
    /**
     * **Binding Property**: app:skeletonShimmerRayInterpolator
     */
    const val SKELETON_SHIMMER_RAY_INTERPOLATOR = "skeletonShimmerRayInterpolator"
    /**
     * **Binding Property**: app:skeletonShimmerRaySharedInterpolator
     */
    const val SKELETON_SHIMMER_RAY_INTERPOLATOR_SHARED = "skeletonShimmerRaySharedInterpolator"
    /**
     * **Binding Property**: app:skeletonShimmerRaySpeedMultiplier
     */
    const val SKELETON_SHIMMER_RAY_SPEED_MULTIPLIER = "skeletonShimmerRaySpeedMultiplier"
}

///////////////////////// Skeleton Shimmer Ray Binding Adapters /////////////////////////

@BindingAdapter(SkeletonShimmerRayBindings.SKELETON_SHIMMER_RAY_COLOR)
internal fun ViewGroup.setSkeletonShimmerRayColor(@ColorInt rayColor: Int?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().shimmerRayProperties.shimmerRayColor = MutableColor(rayColor ?: Color.MIN_COLOR)
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonShimmerRayColor(rayColor)
        }
    }
}

@BindingAdapter(SkeletonShimmerRayBindings.SKELETON_SHIMMER_RAY_TILT)
internal fun ViewGroup.setSkeletonShimmerRayTilt(rayTilt: Float?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().shimmerRayProperties.shimmerRayTilt = rayTilt ?: ShimmerRayProperties.DEFAULT_RAY_TILT
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonShimmerRayTilt(rayTilt)
        }
    }
}

@BindingAdapter(SkeletonShimmerRayBindings.SKELETON_SHIMMER_RAY_COUNT)
internal fun ViewGroup.setSkeletonShimmerRayCount(count: Int?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().shimmerRayProperties.shimmerRayCount = count ?: return
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonShimmerRayCount(count)
        }
    }
}

@BindingAdapter(SkeletonShimmerRayBindings.SKELETON_SHIMMER_RAY_THICKNESS)
internal fun ViewGroup.setSkeletonShimmerRayThickness(thickness: Float?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().shimmerRayProperties.shimmerRayThickness = thickness
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonShimmerRayThickness(thickness)
        }
    }
}

@BindingAdapter(SkeletonShimmerRayBindings.SKELETON_SHIMMER_RAY_THICKNESS_RATIO)
internal fun ViewGroup.setSkeletonShimmerRayThicknessRatio(thicknessRatio: Float?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().shimmerRayProperties.shimmerRayThicknessRatio = thicknessRatio ?: ShimmerRayProperties.DEFAULT_THICKNESS_RATIO
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonShimmerRayThicknessRatio(thicknessRatio)
        }
    }
}

@BindingAdapter(SkeletonShimmerRayBindings.SKELETON_SHIMMER_RAY_INTERPOLATOR)
internal fun ViewGroup.setSkeletonShimmerRayInterpolator(interpolator: Interpolator?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().shimmerRayProperties.shimmerRayInterpolator = interpolator
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonShimmerRayInterpolator(interpolator)
        }
    }
}


@BindingAdapter(SkeletonShimmerRayBindings.SKELETON_SHIMMER_RAY_INTERPOLATOR_SHARED)
internal fun ViewGroup.setSkeletonShimmerRaySharedInterpolator(shared: Boolean?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().shimmerRayProperties.shimmerRaySharedInterpolator = shared ?: true
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonShimmerRaySharedInterpolator(shared)
        }
    }
}

@BindingAdapter(SkeletonShimmerRayBindings.SKELETON_SHIMMER_RAY_SPEED_MULTIPLIER)
internal fun ViewGroup.setSkeletonShimmerRaySpeedMultiplier(speedMultiplier: Float?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().shimmerRayProperties.shimmerRaySpeedMultiplier = speedMultiplier ?: MAX_OFFSET
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonShimmerRaySpeedMultiplier(speedMultiplier)
        }
    }
}