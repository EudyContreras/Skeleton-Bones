package com.eudycontreras.boneslibrary.framework.bones

import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.bindings.SkeletonBoneBindings
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties.Companion.DISTANCE
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties.Companion.MAX_THICKNESS
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties.Companion.MIN_THICKNESS
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayBuilder
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonBone
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonProperties
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType

/**
 * Copyright (C) 2020 Project Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 * Builder class used for building the properties used for rendering **Bones**
 *
 * For a more thorough description about **Bones** please visit the link:
 * [Skeleton-Bones](https://github.com/EudyContreras/Skeleton-Bones) library on GitHub.
 * @see BoneProperties
 */

data class BoneBuilder internal constructor(
    private val boneProperties: BoneProperties
) {

    /**
     * Allows building shimmer ray properties.
     *
     * @param builder The target encapsulation of the shimmer ray builder
     *
     * @see BoneProperties
     * @see BoneBuilder
     */
    fun withShimmerBuilder(builder: ShimmerRayBuilder.() -> Unit): BoneBuilder {
        boneProperties.getRayShimmerProps()?.let {
            builder.invoke(it.builder())
        }
        return this
    }
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
     * @param allow Sets whether or not saving the state should be allowed
     * @see SkeletonBoneBindings.SKELETON_BONE_SAVE_STATE
     */
    fun setAllowSavedState(allow: Boolean = false): BoneBuilder {
        this.boneProperties.allowSavedState = allow
        return this
    }
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
     * @param duration The total duration of the animation
     * @see SkeletonBoneBindings.SKELETON_BONE_TRANSITION_DURATION
     */
    fun setTransitionDuration(duration: Long = SkeletonProperties.DEFAULT_TRANSITION_DURATION): BoneBuilder {
        this.boneProperties.transitionDuration = duration
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the corner radius for this bone. This property has an effect when the
     * bone has an opaque color and a rectangular shape.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @param cornerRadii The corner radii wrapper class
     * @see SkeletonBone
     * @see CornerRadii
     * @see MutableColor
     * @see SkeletonBoneBindings.SKELETON_BONE_CORNER_RADIUS
     */
    fun setCornerRadii(cornerRadii: CornerRadii? = null): BoneBuilder {
        this.boneProperties.cornerRadii = cornerRadii
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the corner radius for this bone. This property has an effect when the
     * bone has an opaque color and a rectangular shape.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @param cornerRadius The corner radius amount
     * @see SkeletonBone
     * @see CornerRadii
     * @see MutableColor
     * @see SkeletonBoneBindings.SKELETON_BONE_CORNER_RADIUS
     */
    fun setCornerRadius(cornerRadius: Float = 0f): BoneBuilder {
        return this.setCornerRadii(CornerRadii(cornerRadius))
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Determines whether or not large bones should be dissected "Broken into smaller bones". This
     * is done for changing how the bone is visually represented.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @param dissect Determines if the bones should be dissected
     * @see SkeletonBone
     * @see SkeletonBoneBindings.SKELETON_BONE_DISSECT_LARGE
     */
    fun setDissectBones(dissect: Boolean? = null): BoneBuilder {
        this.boneProperties.dissectBones = dissect
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the minimum thickness for the bone. If the owner view is
     * thinner the thickness will default to the set value.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @param minThickness The minimum thickness of the bone in pixels
     * @see SkeletonBone
     * @see SkeletonBoneBindings.SKELETON_BONE_MIN_THICKNESS
     */
    fun setMinThickness(minThickness: Float? = null): BoneBuilder {
        this.boneProperties.minThickness = minThickness ?: MIN_THICKNESS
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the bone max thickness. Use tell the bone when it should be
     * dissected if dissection is allowed.
     * ```
     * ```
     * **Default:** 10dp
     * ```
     * ```
     * @param maxThickness The maximum height or thickness of the bones
     * at which the bone should be dissected if the dissect bones flag
     * is set to true
     * @see SkeletonBone
     * @see SkeletonBoneBindings.SKELETON_BONE_MAX_THICKNESS
     */
    fun setMaxThickness(maxThickness: Float = MAX_THICKNESS): BoneBuilder {
        this.boneProperties.maxThickness = maxThickness
        return this
    }

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
     * @param shadeMultiplier The amount the bone color shade should be
     * multiplied by.
     * @see SkeletonBoneBindings.SKELETON_BONE_SHADE_MULTIPLIER
     */
    fun setShaderMultiplier(shadeMultiplier: Float = MAX_OFFSET): BoneBuilder {
        this.boneProperties.shadeMultiplier = shadeMultiplier
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the width of the bone. This will prevent the bones length to be computed base
     * on the width of the owner view of this bone.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @param width The total width/length of the bone in pixels
     * @see SkeletonBoneBindings.SKELETON_BONE_WIDTH
     */
    fun setWidth(width: Float? = null): BoneBuilder {
        this.boneProperties.width = width
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the height of the bone. This will prevent the bones length to be computed base
     * on the width of the owner view of this bone.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @param height The total thickness/height of the bone in pixels
     * @see SkeletonBoneBindings.SKELETON_BONE_HEIGHT
     */
    fun setHeight(height: Float? = null): BoneBuilder {
        this.boneProperties.height = height
        return this
    }

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
     * @param translationX The amount of horizontal translation
     * to apply to the bone.
     * @see SkeletonBoneBindings.SKELETON_BONE_TRANSLATION_X
     */
    fun setTranslationX(translationX: Float = 0f): BoneBuilder {
        this.boneProperties.translationX = translationX
        return this
    }

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
     * @param translationY The amount of vertical translation
     * to apply to the bone.
     * @see SkeletonBoneBindings.SKELETON_BONE_TRANSLATION_Y
     */
    fun setTranslationY(translationY: Float = 0f): BoneBuilder {
        this.boneProperties.translationY = translationY
        return this
    }

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
     * @param color The color of the bone
     * @see MutableColor
     * @see SkeletonBoneBindings.SKELETON_BONE_COLOR
     */
    fun setColor(color: MutableColor? = null): BoneBuilder {
        this.boneProperties.color = color
        return this
    }

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
     * @param shapeType The type of shape of the bone
     * @see ShapeType
     * @see SkeletonBoneBindings.SKELETON_BONE_SHAPE_TYPE
     */
    fun setShapeType(shapeType: ShapeType? = null): BoneBuilder {
        this.boneProperties.shapeType = shapeType
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the distance between the bone parts when the bone has
     * been dissected.
     * ```
     * ```
     * **Default:** 10dp
     * ```
     * ```
     * @param distance The distance between dissected bones.
     * @see SkeletonBone
     */
    fun setMaxDistance(distance: Float = DISTANCE): BoneBuilder {
        this.boneProperties.sectionDistance = distance
        return this
    }

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
     * @param enabled Flags that this bone is currently shown or
     * in its loading state.
     * @see SkeletonBoneBindings.SKELETON_BONE_ENABLED
     */
    fun setEnabled(enabled: Boolean = true): BoneBuilder {
        this.boneProperties.enabled = enabled
        return this
    }
}
