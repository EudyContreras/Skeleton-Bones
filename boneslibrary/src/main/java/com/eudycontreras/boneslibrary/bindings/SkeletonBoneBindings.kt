package com.eudycontreras.boneslibrary.bindings

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.databinding.BindingAdapter
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.MIN_OFFSET
import com.eudycontreras.boneslibrary.doWith
import com.eudycontreras.boneslibrary.extensions.*
import com.eudycontreras.boneslibrary.framework.BonePropertyHolder
import com.eudycontreras.boneslibrary.framework.bones.*
import com.eudycontreras.boneslibrary.framework.bones.Bone
import com.eudycontreras.boneslibrary.framework.bones.BoneManager
import com.eudycontreras.boneslibrary.framework.bones.BoneRenderer
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonProperties
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType
import com.eudycontreras.boneslibrary.tryWith

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 */

object SkeletonBoneBindings {
    /**
     * **Binding Property**: app:skeletonBoneEnabled
     */
    const val SKELETON_BONE_ENABLED = "skeletonBoneEnabled"
    /**
     * **Binding Property**: app:skeletonBoneStateOwner
     */
    const val SKELETON_BONE_STATE_OWNER = "skeletonBoneStateOwner"
    /**
     * **Binding Property**: app:skeletonBoneColor
     */
    const val SKELETON_BONE_COLOR = "skeletonBoneColor"
    /**
     * **Binding Property**: app:skeletonBoneBackground
     */
    const val SKELETON_BONE_BACKGROUND = "skeletonBoneBackground"
    /**
     * **Binding Property**: app:skeletonBoneShadeMultiplier
     */
    const val SKELETON_BONE_SHADE_MULTIPLIER = "skeletonBoneShadeMultiplier"
    /**
     * **Binding Property**: app:skeletonBoneToggleView
     */
    const val SKELETON_BONE_TOGGLE_VIEW = "skeletonBoneToggleView"
    /**
     * **Binding Property**: app:skeletonBoneDissectLargeBones
     */
    const val SKELETON_BONE_DISSECT_LARGE = "skeletonBoneDissectLargeBones"
    /**
     * **Binding Property**: app:skeletonBoneTranslationX
     */
    const val SKELETON_BONE_TRANSLATION_X = "skeletonBoneTranslationX"
    /**
     * **Binding Property**: app:skeletonBoneTranslationY
     */
    const val SKELETON_BONE_TRANSLATION_Y = "skeletonBoneTranslationY"
    /**
     * **Binding Property**: app:skeletonBoneCornerRadius
     */
    const val SKELETON_BONE_CORNER_RADIUS = "skeletonBoneCornerRadius"
    /**
     * **Binding Property**: app:skeletonBoneShapeType
     */
    const val SKELETON_BONE_SHAPE_TYPE = "skeletonBoneShapeType"
    /**
     * **Binding Property**: app:skeletonBoneWidth
     */
    const val SKELETON_BONE_WIDTH = "skeletonBoneWidth"
    /**
     * **Binding Property**: app:skeletonBoneHeight
     */
    const val SKELETON_BONE_HEIGHT = "skeletonBoneHeight"
    /**
     * **Binding Property**: app:skeletonBoneMinWidth
     */
    const val SKELETON_BONE_MIN_WIDTH = "skeletonBoneMinWidth"
    /**
     * **Binding Property**: app:skeletonBoneMinHeight
     */
    const val SKELETON_BONE_MIN_HEIGHT = "skeletonBoneMinHeight"
    /**
     * **Binding Property**: app:skeletonBoneSize
     */
    const val SKELETON_BONE_SIZE = "skeletonBoneSize"
    /**
     * **Binding Property**: app:skeletonBoneMatchBounds
     */
    const val SKELETON_BONE_MATCH_BOUNDS = "skeletonBoneMatchBounds"
    /**
     * **Binding Property**: app:skeletonBoneThickness
     */
    const val SKELETON_BONE_MIN_THICKNESS = "skeletonBoneMinThickness"
    /**
     * **Binding Property**: app:skeletonBoneMaxThickness
     */
    const val SKELETON_BONE_MAX_THICKNESS = "skeletonBoneMaxThickness"
    /**
     * **Binding Property**: app:skeletonBoneIgnored
     */
    const val SKELETON_BONE_IGNORED = "skeletonBoneIgnored"
    /**
     * **Binding Property**: app:skeletonBoneAllowSavedState
     */
    const val SKELETON_BONE_SAVE_STATE = "skeletonBoneAllowSavedState"
    /**
     * **Binding Property**: app:skeletonBoneAllowWeakSavedState
     */
    const val SKELETON_BONE_WEAK_SAVE_STATE = "skeletonBoneAllowWeakSavedState"
    /**
     * **Binding Property**: app:skeletonBoneAllowSavedState
     */
    const val SKELETON_BONE_TRANSITION_DURATION = "skeletonBoneTransitionDuration"
    /**
     * **Binding Property**: app:skeletonBonePropId
     */
    const val SKELETON_BONE_PROPERTY_ID = "skeletonBonePropId"
    /**
     * **Binding Property**: app:skeletonBoneProps
     */
    const val SKELETON_BONE_PROPERTY_PROPS = "skeletonBoneProps"
    /**
     * **Binding Property**: app:skeletonBoneSectionDistance
     */
    const val SKELETON_BONE_SECTION_DISTANCE = "skeletonBoneSectionDistance"
}

