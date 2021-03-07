package com.eudycontreras.boneslibrary.framework.shimmer

import android.view.animation.Interpolator
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.extensions.dp
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties.Companion.DEFAULT_DURATION
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties.Companion.DEFAULT_RAY_TILT
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties.Companion.DEFAULT_THICKNESS_RATIO
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties.Companion.MAX_THICKNESS
import com.eudycontreras.boneslibrary.properties.MutableColor

/**
 * Copyright (C) 2020 Project Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 *
 * Builder class used for building the properties used for rendering **Shimmer Rays**
 *
 * For a more thorough description about **Shimmer Rays** please visit the link:
 * [Skeleton-Bones](https://github.com/EudyContreras/Skeleton-Bones) library on GitHub.
 *
 * @see ShimmerRayProperties
 */

data class ShimmerRayBuilder(
    private val shimmerRayProperties: ShimmerRayProperties = ShimmerRayProperties()
) {

    val properties: ShimmerRayProperties
        get() = shimmerRayProperties

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the tilt or skew used for the **Shimmer Rays**. negative values will skew
     * the ray to right while positive values will skew it to the left.
     * ```
     * ```
     * **Default:** -0.3f.
     * ```
     * ```
     * @param tilt The amount of tilt/skew to apply to the shimmer ray
     */
    fun setTilt(tilt: Float = DEFAULT_RAY_TILT): ShimmerRayBuilder {
        this.shimmerRayProperties.shimmerRayTilt = tilt
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the base color of the **Shimmer Rays**.
     * The color is then used as a base for generating a gradient
     * ```
     * ```
     * **Default:** MutableColor.WHITE
     * ```
     * ```
     * @param color The base color used for creating the ray shimmer gradient
     * @see MutableColor
     */
    fun setColor(color: MutableColor = MutableColor.WHITE as MutableColor): ShimmerRayBuilder {
        this.shimmerRayProperties.shimmerRayColor = color
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the amount of **Shimmer Rays** that should be rendered and animated
     * ```
     * ```
     * **Default:** 0
     * ```
     * ```
     * @param count The amount of shimmer rays that should be rendered
     */
    fun setCount(count: Int = 0): ShimmerRayBuilder {
        this.shimmerRayProperties.shimmerRayCount = count
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Determines whether the Interpolator set for animating the **Shimmer Rays** should be shared
     * or divided among all the **Shimmer Rays** This will affect how the **Shimmer Rays** are animated
     * ```
     * ```
     * **Default:** true
     * ```
     * ```
     * @param sharedInterpolator Flags whether the available interpolator should be shared
     * among the shimmer rays.
     */
    fun setSharedInterpolator(sharedInterpolator: Boolean = true): ShimmerRayBuilder {
        this.shimmerRayProperties.shimmerRaySharedInterpolator = sharedInterpolator
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the interpolator to use when animating the **Shimmer Rays**. The interpolator will either
     * be shared among all the rays or divided among them based on the value of: skeletonShimmerRaySharedInterpolator.
     * ```
     * ```
     * **Default:** FastOutSlowInInterpolator()
     * ```
     * ```
     * The available interpolator available through the android framework as of
     * now are:
     *
     * - accelerate_cubic,
     * - accelerate_decelerate,
     * - accelerate_quad,
     * - accelerate_quint,
     * - anticipate,
     * - anticipate_overshoot,
     * - bounce,
     * - cycle,
     * - decelerate_cubic,
     * - decelerate_quad,
     * - decelerate_quint,
     * - fast_out_extra_slow_in,
     * - fast_out_linear_in,
     * - fast_out_slow_in,
     * - linear,
     * - linear_out_slow_in,
     * - overshoot
     *
     * @see Interpolator
     * @param interpolator The interpolator used for interpolating the shimmer
     * ray animation.
     */
    fun setInterpolator(interpolator: Interpolator): ShimmerRayBuilder {
        this.shimmerRayProperties.shimmerRayInterpolator = interpolator
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the shimmer ray thickness. If both skeletonShimmerRayThicknessRatio
     * and the skeletonShimmerRayThickness are defined, the skeletonShimmerRayThickness will take priority
     * ```
     * ```
     * **Default:** 120dp
     * ```
     * ```
     * @param thickness The thickness/width of the shimmer ray
     */
    fun setThickness(thickness: Float = MAX_THICKNESS.dp): ShimmerRayBuilder {
        this.shimmerRayProperties.shimmerRayThickness = thickness
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the **Shimmer Rays** thickness ratio. The ratio sets the width of the **Shimmer Rays** to be created
     * at the specified percentage of its skeleton width. A ratio of 0.2 means that the
     * shimmer ray's thickness will be 20 percent of the skeletons's width. If both skeletonShimmerRayThicknessRatio
     * and skeletonShimmerRayThickness are defined, the skeletonShimmerRayThickness will take priority.
     * ```
     * ```
     * **Default:** 0.3f
     * ```
     * ```
     * @param thicknessRatio The width/thickness ratio used for setting the ray shimmer width/thickness
     */
    fun setThicknessRatio(thicknessRatio: Float = DEFAULT_THICKNESS_RATIO): ShimmerRayBuilder {
        this.shimmerRayProperties.shimmerRayThicknessRatio = thicknessRatio
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the duration of the **Shimmer Rays** animation. This is the duration of the whole animation. The
     * individual **Shimmer Rays**  may appear to be animated much quicker.This property applies to the parent
     * skeleton only. Shimmer ray durations applied to bones or inner skeletons will be ignored.
     * Use the **setSpeedMultiplier** builder function in order to adjust the speed of children bones and skeletons
     *
     * ```
     * ```
     * **Default:** 2000L
     * ```
     * ```
     * @see setSpeedMultiplier
     * @param duration The total duration of the shimmer ray animation
     */
    fun setAnimationDuration(duration: Long = DEFAULT_DURATION): ShimmerRayBuilder {
        this.shimmerRayProperties.shimmerRayAnimDuration = duration
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the shimmer **Shimmer Ray** speed multiplier. Values greater than 1.0f will result in faster
     * animation speeds, while values lower than 1.0 will result in slower animation speeds. This is an
     * additional layer on top of the animation duration
     * ```
     * ```
     * **Default:** 1.0f
     * ```
     * ```
     * @param speedMultiplier The multiplier used for controlling and altering
     * the speed of the shimmer ray animation
     */
    fun setSpeedMultiplier(speedMultiplier: Float = MAX_OFFSET): ShimmerRayBuilder {
        this.shimmerRayProperties.shimmerRaySpeedMultiplier = speedMultiplier
        return this
    }
}
