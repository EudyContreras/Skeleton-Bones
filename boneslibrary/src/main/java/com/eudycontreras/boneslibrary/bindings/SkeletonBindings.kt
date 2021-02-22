@file:Suppress("UNCHECKED_CAST")

package com.eudycontreras.boneslibrary.bindings

import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.eudycontreras.boneslibrary.MIN_OFFSET
import com.eudycontreras.boneslibrary.doWith
import com.eudycontreras.boneslibrary.extensions.descendantViews
import com.eudycontreras.boneslibrary.extensions.generateId
import com.eudycontreras.boneslibrary.extensions.getProps
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonManager
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonProperties
import com.eudycontreras.boneslibrary.properties.Color
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 */

object SkeletonBindings {
    /**
     * **Binding Property**: skeletonEnabled
     */
    const val SKELETON_ENABLED = "skeletonEnabled"
    /**
     * **Binding Property**: skeletonColor
     */
    const val SKELETON_BACKGROUND_COLOR = "skeletonBackgroundColor"
    /**
     * **Binding Property**: skeletonCornerRadius
     */
    const val SKELETON_CORNER_RADIUS = "skeletonCornerRadius"
    /**
     * **Binding Property**: skeletonLayoutIgnored
     */
    const val SKELETON_IGNORED_LAYOUT = "skeletonLayoutIgnored"
    /**
     * **Binding Property**: skeletonAllowSavedState
     */
    const val SKELETON_SAVE_STATE = "skeletonAllowSavedState"
    /**
     * **Binding Property**: skeletonAllowWeakSavedState
     */
    const val SKELETON_SAVE_WEAK_STATE = "skeletonAllowWeakSavedState"
    /**
     * **Binding Property**: skeletonUseStateTransition
     */
    const val SKELETON_USE_TRANSITION= "skeletonUseStateTransition"
    /**
     * **Binding Property**: skeletonTransitionDuration
     */
    const val SKELETON_TRANSITION_DURATION = "skeletonTransitionDuration"
    /**
     * **Binding Property**: skeletonDissectLargeBones
     */
    const val SKELETON_DISSECT_LARGE_BONES = "skeletonDissectLargeBones"
    /**
     * **Binding Property**: skeletonGenerateBones
     */
    const val SKELETON_GENERATE_BONES = "skeletonGenerateBones"
    /**
     * **Binding Property**: skeletonAnimateRestoredBounds
     */
    const val SKELETON_ANIMATE_RESTORED_BOUNDS = "skeletonAnimateRestoredBounds"
}

///////////////////////// Skeleton Binding Adapters ///////////////////////

/**
 * Adds a Skeleton Loader Drawable to this ViewGroup. The loader is enabled by default.
 * @see SkeletonDrawable
 *
 * @param enabled Determines if the the drawable is in the loading
 * enabled state.
 *
 * @return The instance of the SkeletonDrawable that has been added
 */
fun ViewGroup.addSkeletonLoader(enabled: Boolean?, properties: SkeletonProperties = SkeletonProperties()): SkeletonDrawable {
    doWith(foreground) {
        if (it !is SkeletonDrawable) {

            val drawableBackground = this.background
            val drawableForeground = this.foreground

            this.foreground = SkeletonDrawable(SkeletonManager(getProps(SkeletonProperties.TAG) ?:properties))

            with (this.foreground as SkeletonDrawable) {
                this.owner = this@addSkeletonLoader
                this.enabled = enabled ?: true
                this.baseDrawableForeground = drawableForeground
                this.baseDrawableBackground = drawableBackground
            }
        }
    }
    return foreground as SkeletonDrawable
}

@BindingAdapter(SkeletonBindings.SKELETON_ENABLED)
internal fun ViewGroup.setSkeletonEnabled(enabled: Boolean?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.owner = this
            it.getProps().enabled = enabled ?: true
        } else {
            val parent = getParentSkeletonDrawable()

            if (parent != null) {
                descendantViews().forEach { view ->
                    val id = view.generateId()
                    parent.getProps().setStateOwner(id, enabled ?: true)
                }
            } else {
                addSkeletonLoader(enabled = true)
                setSkeletonEnabled(enabled)
            }
        }
    }
}