///////////////////////// Skeleton Bone Binding Adapters ///////////////////////

/**
 * Adds a bone loader Drawable to this View. The loader is enabled by default.
 * @see BoneDrawable
 *
 * @param enabled Determines if the the drawable is in the loading
 * enabled state.
 *
 * @return The instance of the BoneDrawable that has been added
 */
fun View.addBoneLoader(enabled: Boolean? = true, boneProps: BoneProperties? = null): BoneDrawable {
    doWith(foreground) {
        if (it !is BoneDrawable) {

            val properties: BoneProperties = boneProps ?: getProps(BoneProperties.TAG) ?: BoneProperties()

            val drawableBackground = this.background
            val drawableForeground = properties.background ?: this.foreground

            this.foreground = BoneDrawable(BoneManager(properties = properties).apply {
                this.foreground = drawableForeground
                this.background = drawableBackground
                this.innerBone = Bone.build(this@addBoneLoader, properties, this)
                this.renderer = BoneRenderer(bone = innerBone)
            })

            with (this.foreground as BoneDrawable) {
                this.owner = this@addBoneLoader
                this.enabled = enabled ?: true
                this.baseForeground = drawableForeground
                this.baseBackground = drawableBackground
            }
        }
    }

    return foreground as BoneDrawable
}

fun View.addBoneLoader(
    enabled: Boolean? = true,
    boneLoaderDrawable: BoneDrawable
): BoneDrawable {
    doWith(foreground) {
        if (it !is BoneDrawable) {
            boneLoaderDrawable.resetForReuse()
            val properties: BoneProperties = boneLoaderDrawable.getProps()

            val drawableBackground = this.background
            val drawableForeground = properties.background ?: this.foreground

            boneLoaderDrawable.boneManager.apply {
                this.foreground = drawableForeground
                this.background = drawableBackground
                this.innerBone = Bone.build(this@addBoneLoader, builder.boneProperties, this)
                this.renderer = BoneRenderer(bone = innerBone)
            }

            this.foreground = boneLoaderDrawable.apply {
                this.owner = this@addBoneLoader
                this.enabled = enabled ?: true
                this.baseForeground = drawableForeground
                this.baseBackground = drawableBackground
            }
        }
    }
    return foreground as BoneDrawable
}

