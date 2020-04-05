package com.eudycontreras.boneslibrary.framework.bones

import android.graphics.drawable.Drawable
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.bindings.SkeletonBoneBindings
import com.eudycontreras.boneslibrary.common.Cloneable
import com.eudycontreras.boneslibrary.extensions.clone
import com.eudycontreras.boneslibrary.extensions.dp
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties.Companion.DEFAULT_DURATION
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonBone
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.Dimension
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType

/**
 * Copyright (C) 2020 Project Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 * For a more thorough description about **Bones* please visit the link:
 * [Skeleton-Bones](https://github.com/EudyContreras/Skeleton-Bones) library on GitHub.
 */

class BoneProperties internal constructor(): Cloneable<BoneProperties>{

    @Volatile
    internal var enabledListener: ((enabled: Boolean) -> Unit)? = null

    @Volatile
    internal var enabledProvider: (() -> Boolean)? = null

    @Volatile
    internal var background: Drawable? = null

    @Volatile
    internal var disposed: Boolean = false

    @Volatile
    internal var originalBounds: Dimension? = null

    @Volatile
    internal var originalParentTransition: LayoutTransitionData? = null

    internal data class LayoutTransitionData(
        val changingDuration: Long,
        val isChangingEnabled: Boolean,
        val isDisappearingEnabled: Boolean
    )

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Reference to the properties used for rendering and building
     * shimmer rays. The shimmer rays are typically how a Skeleton animates
     * its loading state. For a more complete description please look at the
     * [Shimmer Ray](https://github.com/EudyContreras/Bones/wiki/Shimmer-Rays) wiki on GitHub.
     *
     * @see ShimmerRayProperties
     */
    @Volatile
    var shimmerRayProperties: ShimmerRayProperties? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Determines whether or not the internal state of the bone representation of
     * this should be saved. Useful when the state could change from true
     * to false (Content to Loading).
     * ```
     * ```
     * **Default:** false
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_SAVE_STATE
     */
    @Volatile
    var allowSavedState: Boolean = false

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Same as **allowSavedState** except that the state is saved using
     * a weak reference. This makes so that the reference to the state will be finalize
     * if it is weakly reachable
     * ```
     * ```
     * **Default:** false
     * ```
     * ```
     * @see allowSavedState
     * @see SkeletonBoneBindings.SKELETON_BONE_WEAK_SAVE_STATE
     *
     * @sample allowSavedState
     */
    @Volatile
    var allowWeakSavedState: Boolean = false

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the duration of the transition between the enabled and the disabled states
     * of the **BoneDrawable** or **Bone**.
     * ```
     * ```
     * **Default:** 2000L
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_SAVE_STATE
     */
    @Volatile
    var transitionDuration: Long = DEFAULT_DURATION

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the corner radius for this bone. This property is most noticeable if the
     * bone has an opaque color and a rectangular shape.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see SkeletonBone
     * @see CornerRadii
     * @see MutableColor
     * @see SkeletonBoneBindings.SKELETON_BONE_CORNER_RADIUS
     */
    @Volatile
    var cornerRadii: CornerRadii? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Determines whether large bones should be dissected "Broken into smaller bones". This
     * is done for changing how the bone is visually represented.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see SkeletonBone
     * @see SkeletonBoneBindings.SKELETON_BONE_DISSECT_LARGE
     */
    @Volatile
    var dissectBones: Boolean? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the min bone thickness.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see SkeletonBone
     * @see SkeletonBoneBindings.SKELETON_BONE_MIN_THICKNESS
     */
    @Volatile
    var minThickness: Float = MIN_THICKNESS

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the bone max thickness.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see SkeletonBone
     * @see SkeletonBoneBindings.SKELETON_BONE_MAX_THICKNESS
     */
    @Volatile
    var maxThickness: Float = MAX_THICKNESS

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the color shade multiplier for the bone. Values less
     * than 1.0f will result in darker shades of the assigned color, while values higher than 1.0f
     * will result in brighter shades of the base bone color
     * ```
     * ```
     * **Default:** 1.0f
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_SHADE_MULTIPLIER
     */
    @Volatile
    var shadeMultiplier: Float = MAX_OFFSET

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Flags the bone's dimension to be computed with the owner view's exact bounds.
     * This will effectively ignore other bound limits that have been set.
     * ```
     * ```
     * **Default:** false
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_MATCH_BOUNDS
     */
    @Volatile
    var matchOwnersBounds: Boolean = false

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the length/width of the bone. This will prevent the bones length to be computed base
     * on the width of the owner view of this bone.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_WIDTH
     */
    @Volatile
    var width: Float? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the min length/width of the bone. This is used to assure there are valid bounds
     * for generating the bone
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_MIN_WIDTH
     */
    @Volatile
    var minWidth: Float? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the thickness/height of the bone. This will prevent the bones thickness to be computed base
     * on the height of the owner view of this bone.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_HEIGHT
     */
    @Volatile
    var height: Float? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the min thickness/height of the bone. This is used to assure there are valid bounds
     * for generating the bone
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_MIN_HEIGHT
     */
    @Volatile
    var minHeight: Float? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets an X translation value for the bone.
     * ```
     * ```
     * **Default:** 0f
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_TRANSLATION_X
     */
    @Volatile
    var translationX: Float = 0f

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets an Y translation value for the bone.
     * ```
     * ```
     * **Default:** 0f
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_TRANSLATION_Y
     */
    @Volatile
    var translationY: Float = 0f

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the color for the bone.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see MutableColor
     * @see SkeletonBoneBindings.SKELETON_BONE_COLOR
     */
    @Volatile
    var color: MutableColor? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the color for the bone base on the owner's background color.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see MutableColor
     */
    @Volatile
    var backgroundColor: MutableColor? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the type of shape the bone should have (Circular | Rectangular).
     * If unset the shape will default to the the shape of the owner view's background
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @see ShapeType
     * @see SkeletonBoneBindings.SKELETON_BONE_SHAPE_TYPE
     */
    @Volatile
    var shapeType: ShapeType? = null

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the toggle view flag. When true, the owner views of the bones are hidden while
     * the loading state is true (Loading).
     * ```
     * ```
     * **Default:** true
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_TOGGLE_VIEW
     */
    @Volatile
    var toggleView: Boolean = true

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the distance between the bone sections for bones that have
     * been dissected.
     * ```
     * ```
     * **Default:** 10dp
     * ```
     * ```
     * @see SkeletonBone
     */
    @Volatile
    var sectionDistance: Float = DISTANCE

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Determines whether or not this bone is shown for the View it was
     * set to.
     * ```
     * ```
     * **Default:** true
     * ```
     * ```
     * @see SkeletonBoneBindings.SKELETON_BONE_ENABLED
     */
    var enabled: Boolean
        get() = enabledProvider?.invoke() ?: false
        set(value) {
            enabledListener?.invoke(value)
        }

    /**
     * Returns a builder for this Bone Property instance.
     */
    internal fun builder(): BoneBuilder {
        return BoneBuilder(this)
    }

    /**
     * Retrieves the shimmer properties for the bone.
     *
     * @see ShimmerRayProperties
     */
    @Synchronized fun getRayShimmerProps(): ShimmerRayProperties? {
        if (this.shimmerRayProperties == null) {
            this.shimmerRayProperties = ShimmerRayProperties()
        }
        return this.shimmerRayProperties
    }

    /**
     * Creates a clone of this Bone Property instance
     */
    override fun clone(): BoneProperties {
        return BoneProperties().also {
            it.width = this.width
            it.height = this.height
            it.disposed = this.disposed
            it.shapeType = this.shapeType
            it.sectionDistance = this.sectionDistance
            it.minThickness = this.minThickness
            it.maxThickness = this.maxThickness
            it.translationX = this.translationX
            it.translationY = this.translationY
            it.dissectBones = this.dissectBones
            it.shadeMultiplier = this.shadeMultiplier
            it.allowSavedState = this.allowSavedState
            it.allowWeakSavedState = this.allowWeakSavedState
            it.transitionDuration = this.transitionDuration
            it.cornerRadii = this.cornerRadii?.clone()
            it.color = this.color?.clone()
            it.background = this.background?.clone()
            it.shimmerRayProperties = this.shimmerRayProperties?.clone()
        }
    }

    internal companion object {

        @JvmStatic val MIN_THICKNESS = 10f.dp
        @JvmStatic val MAX_THICKNESS = 10f.dp

        @JvmStatic val DISTANCE = 10f.dp

        const val OVERFLOW_THRESHOLD = 2.5
        const val HEIGHT_THRESHOLD = 1.5f

        val TAG: Int = BoneProperties::class.java.simpleName.hashCode()
    }
}