@BindingAdapter(SkeletonBindings.SKELETON_GENERATE_BONES)
internal fun ViewGroup.setSkeletonGenerateBones(generateBones: Boolean?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().allowBoneGeneration = generateBones ?: true
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonGenerateBones(generateBones)
        }
    }
}

@BindingAdapter(SkeletonBindings.SKELETON_SAVE_STATE)
internal fun ViewGroup.setSkeletonAllowSavedState(allowSavedState: Boolean?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().allowSavedState = allowSavedState ?: true
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonAllowSavedState(allowSavedState)
        }
    }
}

@BindingAdapter(SkeletonBindings.SKELETON_SAVE_WEAK_STATE)
internal fun ViewGroup.setSkeletonAllowWeakSavedState(allowSavedState: Boolean?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().allowWeakSavedState = allowSavedState ?: true
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonAllowWeakSavedState(allowSavedState)
        }
    }
}


@BindingAdapter(SkeletonBindings.SKELETON_ANIMATE_RESTORED_BOUNDS)
internal fun ViewGroup.setSkeletonAnimateRestoredBounds(animateRestoredBounds: Boolean?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().animateRestoredBounds = animateRestoredBounds ?: false
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonAnimateRestoredBounds(animateRestoredBounds)
        }
    }
}

@BindingAdapter(SkeletonBindings.SKELETON_TRANSITION_DURATION)
internal fun ViewGroup.setStateTransitionDuration(transitionDuration: Long?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().stateTransitionDuration = transitionDuration ?: SkeletonProperties.DEFAULT_TRANSITION_DURATION
        } else {
            addSkeletonLoader(enabled = true)
            setStateTransitionDuration(transitionDuration)
        }
    }
}

@BindingAdapter(SkeletonBindings.SKELETON_USE_TRANSITION)
internal fun ViewGroup.setSkeletonUseTransition(useTransition: Boolean?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().useStateTransition = useTransition ?: true
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonUseTransition(useTransition)
        }
    }
}

@BindingAdapter(SkeletonBindings.SKELETON_BACKGROUND_COLOR)
internal fun ViewGroup.setSkeletonBackgroundColor(@ColorInt color: Int?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().skeletonBackgroundColor = MutableColor.fromColor(color)
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonBackgroundColor(color)
        }
    }
}

@BindingAdapter(SkeletonBindings.SKELETON_CORNER_RADIUS)
internal fun ViewGroup.setSkeletonCornerRadius(cornerRadius: Float?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            it.getProps().skeletonCornerRadii = CornerRadii(cornerRadius ?: MIN_OFFSET)
        } else {
            addSkeletonLoader(enabled = true)
            setSkeletonCornerRadius(cornerRadius)
        }
    }
}

@BindingAdapter(SkeletonBindings.SKELETON_DISSECT_LARGE_BONES)
internal fun ViewGroup.setSkeletonDissectLargeBones(dissect: Boolean?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            descendantViews().forEach { view ->
                val id = view.generateId()
                it.getProps().getBoneProps(id).apply {
                    this.dissectBones = dissect
                }
            }
        } else {
            val parent = getParentSkeletonDrawable()

            if (parent != null) {
                descendantViews().forEach { view ->
                    val id = view.generateId()
                    parent.getProps().getBoneProps(id).apply {
                        this.dissectBones = dissect
                    }
                }
            } else {
                addSkeletonLoader(enabled = true)
                setSkeletonDissectLargeBones(dissect)
            }
        }
    }
}

@BindingAdapter(SkeletonBindings.SKELETON_IGNORED_LAYOUT)
internal fun ViewGroup.setSkeletonLayoutIgnored(ignored: Boolean?) {
    if (this.foreground is SkeletonDrawable) return

    getParentSkeletonDrawable()?.let {
        val props = it.getProps()
        val children = this.descendantViews()
        if (ignored == true) {
            props.addIgnored(generateId())
        } else {
            props.removeIgnored(generateId())
        }
        for (child in children) {
            if (ignored == true) {
                props.addIgnored(child.generateId())
            } else {
                props.removeIgnored(child.generateId())
            }
        }
    }
}

