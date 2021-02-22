package com.eudycontreras.boneslibrary.framework.skeletons

import android.view.View
import com.eudycontreras.boneslibrary.bindings.SkeletonBindings
import com.eudycontreras.boneslibrary.extensions.generateId
import com.eudycontreras.boneslibrary.framework.bones.BoneBuilder
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayBuilder
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties.Companion.DEFAULT_DURATION
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType

//TODO Update to reflect new properties

/**
 * Copyright (C) 2020 Project Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 *
 * Builder class used for building the properties used for rendering **Skeletons**
 *
 * For a more thorough description about **Skeletons** please visit the link:
 * [Skeleton-Bones](https://github.com/EudyContreras/Skeleton-Bones) library on GitHub.
 *
 * @see SkeletonProperties
 */

class SkeletonBuilder(
    internal val skeletonProperties: SkeletonProperties = SkeletonProperties()
) {

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Allows building bone properties.
     *
     * @param builder The target encapsulation of the bone builder
     *
     * @see BoneProperties
     * @see BoneBuilder
     */
    fun withBoneBuilder(builder: BoneBuilder.() -> Unit): SkeletonBuilder {
        builder.invoke(skeletonProperties.getBoneProps().builder())
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since February 2021
     *
     * Allows building bone properties.
     *
     * @param view The view of the bone to be built.
     * @param builder The target encapsulation of the bone builder
     *
     * @see BoneProperties
     * @see BoneBuilder
     */
    fun withBoneBuilder(view: View, builder: BoneBuilder.() -> Unit): SkeletonBuilder {
        builder.invoke(skeletonProperties.getBoneProperties(view.generateId()).builder())
        return this
    }

    /**
      * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Allows building bone properties.
     *
     * @param ownerId The owner of the bone to be built.
     * @param builder The target encapsulation of the bone builder
     *
     * @see BoneProperties
     * @see BoneBuilder
     */
    fun withBoneBuilder(ownerId: Int, builder: BoneBuilder.() -> Unit): SkeletonBuilder {
        builder.invoke(skeletonProperties.getBoneProperties(ownerId).builder())
        return this
    }

    /**
     * Allows building shimmer ray properties.
     *
     * @param builder The target encapsulation of the shimmer ray builder
     *
     * @see BoneProperties
     * @see BoneBuilder
     */
    fun withShimmerBuilder(builder: ShimmerRayBuilder.() -> Unit): SkeletonBuilder {
        builder.invoke(skeletonProperties.shimmerRayProperties.builder())
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the duration of the transition between the enabled and the disabled states
     * of the **SkeletonDrawable**.
     * ```
     * ```
     * **Default:** 250L
     * ```
     * ```
     * @param duration The total duration of the transition
     *
     * @see SkeletonBindings.SKELETON_TRANSITION_DURATION
     */
    fun setStateTransitionDuration(duration: Long = DEFAULT_DURATION): SkeletonBuilder {
        this.skeletonProperties.stateTransitionDuration = duration
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Determines if a transition should be used for removing the **SkeletonDrawable**
     * from the **ViewGroup**
     * ```
     * ```
     * **Default:** true
     * ```
     * ```
     * @param useTransition Determines whether or not a transition should be used
     *
     * @see SkeletonBindings.SKELETON_USE_TRANSITION
     */
    fun setUseStateTransition(useTransition: Boolean = true): SkeletonBuilder {
        this.skeletonProperties.useStateTransition = useTransition
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Determines whether or not the internal state of the skeleton representation of
     * this should be saved. Useful when the state could change from true
     * to false (Content to Loading).
     * ```
     * ```
     * **Default:** false
     * ```
     * ```
     * @param allowSavedState Flags whether or not the internal state of the skeleton
     * drawable should be saved.
     *
     * @see SkeletonBindings.SKELETON_SAVE_STATE
     */
    fun setAllowSavedState(allowSavedState: Boolean = false): SkeletonBuilder {
        this.skeletonProperties.allowSavedState = allowSavedState
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the background color used by this **Skeleton**. Default: null. This should be ideally
     * used together with corner radius and shape type.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @param color The color of the skeleton drawable
     *
     * @see SkeletonBone
     * @see MutableColor
     * @see ShapeType
     * @see CornerRadii
     * @see SkeletonBindings.SKELETON_BACKGROUND_COLOR
     */
    fun setColor(color: MutableColor? = null): SkeletonBuilder {
        this.skeletonProperties.skeletonBackgroundColor = color
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since February 2021
     *
     *
     * When true, view bounds that have been resize in order to accommodate minimum dimensions
     * set in order to build valid bones will be restored to their default values using a simple layout
     * animation. This is done to provide a smoother using experience in such cases.
     * ```
     * ```
     * **Default:** false
     * ```
     * ```
     * @see animateRestoreBounds
     * @see SkeletonBindings.SKELETON_ANIMATE_RESTORED_BOUNDS
     */
    fun setAnimateRestoreBounds(animateRestoreBounds: Boolean = false): SkeletonBuilder {
        this.skeletonProperties.animateRestoredBounds = animateRestoreBounds
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the corner radius for this skeleton. This property is most noticeable if the
     * skeleton has an opaque color and a rectangular shape.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @param cornerRadii The wrapper container information about the radius of
     * each corner of the SkeletonDrawable
     *
     * @see SkeletonBone
     * @see CornerRadii
     * @see MutableColor
     * @see SkeletonBindings.SKELETON_CORNER_RADIUS
     */
    fun setCornerRadii(cornerRadii: CornerRadii? = null): SkeletonBuilder {
        this.skeletonProperties.skeletonCornerRadii = cornerRadii
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the corner radius for this skeleton. This property is most noticeable if the
     * skeleton has an opaque color and a rectangular shape.
     * ```
     * ```
     * **Default:** null
     * ```
     * ```
     * @param cornerRadius The amount of rounding radius to add to the corners
     *
     * @see SkeletonBone
     * @see CornerRadii
     * @see MutableColor
     * @see SkeletonBindings.SKELETON_CORNER_RADIUS
     */
    fun setCornerRadii(cornerRadius: Float? = null): SkeletonBuilder {
        this.skeletonProperties.skeletonCornerRadii = cornerRadius?.let { CornerRadii(cornerRadius) }
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Determines whether or not this Skeleton is shown for the ViewGroup it was
     * set to.
     * ```
     * ```
     * **Default:** true
     * ```
     * ```
     * @param enabled Flags that this bone is currently shown or
     * in its loading state.
     * @see SkeletonBindings.SKELETON_ENABLED
     */
    fun setEnabled(enabled: Boolean = true): SkeletonBuilder {
        this.skeletonProperties.enabled = enabled
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since February 2021
     *
     * Adds the given ids to ignored list so that the respective views
     * are ignored upon bone generation
     * ```
     * ```
     * @param ids The ids of the views to be ignore upon bone loader
     * generation
     */
    fun withIgnoredBones(vararg ids: Int): SkeletonBuilder {
        this.skeletonProperties.addIgnored(*ids)
        return this
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since February 2021
     *
     * Adds the ids of the given views to ignored list so that the respective views
     * are ignored upon bone generation
     * ```
     * ```
     * @param views The views to be ignore upon bone loader
     * generation
     */
    fun withIgnoredBones(vararg views: View): SkeletonBuilder {
        val ids = views.map { it.generateId() }.toIntArray()
        this.skeletonProperties.addIgnored(*ids)
        return this
    }
}