@BindingAdapter(
    value = [
        SkeletonBoneBindings.SKELETON_BONE_PROPERTY_ID,
        "prop_${SkeletonBoneBindings.SKELETON_BONE_ENABLED}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_COLOR}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_SHAPE_TYPE}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_MIN_THICKNESS}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_MAX_THICKNESS}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_SECTION_DISTANCE}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_CORNER_RADIUS}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_DISSECT_LARGE}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_BACKGROUND}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_SAVE_STATE}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_WEAK_SAVE_STATE}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_SHADE_MULTIPLIER}",
        "prop_${SkeletonBoneBindings.SKELETON_BONE_TRANSITION_DURATION}",
        "prop_${SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_COLOR}",
        "prop_${SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_TILT}",
        "prop_${SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_COUNT}",
        "prop_${SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_THICKNESS}",
        "prop_${SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_THICKNESS_RATIO}",
        "prop_${SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_SPEED_MULTIPLIER}",
        "prop_${SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_INTERPOLATOR}",
        "prop_${SkeletonBoneShimmerRayBindings.SKELETON_BONE_SHIMMER_RAY_INTERPOLATOR_SHARED}"
    ],
    requireAll = false
)
internal fun View.setBonePropertyId(
    propId: Int?,
    enabled: Boolean?,
    boneColor: Int?,
    shapeType: ShapeType?,
    minThickness: Float?,
    maxThickness: Float?,
    sectionDistance: Float?,
    cornerRadius: Float?,
    dissect: Boolean?,
    background: Drawable?,
    allowSavedState: Boolean?,
    allowWeakSavedState: Boolean?,
    shadeMultiplier: Float?,
    animDuration: Long?,
    shimmerRayColor: Int?,
    shimmerRayTilt: Float?,
    shimmerRayCount: Int?,
    shimmerRayThickness: Float?,
    shimmerRayThicknessRatio: Float?,
    shimmerRaySpeedMultiplier: Float?,
    shimmerRayInterpolator: Interpolator?,
    shimmerRayInterpolatorShared: Boolean?
) {
    val parent = findParent()

    val props = parent?.findView { it is BonePropertyHolder && it.propId == propId }

    val propsHolder = props as? BonePropertyHolder?

    if (propsHolder != null) {
        if (hasProps(BoneProperties.TAG)) {
            setSkeletonBoneEnabled(enabled = enabled)
        } else {
            val properties: BoneProperties = propsHolder.boneProperties.clone()

            if (foreground !is BoneDrawable) {
                properties.background = this.background?.clone()
            }
            setSkeletonBoneEnabledAndProps(enabled = enabled, boneProps = properties)
        }

        if (boneColor != null) setSkeletonBoneColor(boneColor = boneColor)
        if (shapeType != null) setSkeletonBoneShapeType(shapeType = shapeType)
        if (minThickness != null) setSkeletonBoneMinThickness(thickness = minThickness)
        if (maxThickness != null) setSkeletonBoneMaxThickness(thickness = maxThickness)
        if (sectionDistance != null) setBoneSectionDistance(distance = sectionDistance)
        if (cornerRadius != null) setSkeletonBoneCorners(cornerRadius = cornerRadius)
        if (dissect != null) setSkeletonBoneDissectLargeBones(dissect = dissect)
        if (background != null) setSkeletonBoneBackground(background = background)
        if (allowSavedState != null) setSkeletonBoneAllowSavedState(allowSavedState = allowSavedState)
        if (allowWeakSavedState != null) setSkeletonBoneAllowWeakSavedState(allowSavedState = allowWeakSavedState)
        if (shadeMultiplier != null) setSkeletonBoneShadeMultiplier(shadeMultiplier = shadeMultiplier)
        if (animDuration != null) setSkeletonBoneTransitionDuration(duration = animDuration)

        /**
         * Set shimmer props
         */
        if (shimmerRayColor != null) setSkeletonBoneShimmerRayColor(rayColor = shimmerRayColor)
        if (shimmerRayTilt != null) setSkeletonBoneShimmerRayTilt(rayTilt = shimmerRayTilt)
        if (shimmerRayCount != null) setSkeletonBoneShimmerRayCount(count = shimmerRayCount)
        if (shimmerRayThickness != null) setSkeletonBoneShimmerRayThickness(thickness = shimmerRayThickness)
        if (shimmerRayThicknessRatio != null) setSkeletonBoneShimmerRayThicknessRatio(thicknessRatio = shimmerRayThicknessRatio)
        if (shimmerRaySpeedMultiplier != null) setSkeletonBoneShimmerRaySpeedMultiplier(speedMultiplier = shimmerRaySpeedMultiplier)
        if (shimmerRayInterpolator != null) setSkeletonBoneShimmerRayInterpolator(interpolator = shimmerRayInterpolator)
        if (shimmerRayInterpolatorShared != null) setSkeletonBoneShimmerRaySharedInterpolator(shared = shimmerRayInterpolatorShared)
    }
}

