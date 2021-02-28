package com.eudycontreras.boneslibrary.framework.shimmer

import android.view.animation.*
import androidx.annotation.Dimension
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.common.Cloneable
import com.eudycontreras.boneslibrary.properties.MutableColor

/**
 * Copyright (C) 2020 Project Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 * For a more thorough description about **Shimmer Rays** please visit the link:
 * [Skeleton-Bones](https://github.com/EudyContreras/Skeleton-Bones) library on GitHub.
 *
 */

class ShimmerRayProperties: Cloneable<ShimmerRayProperties> {

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
     */
    @Volatile
    var shimmerRayTilt: Float = DEFAULT_RAY_TILT

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
     * @see MutableColor
     */
    @Volatile
    var shimmerRayColor: MutableColor = MutableColor.WHITE as MutableColor

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
     */
    @Volatile
    var shimmerRayCount: Int = DEFAULT_RAY_COUNT

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
     */
    @Volatile
    var shimmerRaySharedInterpolator: Boolean = true

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
     */
    @Volatile
    var shimmerRayInterpolator: Interpolator? = FastOutSlowInInterpolator()

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
     */
    @Volatile
    var shimmerRayThickness: Float? = null

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
     */
    @Volatile
    var shimmerRayThicknessRatio: Float = DEFAULT_THICKNESS_RATIO

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the duration of the **Shimmer Rays** animation. This is the duration of the whole animation. The
     * individual **Shimmer Rays**  may appear to be animated much quicker. This property applies to the parent
     * skeleton only. Shimmer ray durations applied to bones or inner skeletons will be ignored.
     * Use the **shimmerRaySpeedMultiplier** property in order to adjust the speed of children bones and skeletons
     *
     * ```
     * ```
     * **Default:** 2000L
     * ```
     * ```
     * @see shimmerRaySpeedMultiplier
     */
    @Volatile
    var shimmerRayAnimDuration: Long = DEFAULT_DURATION

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
     */
    @Volatile
    var shimmerRaySpeedMultiplier: Float = MAX_OFFSET
        set(value) {
            val newValue = MAX_OFFSET + (MAX_OFFSET - value)
            field = newValue
        }

    override fun clone(): ShimmerRayProperties {
        return ShimmerRayProperties().also {
            it.shimmerRayCount = this.shimmerRayCount
            it.shimmerRayTilt = this.shimmerRayTilt
            it.shimmerRayAnimDuration = this.shimmerRayAnimDuration
            it.shimmerRaySpeedMultiplier = this.shimmerRaySpeedMultiplier
            it.shimmerRayThickness = this.shimmerRayThickness
            it.shimmerRayThicknessRatio = this.shimmerRayThicknessRatio
            it.shimmerRaySharedInterpolator = this.shimmerRaySharedInterpolator
            it.shimmerRayColor = this.shimmerRayColor.copy()
            it.shimmerRayInterpolator = when (this.shimmerRayInterpolator) {
                is LinearInterpolator -> LinearInterpolator()
                is FastOutSlowInInterpolator -> FastOutSlowInInterpolator()
                is FastOutLinearInInterpolator -> FastOutLinearInInterpolator()
                is AccelerateInterpolator -> AccelerateInterpolator()
                is DecelerateInterpolator -> DecelerateInterpolator()
                is BounceInterpolator -> BounceInterpolator()
                is AnticipateInterpolator -> AnticipateInterpolator()
                is AnticipateOvershootInterpolator -> AnticipateOvershootInterpolator()
                else -> this.shimmerRayInterpolator
            }
        }
    }

    internal fun builder(): ShimmerRayBuilder {
        return ShimmerRayBuilder(this)
    }


    companion object {
        @Dimension(unit = Dimension.DP) const val MAX_THICKNESS = 120

        const val DEFAULT_RAY_TILT = -0.3f
        const val DEFAULT_RAY_COUNT = 0
        const val DEFAULT_DURATION = 2000L
        const val DEFAULT_THICKNESS_RATIO = 0.45f
    }
}