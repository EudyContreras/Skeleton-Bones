package com.eudycontreras.boneslibrary.bindings

import android.view.View
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.doWith
import com.eudycontreras.boneslibrary.extensions.generateId
import com.eudycontreras.boneslibrary.framework.bones.BoneDrawable
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties
import com.eudycontreras.boneslibrary.properties.MutableColor

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since March 2020
 */

object SkeletonBoneShimmerRayBindings {
    /**
     * **Binding Property**: app:skeletonBoneShimmerRayColor
     */
    const val SKELETON_BONE_SHIMMER_RAY_COLOR = "skeletonBoneShimmerRayColor"
    /**
     * **Binding Property**: app:skeletonBoneShimmerRayTilt
     */
    const val SKELETON_BONE_SHIMMER_RAY_TILT = "skeletonBoneShimmerRayTilt"
    /**
     * **Binding Property**: app:skeletonBoneShimmerRayCount
     */
    const val SKELETON_BONE_SHIMMER_RAY_COUNT = "skeletonBoneShimmerRayCount"
    /**
     * **Binding Property**: app:skeletonBoneShimmerRayThickness
     */
    const val SKELETON_BONE_SHIMMER_RAY_THICKNESS = "skeletonBoneShimmerRayThickness"
    /**
     * **Binding Property**: app:skeletonBoneShimmerRayThicknessRatio
     */
    const val SKELETON_BONE_SHIMMER_RAY_THICKNESS_RATIO = "skeletonBoneShimmerRayThicknessRatio"
    /**
     * **Binding Property**: app:skeletonBoneShimmerRayInterpolator
     */
    const val SKELETON_BONE_SHIMMER_RAY_INTERPOLATOR = "skeletonBoneShimmerRayInterpolator"
    /**
     * **Binding Property**: app:skeletonBoneShimmerRaySharedInterpolator
     */
    const val SKELETON_BONE_SHIMMER_RAY_INTERPOLATOR_SHARED = "skeletonBoneShimmerRaySharedInterpolator"
    /**
     * **Binding Property**: app:skeletonBoneShimmerRaySpeedMultiplier
     */
    const val SKELETON_BONE_SHIMMER_RAY_SPEED_MULTIPLIER = "skeletonBoneShimmerRaySpeedMultiplier"
}

///////////////////////// Skeleton Bone Shimmer Ray Binding Adapters ///////////////////////

@BindingAdapter(SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_COLOR)
internal fun View.setSkeletonBoneShimmerRayColor(@ColorInt rayColor: Int?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.getRayShimmerProps()?.apply {
                this.shimmerRayColor = MutableColor.fromColor(rayColor)
            }
        }
    } else {
        setBoneShimmerRayColor(rayColor)
    }
}


private fun View.setBoneShimmerRayColor(@ColorInt rayColor: Int?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().apply {
                this.getRayShimmerProps()?.apply {
                    this.shimmerRayColor = MutableColor.fromColor(rayColor)
                }
            }
        } else {
            addBoneLoader(enabled = true)
            setBoneShimmerRayColor(rayColor)
        }
    }
}


@BindingAdapter(SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_TILT)
internal fun View.setSkeletonBoneShimmerRayTilt(rayTilt: Float?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.getRayShimmerProps()?.apply {
                this.shimmerRayTilt = rayTilt ?: ShimmerRayProperties.DEFAULT_RAY_TILT
            }
        }
    } else {
        setBoneShimmerRayTilt(rayTilt)
    }
}

private fun View.setBoneShimmerRayTilt(rayTilt: Float?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().apply {
                this.getRayShimmerProps()?.apply {
                    this.shimmerRayTilt = rayTilt ?: ShimmerRayProperties.DEFAULT_RAY_TILT
                }
            }
        } else {
            addBoneLoader(enabled = true)
            setBoneShimmerRayTilt(rayTilt)
        }
    }
}

@BindingAdapter(SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_COUNT)
internal fun View.setSkeletonBoneShimmerRayCount(count: Int?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.getRayShimmerProps()?.apply {
                this.shimmerRayCount = count ?: return
            }
        }
    } else {
        setBoneShimmerRayCount(count)
    }
}