internal fun View.setSkeletonBoneEnabledAndProps(enabled: Boolean?, boneProps: BoneProperties? = null) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().enabled = enabled ?: true
        } else {
            val id = generateId()
            val parent = getParentSkeletonDrawable()

            if (parent != null) {
                parent.getProps().setStateOwner(id, enabled ?: true)
                if (enabled == false) {
                    parent.getProps().getBoneProps(id).apply {
                        this.enabled = enabled
                    }
                }
            } else {
                addBoneLoader(enabled = true, boneProps = boneProps)
                setSkeletonBoneEnabledAndProps(enabled, null)
            }
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_PROPERTY_PROPS)
internal fun View.setSkeletonBoneProps(boneProps: BoneProperties? = null) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        if (boneProps != null) {
            parent.getProps().settBoneProps(ownerId, boneProps)
        }
    } else {
        setBoneProps(boneProps)
    }
}

private fun View.setBoneProps(boneProps: BoneProperties? = null) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            if (boneProps != null) {
                it.setProps(boneProps)
            }
        } else {
            addBoneLoader(enabled = true, boneProps = boneProps)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_ENABLED)
internal fun View.setSkeletonBoneEnabled(enabled: Boolean?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().enabled = enabled ?: true
        } else {
            val id = generateId()
            val parent = getParentSkeletonDrawable()

            if (parent != null) {
                parent.getProps().setStateOwner(id, enabled ?: true)
                if (enabled == false) {
                    parent.getProps().getBoneProps(id).apply {
                        this.enabled = enabled
                    }
                }
            } else {
                addBoneLoader(enabled = true)
                setSkeletonBoneEnabled(enabled)
            }
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_STATE_OWNER)
internal fun View.setSkeletonBoneStateOwner(stateOwner: Boolean?) {
    val id = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().setStateOwner(id, stateOwner ?: true)
    } else {
        addBoneLoader(enabled = true)
        setSkeletonBoneStateOwner(stateOwner)
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_BACKGROUND)
internal fun View.setSkeletonBoneBackground(background: Drawable?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.baseForeground = background
        } else {
            addBoneLoader(enabled = true)
            setSkeletonBoneBackground(background)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_SAVE_STATE)
internal fun View.setSkeletonBoneAllowSavedState(allowSavedState: Boolean?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().allowSavedState = allowSavedState ?: true
        } else {
            addBoneLoader(enabled = true)
            setSkeletonBoneAllowSavedState(allowSavedState)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_WEAK_SAVE_STATE)
internal fun View.setSkeletonBoneAllowWeakSavedState(allowSavedState: Boolean?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().allowWeakSavedState = allowSavedState ?: true
        } else {
            addBoneLoader(enabled = true)
            setSkeletonBoneAllowWeakSavedState(allowSavedState)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_TOGGLE_VIEW)
internal fun View.setSkeletonBoneToggleView(toggleView: Boolean?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.toggleView = toggleView ?: true
        }
    } else {
        setSkeletonBoneToggleView(toggleView)
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_MATCH_BOUNDS)
internal fun View.setSkeletonBoneMatchBounds(matchBounds: Boolean?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.matchOwnersBounds = matchBounds ?: false
        }
    } else {
        setSkeletonBoneMatchBounds(matchBounds)
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_SHAPE_TYPE)
internal fun View.setSkeletonBoneShapeType(shapeType: ShapeType?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.shapeType = shapeType
        }
    } else {
        setBoneShapeType(shapeType)
    }
}

