package com.eudycontreras.boneslibrary.framework.skeletons

import android.view.View
import com.eudycontreras.boneslibrary.bindings.SkeletonBindings
import com.eudycontreras.boneslibrary.common.Cloneable
import com.eudycontreras.boneslibrary.common.Reusable
import com.eudycontreras.boneslibrary.extensions.generateId
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType
import com.eudycontreras.boneslibrary.tryGet
import com.eudycontreras.boneslibrary.utilities.Shadow

/**
 * Copyright (C) 2020 Project Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 * Class which holds all the information used for generating and building a complete skeleton
 * hierarchy.
 *
 * For a more thorough description about **Skeletons** please visit the link:
 * [Skeleton-Bones](https://github.com/EudyContreras/Skeleton-Bones) library on GitHub.
 */

class SkeletonProperties : Cloneable<SkeletonProperties>, Reusable {

    private var boneProperties: MutableMap<Int, BoneProperties> = mutableMapOf()

    private var ignoredIds: HashSet<Int> = HashSet()

    private var disposedIds: HashSet<Int> = HashSet()

    private var stateOwners: HashMap<Int, Boolean> = HashMap()

    private val defaultBoneProperties: BoneProperties = BoneProperties()

    @Volatile
    internal var waiting: Boolean = false

    @Volatile
    internal var enabledListener: ((enabled: Boolean) -> Unit)? = null

    @Volatile
    internal var enabledProvider: (() -> Boolean)? = null

    @Volatile
    var allowShadows: Boolean = true

    @Volatile
    var allowBoneGeneration: Boolean = true

    @Volatile
    var shadowColor: MutableColor = Shadow.COLOR

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
    var shimmerRayProperties: ShimmerRayProperties = ShimmerRayProperties()

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
     * @see SkeletonBindings.SKELETON_TRANSITION_DURATION
     */
    @Volatile
    var stateTransitionDuration: Long = DEFAULT_TRANSITION_DURATION

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
     * @see SkeletonBindings.SKELETON_USE_TRANSITION
     */
    @Volatile
    var useStateTransition: Boolean = true

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
     * @see SkeletonBone
     * @see ShapeType
     * @see CornerRadii
     * @see SkeletonBindings.SKELETON_BACKGROUND_COLOR
     */
    @Volatile
    var skeletonBackgroundColor: MutableColor? = null

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
     * @see SkeletonBone
     * @see CornerRadii
     * @see MutableColor
     * @see SkeletonBindings.SKELETON_CORNER_RADIUS
     */
    @Volatile
    var skeletonCornerRadii: CornerRadii? = null

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
     * @see SkeletonBindings.SKELETON_SAVE_STATE
     */
    @Volatile
    var allowSavedState: Boolean = false

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
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
     * @see SkeletonBindings.SKELETON_SAVE_WEAK_STATE
     */
    @Volatile
    var allowWeakSavedState: Boolean = false

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
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
     * @see allowSavedState
     * @see SkeletonBindings.SKELETON_ANIMATE_RESTORED_BOUNDS
     */
    @Volatile
    var animateRestoredBounds: Boolean = false

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
     * @see SkeletonBindings.SKELETON_ENABLED
     */
    var enabled: Boolean
        get() = enabledProvider?.invoke() ?: false
        set(value) {
            if (value) {
                if (value != enabledProvider?.invoke()) {
                    enabledListener?.invoke(value)
                }
            } else {
                waiting = if (stateOwners.size <= 0) {
                    enabledListener?.invoke(value)
                    false
                } else {
                    true
                }
            }
        }