private fun View.setBoneShimmerRayCount(count: Int?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().apply {
                this.getRayShimmerProps()?.apply {
                    this.shimmerRayCount = count ?: return
                }
            }
        } else {
            addBoneLoader(enabled = true)
            setBoneShimmerRayCount(count)
        }
    }
}

@BindingAdapter(SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_THICKNESS)
internal fun View.setSkeletonBoneShimmerRayThickness(thickness: Float?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.getRayShimmerProps()?.apply {
                this.shimmerRayThickness = thickness
            }
        }
    } else {
        setBoneShimmerRayThickness(thickness)
    }
}

private fun View.setBoneShimmerRayThickness(thickness: Float?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().apply {
                this.getRayShimmerProps()?.apply {
                    this.shimmerRayThickness = thickness
                }
            }
        } else {
            addBoneLoader(enabled = true)
            setBoneShimmerRayThickness(thickness)
        }
    }
}

@BindingAdapter(SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_THICKNESS_RATIO)
internal fun View.setSkeletonBoneShimmerRayThicknessRatio(thicknessRatio: Float?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.getRayShimmerProps()?.apply {
                this.shimmerRayThicknessRatio = thicknessRatio ?: ShimmerRayProperties.DEFAULT_THICKNESS_RATIO
            }
        }
    } else {
        setBoneShimmerRayThicknessRatio(thicknessRatio)
    }
}

private fun View.setBoneShimmerRayThicknessRatio(thicknessRatio: Float?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().apply {
                this.getRayShimmerProps()?.apply {
                    this.shimmerRayThicknessRatio = thicknessRatio ?: ShimmerRayProperties.DEFAULT_THICKNESS_RATIO
                }
            }
        } else {
            addBoneLoader(enabled = true)
            setBoneShimmerRayThicknessRatio(thicknessRatio)
        }
    }
}

@BindingAdapter(SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_INTERPOLATOR)
internal fun View.setSkeletonBoneShimmerRayInterpolator(interpolator: Interpolator?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.getRayShimmerProps()?.apply {
                this.shimmerRayInterpolator = interpolator
            }
        }
    } else {
        setBoneShimmerRayInterpolator(interpolator)
    }
}

private fun View.setBoneShimmerRayInterpolator(interpolator: Interpolator?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().apply {
                this.getRayShimmerProps()?.apply {
                    this.shimmerRayInterpolator = interpolator
                }
            }
        } else {
            addBoneLoader(enabled = true)
            setBoneShimmerRayInterpolator(interpolator)
        }
    }
}

@BindingAdapter(SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_INTERPOLATOR_SHARED)
internal fun View.setSkeletonBoneShimmerRaySharedInterpolator(shared: Boolean?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.getRayShimmerProps()?.apply {
                this.shimmerRaySharedInterpolator = shared ?: true
            }
        }
    } else {
        setBoneShimmerRaySharedInterpolator(shared)
    }
}

private fun View.setBoneShimmerRaySharedInterpolator(shared: Boolean?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().apply {
                this.getRayShimmerProps()?.apply {
                    this.shimmerRaySharedInterpolator = shared ?: true
                }
            }
        } else {
            addBoneLoader(enabled = true)
            setBoneShimmerRaySharedInterpolator(shared)
        }
    }
}

@BindingAdapter(SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_SPEED_MULTIPLIER)
internal fun View.setSkeletonBoneShimmerRaySpeedMultiplier(speedMultiplier: Float?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.getRayShimmerProps()?.apply {
                this.shimmerRaySpeedMultiplier = speedMultiplier ?: MAX_OFFSET
            }
        }
    } else {
        setBoneShimmerRaySpeedMultiplier(speedMultiplier)
    }
}

private fun View.setBoneShimmerRaySpeedMultiplier(speedMultiplier: Float?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().apply {
                this.getRayShimmerProps()?.apply {
                    this.shimmerRaySpeedMultiplier = speedMultiplier ?: MAX_OFFSET
                }
            }
        } else {
            addBoneLoader(enabled = true)
            setBoneShimmerRaySpeedMultiplier(speedMultiplier)
        }
    }
}