private fun View.setBoneShapeType(shapeType: ShapeType?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().shapeType = shapeType
        } else {
            addBoneLoader(enabled = true)
            setBoneShapeType(shapeType)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_SECTION_DISTANCE)
internal fun View.setSkeletonBoneSectionDistance(@Dimension distance: Float?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.sectionDistance = distance ?: return
        }
    } else {
        setBoneSectionDistance(distance)
    }
}

private fun View.setBoneSectionDistance(@Dimension distance: Float?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().sectionDistance = distance ?: return
        } else {
            addBoneLoader(enabled = true)
            setBoneSectionDistance(distance)
        }
    }
}


@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_COLOR)
internal fun View.setSkeletonBoneColor(@ColorInt boneColor: Int?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.color = boneColor?.let { MutableColor(it) }
        }
    } else {
        setBoneColor(boneColor)
    }
}

private fun View.setBoneColor(@ColorInt boneColor: Int?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().color = boneColor?.let { color -> MutableColor(color) }
        } else {
            addBoneLoader(enabled = true)
            setBoneColor(boneColor)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_SHADE_MULTIPLIER)
internal fun View.setSkeletonBoneShadeMultiplier(shadeMultiplier: Float?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.shadeMultiplier = shadeMultiplier ?: MAX_OFFSET
        }
    } else {
        setBoneShadeMultiplier(shadeMultiplier)
    }
}

private fun View.setBoneShadeMultiplier(shadeMultiplier: Float?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().shadeMultiplier = shadeMultiplier ?: MAX_OFFSET
        } else {
            addBoneLoader(enabled = true)
            setBoneShadeMultiplier(shadeMultiplier)
        }
    }
}


@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_CORNER_RADIUS)
internal fun View.setSkeletonBoneCorners(cornerRadius: Float?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.cornerRadii = CornerRadii(cornerRadius ?: MIN_OFFSET)
        }
    } else {
        setBoneCorners(cornerRadius)
    }
}

private fun View.setBoneCorners(cornerRadius: Float?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().cornerRadii = CornerRadii(cornerRadius ?: MIN_OFFSET)
        } else {
            addBoneLoader(enabled = true)
            setBoneCorners(cornerRadius)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_TRANSITION_DURATION)
internal fun View.setSkeletonBoneTransitionDuration(duration: Long?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.transitionDuration = duration ?: SkeletonProperties.DEFAULT_TRANSITION_DURATION
        }
    } else {
        setBoneTransitionDuration(duration)
    }
}

private fun View.setBoneTransitionDuration(duration: Long?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().transitionDuration = duration ?: SkeletonProperties.DEFAULT_TRANSITION_DURATION
        } else {
            addBoneLoader(enabled = true)
            setBoneTransitionDuration(duration)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_WIDTH)