    override fun resetForReuse() {
        waiting = false
        disposedIds.clear()
        stateOwners.clear()
        boneProperties.clear()
        defaultBoneProperties.resetForReuse()
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Retrieves the **BoneProperties** for the bone with
     * the given ownerId.
     *
     * @param ownerId The id of the view this bone is meant to represent.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun getBoneProps(ownerId: Int): BoneProperties {
        return boneProperties[ownerId] ?: defaultBoneProperties.clone()
            .apply {
                boneProperties[ownerId] = this
            }
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the **BoneProperties** for the bone with
     * the given ownerId.
     *
     * @param ownerId The id of the view this bone is meant to represent.
     *
     * @see BoneProperties
     */
    @Synchronized
    @Deprecated(
        message = "this is spelled wrong and will be removed",
        replaceWith = ReplaceWith("setBoneProps")
    )
    fun settBoneProps(ownerId: Int, boneProps: BoneProperties) {
        boneProperties[ownerId] = boneProps
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the **BoneProperties** for the bone with
     * the given ownerId.
     *
     * @param ownerId The id of the view this bone is meant to represent.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun setBoneProps(ownerId: Int, boneProps: BoneProperties) {
        boneProperties[ownerId] = boneProps
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Sets the **BoneProperties** for the bone with
     * the given ownerId.
     *
     * @param view The view this bone is meant to represent.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun setBoneProps(view: View, boneProps: BoneProperties) {
        setBoneProps(view.generateId(), boneProps)
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Returns true when the view with the given id is currently
     * being ignored during **Bone** generation.
     *
     * @param ownerId The id of the view which is meant to be a **Bone's** owner.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun isIgnored(ownerId: Int): Boolean {
        return ignoredIds.contains(ownerId)
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Returns true when the view with the given id is currently
     * being ignored during **Bone** generation.
     *
     * @param ownerId The id of the view which is meant to be a **Bone's** owner.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun isIgnored(view: View): Boolean {
        return isIgnored(view.generateId())
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Returns true when the view with the given id has been disposed
     *
     * @param ownerId The id of the view which is meant to be a **Bone's** owner.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun isDisposed(ownerId: Int): Boolean {
        return disposedIds.contains(ownerId)
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Returns true when the view with the given id has been disposed
     *
     * @param view The view which is meant to be a **Bone's** owner.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun isDisposed(view: View): Boolean {
        return isDisposed(view.generateId())
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Adds the given id to list of ignored ids.
     * This will prevent the generation of the bone meant to represent the owner
     * view.
     *
     * @param ownerId The id of the view which is meant to be a **Bone's** owner.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun addIgnored(vararg ownerId: Int) {
        ignoredIds.addAll(ownerId.asList())
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Adds the given id to list of ignored ids.
     * This will prevent the generation of the bone meant to represent the owner
     * view.
     *
     * @param view The view which is meant to be a **Bone's** owner.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun addIgnored(vararg view: View) {
        ignoredIds.addAll(view.map { it.generateId() })
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Adds the given id to list of disposed ids.
     *
     * @param ownerId The id of the view which is meant to be a **Bone's** owner.
     *
     * @see BoneProperties
     */
    @Synchronized
    internal fun addDisposed(ownerId: Int) {
        disposedIds.add(ownerId)
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Removes the given id from the list of ignored ids.
     * This makes the generation of the bone no longer ignored during bone generation
     *
     * @param ownerId The id of the view which is meant to be a **Bone's** owner.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun removeIgnored(ownerId: Int) {
        ignoredIds.remove(ownerId)
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Returns the state state of the state owner
     *
     * @param ownerId The id of the state owner view
     *
     * @see BoneProperties
     */
    @Synchronized
    fun getStateOwnerState(ownerId: Int): Boolean {
        return tryGet {
            stateOwners[ownerId] ?: false
        } ?: false
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Returns the state state of the state owner
     *
     * @param ownerId The id of the state owner view
     *
     * @see BoneProperties
     */
    @Synchronized
    fun getStateOwnerState(view: View): Boolean {
        return getStateOwnerState(view.generateId())
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Returns true when the view with the given id is currently
     * being a state owner.
     *
     * @param ownerId The id of the state owner view
     *
     * @see BoneProperties
     */
    @Synchronized
    fun hasStateOwner(ownerId: Int): Boolean {
        return stateOwners.containsKey(ownerId)
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Returns true when the view with the given id is currently
     * being a state owner.
     *
     * @param view The id of the state owner view
     *
     * @see BoneProperties
     */
    @Synchronized
    fun hasStateOwner(view: View): Boolean {
        return hasStateOwner(view.generateId())
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * If true, Adds the given id and the state to the map of state owner.
     * When false it will remove the state owner since there in no point of
     * keeping a loaded owner's state. A state owner will own the state of those
     * bones or skeletons belonging to it. If a state owner state changes it will
     * also change the state of the views it owns.
     *
     * @param ownerId The id of the state owner view.
     * @param state The state of the state owner
     *
     * @see BoneProperties
     */
    @Synchronized
    fun setStateOwner(ownerId: Int, state: Boolean) {
        if (state) {
            stateOwners[ownerId] = state
        } else {
            stateOwners.remove(ownerId)
        }
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * If true, Adds the given id and the state to the map of state owner.
     * When false it will remove the state owner since there in no point of
     * keeping a loaded owner's state. A state owner will own the state of those
     * bones or skeletons belonging to it. If a state owner state changes it will
     * also change the state of the views it owns.
     *
     * @param view The state owner view.
     * @param state The state of the state owner
     *
     * @see BoneProperties
     */
    @Synchronized
    fun setStateOwner(view: View, state: Boolean) {
        setStateOwner(view.generateId(), state)
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Removes the given id from the mpa of state owners.
     *
     * @param ownerId The id of the state owner view.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun removeStateOwner(ownerId: Int) {
        stateOwners.remove(ownerId)
    }


    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Removes the given id from the mpa of state owners.
     *
     * @param view The state owner view.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun removeStateOwner(view: View) {
        removeStateOwner(view.generateId())
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Clears the list of ignored ids. All ignored bones will then be rendered.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun clearIgnored() {
        ignoredIds.clear()
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Clears the list of state owner ids.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun clearStateOwners() {
        stateOwners.clear()
    }

    /**
     * @Project Project Bones
     * @author Eudy Contreras
     * @since March 2020
     *
     * Clears the list of disposed ids.
     *
     * @see BoneProperties
     */
    @Synchronized
    fun clearDisposedIds() {
        disposedIds.clear()
    }

    @Synchronized
    internal fun removeBoneProps(ownerId: Int) {
        boneProperties.remove(ownerId)
    }

    @Synchronized
    internal fun getBoneProperties(ownerId: Int): BoneProperties {
        return getBoneProps(ownerId)
    }

    @Synchronized
    internal fun getBoneProps(): BoneProperties {
        return defaultBoneProperties
    }

    override fun clone(): SkeletonProperties {
        return SkeletonProperties().also {
            it.waiting = this.waiting
            it.allowShadows = this.allowShadows
            it.shadowColor = this.shadowColor
            it.ignoredIds = HashSet(this.ignoredIds)
            it.allowSavedState = this.allowSavedState
            it.allowWeakSavedState = this.allowWeakSavedState
            it.allowBoneGeneration = this.allowBoneGeneration
            it.useStateTransition = this.useStateTransition
            it.animateRestoredBounds = this.animateRestoredBounds
            it.skeletonBackgroundColor = this.skeletonBackgroundColor
            it.skeletonCornerRadii = this.skeletonCornerRadii?.copy()
            it.shimmerRayProperties = this.shimmerRayProperties.clone()
            it.stateTransitionDuration = this.stateTransitionDuration
            it.boneProperties = HashMap(this.boneProperties.mapValues { entry -> entry.value.clone() })
        }
    }

    internal companion object {
        const val DEFAULT_TRANSITION_DURATION = 250L

        val TAG: Int = SkeletonProperties::class.java.simpleName.hashCode()
    }
}