///////////////////////// Skeleton Bone Binding Adapters ///////////////////////

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_COLOR)
internal fun ViewGroup.setSkeletonBoneColor(@ColorInt boneColor: Int?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            descendantViews().forEach { view ->
                val id = view.generateId()
                it.getProps().getBoneProps(id).apply {
                    this.color = MutableColor(boneColor ?: Color.MIN_COLOR)
                }
            }
        } else {
            val parent = getParentSkeletonDrawable()

            if (parent != null) {
                descendantViews().forEach { view ->
                    val id = view.generateId()
                    parent.getProps().getBoneProps(id).apply {
                        this.color = MutableColor(boneColor ?: Color.MIN_COLOR)
                    }
                }
            } else {
                addSkeletonLoader(enabled = true)
                setSkeletonBoneColor(boneColor)
            }
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_TOGGLE_VIEW)
internal fun ViewGroup.setSkeletonBoneToggleView(toggleView: Boolean?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            descendantViews().forEach { view ->
                val id = view.generateId()
                it.getProps().getBoneProps(id).apply {
                    this.toggleView = toggleView ?: true
                }
            }
        } else {
            val parent = getParentSkeletonDrawable()

            if (parent != null) {
                descendantViews().forEach { view ->
                    val id = view.generateId()
                    parent.getProps().getBoneProps(id).apply {
                        this.toggleView = toggleView ?: true
                    }
                }
            } else {
                addSkeletonLoader(enabled = true)
                setSkeletonBoneToggleView(toggleView)
            }
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_CORNER_RADIUS)
internal fun ViewGroup.setSkeletonBoneCorners(cornerRadius: Float?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            descendantViews().forEach { view ->
                val id = view.generateId()
                it.getProps().getBoneProps(id).apply {
                    this.cornerRadii = CornerRadii(cornerRadius ?: MIN_OFFSET)
                }
            }
        } else {
            val parent = getParentSkeletonDrawable()

            if (parent != null) {
                descendantViews().forEach { view ->
                    val id = view.generateId()
                    parent.getProps().getBoneProps(id).apply {
                        this.cornerRadii = CornerRadii(cornerRadius ?: MIN_OFFSET)
                    }
                }
            } else {
                addSkeletonLoader(enabled = true)
                setSkeletonBoneCorners(cornerRadius)
            }
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_MIN_THICKNESS)
internal fun ViewGroup.setSkeletonBoneMinThickness(minThickness: Float?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            descendantViews().forEach { view ->
                val id = view.generateId()
                it.getProps().getBoneProps(id).apply {
                    this.minThickness = minThickness ?: BoneProperties.MIN_THICKNESS
                }
            }
        } else {
            val parent = getParentSkeletonDrawable()

            if (parent != null) {
                descendantViews().forEach { view ->
                    val id = view.generateId()
                    parent.getProps().getBoneProps(id).apply {
                        this.minThickness = minThickness ?: BoneProperties.MIN_THICKNESS
                    }
                }
            } else {
                addSkeletonLoader(enabled = true)
                setSkeletonBoneMinThickness(minThickness)
            }
        }
    }
}

@BindingAdapter(SkeletonBoneBindings.SKELETON_BONE_MAX_THICKNESS)
internal fun ViewGroup.setSkeletonBoneMaxThickness(maxThickness: Float?) {
    doWith(foreground) {
        if (it is SkeletonDrawable) {
            descendantViews().forEach { view ->
                val id = view.generateId()
                it.getProps().getBoneProps(id).apply {
                    this.maxThickness = maxThickness ?: BoneProperties.MAX_THICKNESS
                }
            }
        } else {
            val parent = getParentSkeletonDrawable()

            if (parent != null) {
                descendantViews().forEach { view ->
                    val id = view.generateId()
                    parent.getProps().getBoneProps(id).apply {
                        this.maxThickness = maxThickness ?: BoneProperties.MAX_THICKNESS
                    }
                }
            } else {
                addSkeletonLoader(enabled = true)
                setSkeletonBoneMaxThickness(maxThickness)
            }
        }
    }
}