internal fun View.setSkeletonBoneWidth(width: Float?) {
    val ownerId = generateId()
    getParentSkeletonDrawable()?.let {
        it.getProps().getBoneProps(ownerId).apply {
            this.width = width
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_MIN_WIDTH)
internal fun View.setSkeletonBoneMinWidth(minWidth: Float?) {
    val ownerId = generateId()
    getParentSkeletonDrawable()?.let {
        it.getProps().getBoneProps(ownerId).apply {
            this.minWidth = minWidth
        }
    }
}


@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_HEIGHT)
internal fun View.setSkeletonBoneHeight(height: Float?) {
    val ownerId = generateId()
    getParentSkeletonDrawable()?.let {
        it.getProps().getBoneProps(ownerId).apply {
            this.height = height
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_MIN_HEIGHT)
internal fun View.setSkeletonBoneMinHeight(minHeight: Float?) {
    val ownerId = generateId()
    getParentSkeletonDrawable()?.let {
        it.getProps().getBoneProps(ownerId).apply {
            this.minHeight = minHeight
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_MIN_THICKNESS)
internal fun View.setSkeletonBoneMinThickness(thickness: Float?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.minThickness = thickness ?: return
        }
    } else {
        setBoneMinThickness(thickness)
    }
}

private fun View.setBoneMinThickness(thickness: Float?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().minThickness = thickness ?: return
        } else {
            addBoneLoader(enabled = true)
            setBoneMinThickness(thickness)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_MAX_THICKNESS)
internal fun View.setSkeletonBoneMaxThickness(thickness: Float?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.maxThickness = thickness ?: return
        }
    } else {
        setBoneMaxThickness(thickness)
    }
}

private fun View.setBoneMaxThickness(thickness: Float?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().maxThickness = thickness ?: return
        } else {
            addBoneLoader(enabled = true)
            setBoneMaxThickness(thickness)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_SIZE)
internal fun View.setSkeletonBoneSize(value: Float?) {
    tryWith {
        setSkeletonBoneWidth(value)
        setSkeletonBoneHeight(value)
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_TRANSLATION_X)
internal fun View.setSkeletonBoneTranslationX(@Dimension translation: Float?) {
    val ownerId = generateId()
    getParentSkeletonDrawable()?.let {
        it.getProps().getBoneProps(ownerId).apply {
            this.translationX = translation ?: MIN_OFFSET
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_TRANSLATION_Y)
internal fun View.setSkeletonBoneTranslationY(@Dimension translation: Float?) {
    val ownerId = generateId()
    getParentSkeletonDrawable()?.let {
        it.getProps().getBoneProps(ownerId).apply {
            this.translationY = translation ?: MIN_OFFSET
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_DISSECT_LARGE)
internal fun View.setSkeletonBoneDissectLargeBones(dissect: Boolean?) {
    val ownerId = generateId()
    val parent = getParentSkeletonDrawable()

    if (parent != null) {
        parent.getProps().getBoneProps(ownerId).apply {
            this.dissectBones = dissect
        }
    } else {
        setBoneDissectLargeBones(dissect)
    }
}

private fun View.setBoneDissectLargeBones(dissect: Boolean?) {
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().dissectBones = dissect
        } else {
            addBoneLoader(enabled = true)
            setBoneDissectLargeBones(dissect)
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_IGNORED)
internal fun View.setSkeletonIgnored(ignored: Boolean?) {
    val ownerId = generateId()
    getParentSkeletonDrawable()?.let {
        val props = it.getProps()
        val ignoredParent = findParent { parent -> props.isIgnored(parent.generateId()) }

        if (ignoredParent == null) {
            if (ignored == true) {
                props.addIgnored(ownerId)
            } else {
                props.removeIgnored(ownerId)
            }
        }
    }
}

/**
 * Finds the nearest parent of this view that has
 * an skeleton loader and returns the SkeletonDrawable loader
 * associated with it.
 * @see SkeletonDrawable
 */
fun View.getParentSkeletonDrawable(): SkeletonDrawable? {
    val skeletonParent = findParent { it.foreground is SkeletonDrawable }
    return skeletonParent?.foreground as? SkeletonDrawable
}

/**
 * Returns true if this view has an ancestor with an attached
 * SkeletonDrawable loader
 * @see SkeletonDrawable
 */
fun View.hasSkeletonLoaderAncestor(): Boolean {
    return getParentSkeletonDrawable() != null
}

/**
 * Returns true if this view has BoneDrawable loader as its foreground
 */
fun View.isBoneLoader(): Boolean {
    return this.foreground is BoneDrawable
}

/**
 * Returns the BoneDrawable loader of this View or null if it does not have one
 */
fun View.getBoneDrawable(): BoneDrawable? {
    return this.foreground as? BoneDrawable?
}

/**
 * Returns the BoneDrawable loader of this ViewGroup
 * @throws IllegalStateException when this view does not contain a BoneDrawable
 */
fun ViewGroup.requireBoneDrawable(): BoneDrawable {
    return runCatching { this.foreground as BoneDrawable }.getOrElse {
        throw IllegalStateException("This view does not contain a BoneDrawable")
    }
}