package com.eudycontreras.boneslibrary.framework.skeletons

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Outline
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.core.view.doOnLayout
import com.eudycontreras.boneslibrary.bindings.addSkeletonLoader
import com.eudycontreras.boneslibrary.common.AnimatableCallback
import com.eudycontreras.boneslibrary.common.AnimateableDrawable
import com.eudycontreras.boneslibrary.extensions.saveProps
import com.eudycontreras.boneslibrary.framework.bones.BoneDrawable
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable.Companion.create
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.tryGet

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones Copyright (C) 2020 Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 * A **SkeletonDrawable** is a GradientDrawable that can overlay any **ViewGroup**.
 * The drawable can then effectively generate "Bones" which are visual representations of each view
 * inside the ViewGroup. Each **Bone** or **BoneDrawable** is rendered exactly where the view is meant to be render
 * which makes the SkeletonDrawable give a nearly accurate and pixel-perfect representation of how
 * the ViewGroup should look once it has content. For a more complete description please look
 * at the [Skeleton-Bones](https://github.com/EudyContreras/Skeleton-Bones) library on GitHub.
 * ```
 * ```
 * A **SkeletonDrawable** is highly customizable and can be customized through its mutable properties.
 * The customizable properties of a SkeletonDrawable can be found inside its instance of the SkeletonProperties
 * class. The SkeletonProperties can be access through a function called to getProps(). For a comprehensive list
 * of properties used for customizing the drawable please look at the **SkeletonProperties** class. Link found below!
 * The **SkeletonProperties** class grants access to the global and individual properties of each **Bone** and **ShimmerRay**
 * within the **SkeletonDrawable**
 *
 * ```
 * ```
 * A **SkeletonDrawable** is ideally created and used through data-binding using property accessors, but
 * the drawable can nonetheless also be created in-code in the following ways:
 *
 * **Example:**
 *
 * ```
 *      val viewGroup: ViewGroup = getContainer()
 *
 *      val drawable = viewGroup.addSkeletonLoader().apply {
 *          this.enabled = false
 *          this.skeletonColor = Colors.WHITE
 *      }
 *
 *      val drawable = SkeletonDrawable.create(viewGroup).apply {
 *          this.enabled = false
 *          this.skeletonColor = Colors.WHITE
 *      }
 * ```
 * Using a **SkeletonDrawable** Instead of a wrapper provides the following benefits:
 * - Lightweight and easy to use
 * - Non-invasive, No changes to existing code is required.
 * - No layout nor view wrapping which creates complex hierarchies
 * - Nearly pixel perfect skeleton bone placing
 * - Disposed when no longer needed
 * - No boilerplate
 * - Uses data binding
 * - Highly performing
 * - Highly customizable
 * - Code-free use
 *
 * @see ViewGroup
 * @see BoneDrawable
 * @see SkeletonProperties
 * @see GradientDrawable
 * @see getProps
 * @see addSkeletonLoader
 * @see create
 *
 * @constructor Privately used to create an instance of the **SkeletonDrawable**. Please use
 * the static create function for creating a **SkeletonDrawable**
 */

class SkeletonDrawable internal constructor(
    private val skeletonManager: SkeletonManager
) : GradientDrawable(), AnimateableDrawable, AnimatableCallback {

    init {
        skeletonManager.setDrawable(this)

        skeletonManager.addUpdateListener {
            invalidateSelf()

            if (skeletonManager.properties.waiting) {
                skeletonManager.properties.enabled = false
            }
        }

        skeletonManager.addAnimationListener(
            onStart = {
                listeners.forEach { it.onAnimationStart(this) }
            },
            onEnd = {
                listeners.forEach { it.onAnimationEnd(this) }
            }
        )

        skeletonManager.onDiscarded {
            owner?.foreground = baseDrawableForeground
            if (skeletonManager.properties.allowWeakSavedState) {
                owner?.saveProps(SkeletonProperties.TAG, skeletonManager.properties, true)
            } else if (skeletonManager.properties.allowSavedState) {
                owner?.saveProps(SkeletonProperties.TAG, skeletonManager.properties)
            }
            listeners.clear()
        }
    }

    private val listeners: MutableList<AnimationCallback> = mutableListOf()

    private val self: GradientDrawable
        get() = this

    @Volatile
    internal var baseDrawableForeground: Drawable? = null

    @Volatile
    internal var baseDrawableBackground: Drawable? = null
        set(value) {
            if (value != field) {
                field = value
                value?.let {
                    if (VERSION.SDK_INT >= VERSION_CODES.N) {
                        initializeProperties(it)
                    }
                }
            }
        }

    @Volatile
    internal var owner: ViewGroup? = null
        set(value) {
            if (field == value)
                return
            owner?.outlineProvider = ViewOutlineProvider.BACKGROUND
            field = value
        }

    @Volatile
    internal var enabled: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                owner?.let { viewGroup ->
                    viewGroup.doOnLayout {
                        skeletonManager.getSkeleton().compute(field, viewGroup) {
                            skeletonManager.showSkeleton(field)
                            invalidateSelf()
                        }
                    }
                }
            }
        }

    @RequiresApi(VERSION_CODES.N)
    private fun initializeProperties(drawable: Drawable) {
        when (drawable) {
            is GradientDrawable -> {
                (self.mutate() as GradientDrawable).apply {
                    val corners = tryGet { drawable.cornerRadii?.let { CornerRadii(it) } } ?: CornerRadii(drawable.cornerRadius)
                    this.shape = drawable.shape
                    this.alpha = drawable.alpha
                    this.color = drawable.color
                    this.colors = drawable.colors
                    this.cornerRadii = corners.corners
                    this.gradientRadius = drawable.gradientRadius
                    this.gradientType = drawable.gradientType
                    if (skeletonManager.properties.skeletonCornerRadii == null) {
                        skeletonManager.getSkeleton().corners.apply(corners)
                    }
                    invalidateSelf()
                }
            }
        }
    }

    /**
     * Retrieves the properties used by this **SkeletonDrawable**
     */
    @Synchronized
    fun getProps(): SkeletonProperties = skeletonManager.properties

    /**
     * Sets the properties use by this **SkeletonDrawable**
     * @param properties The properties to be set on this drawable
     */
    @Synchronized
    fun setProps(properties: SkeletonProperties) {
        skeletonManager.properties = properties.clone()
        owner?.let {
            if (it.isLaidOut) {
                skeletonManager.getSkeleton().compute(enabled, it) {
                    skeletonManager.showSkeleton(enabled)
                    invalidateSelf()
                }
            }
        }
    }

    /**
     * Retrieves the property builder which can be used for building
     * this **SkeletonDrawable**
     */
    @Synchronized
    fun build(): SkeletonBuilder = skeletonManager.getBuilder()

    override fun draw(canvas: Canvas) {
        skeletonManager.renderer.render(canvas)
    }

    @Synchronized
    override fun setAlpha(alpha: Int) { }

    @Synchronized
    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    @Synchronized
    override fun setColorFilter(colorFilter: ColorFilter?) {}

    @Synchronized
    override fun isAnimationRunning(): Boolean {
        return skeletonManager.isAnimating
    }

    @Synchronized
    override fun startAnimation() {
        skeletonManager.startAnimation()
    }

    @Synchronized
    override fun endAnimation() {
        skeletonManager.stopAnimation()
    }

    @Synchronized
    override fun registerAnimationCallback(callback: AnimationCallback) {
        this.listeners.add(callback)
    }

    @Synchronized
    override fun unregisterAnimationCallback(callback: AnimationCallback): Boolean {
        return this.listeners.remove(callback)
    }

    @Synchronized
    override fun clearAnimationCallbacks() {
        this.listeners.clear()
    }

    @RequiresApi(VERSION_CODES.N)
    override fun getOutline(outline: Outline) {
        when (shape) {
            RECTANGLE -> {
                if (baseDrawableBackground != null && baseDrawableBackground is GradientDrawable) {
                    baseDrawableBackground?.getOutline(outline)
                } else {
                    owner?.let {
                        outline.setConvexPath(skeletonManager.renderer.getPath())
                    }
                }
            }
            OVAL -> {
                outline.setOval(bounds)
            }
            else -> {}
        }
    }

    companion object {
        /**
         * Creates an instance of a SkeletonDrawable. The drawable is directly
         * attached to the ViewGroup passed in the constructor. The SkeletonDrawable can
         * be manipulated by changing its properties. The properties can be accessed
         * through getProps()
         */
        @JvmStatic
        fun create(viewGroup: ViewGroup, enabled: Boolean = true): SkeletonDrawable {
            return viewGroup.addSkeletonLoader(enabled)
        }
    }
}
