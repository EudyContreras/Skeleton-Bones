package com.eudycontreras.boneslibrary.framework.bones

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Outline
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.core.view.doOnLayout
import com.eudycontreras.boneslibrary.bindings.addBoneLoader
import com.eudycontreras.boneslibrary.common.AnimatableCallback
import com.eudycontreras.boneslibrary.common.AnimateableDrawable
import com.eudycontreras.boneslibrary.common.Reusable
import com.eudycontreras.boneslibrary.extensions.saveProps
import com.eudycontreras.boneslibrary.framework.bones.BoneDrawable.Companion.create
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRay
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.ShapeType
import com.eudycontreras.boneslibrary.tryGet

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones Copyright (C) 2020 Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 * A **BoneDrawable** is a GradientDrawable that can overlay any **View*.
 * The drawable can then effectively generate respective "Shimmer Ray" effects that
 * overlays the target View. For a more details please look at the
 * [Skeleton-Bones](https://github.com/EudyContreras/Skeleton-Bones) library on GitHub.
 * ```
 * ```
 * A **BoneDrawable** is highly customizable and can be customized through its mutable properties.
 * The customizable properties of a BoneDrawable can be found inside its instance of the **BoneProperties**
 * class. The BoneProperties can be access through a function called to getProps(). For a comprehensive list
 * of properties used for customizing the drawable please look at the **BoneProperties** class. Link found below!
 *
 * ```
 * ```
 * A **BoneDrawable** is ideally created and used through data-binding using property accessors, but
 * the drawable can nonetheless also be created in-code in the following ways:
 *
 * **Example:**
 *
 * ```
 *      val view: View = getView()
 *
 *      val drawable = view.addBoneLoader().apply {
 *          this.enabled = false
 *          this.color = Colors.RED
 *      }
 *
 *      val drawable = BoneDrawable.create(view).apply {
 *          this.enabled = false
 *          this.color = Colors.RED
 *      }
 * ```
 * Using a **BoneDrawable** Instead of a wrapper provides the following benefits:
 * - Lightweight and easy to use
 * - Non-invasive, No changes to existing code is required.
 * - No layout nor view wrapping which creates complex hierarchies
 * - Disposed when no longer needed
 * - No boilerplate
 * - Uses data binding
 * - Highly performing
 * - Highly customizable
 * - Code-free use
 *
 *
 * @see View
 * @see BoneDrawable
 * @see BoneProperties
 * @see ShimmerRay
 * @see GradientDrawable
 * @see addBoneLoader
 * @see getProps
 * @see create
 *
 * @constructor Privately used to create an instance of the **BoneDrawable**. Please use
 * the static create function for creating a **BoneDrawable**
 *
 */

class BoneDrawable internal constructor(
    internal var boneManager: BoneManager
) : GradientDrawable(), AnimateableDrawable, AnimatableCallback, Reusable {

    init {
        initialize()
    }

    private var listeners: MutableList<AnimationCallback>? = null

    internal var background: Drawable? = null

    internal var baseForeground: Drawable? = null
        set(value) {
            field = value
            boneManager.foreground = value
        }

    internal var baseBackground: Drawable? = null
        set(value) {
            if (value != field) {
                field = value
                field?.let {
                    if (VERSION.SDK_INT >= VERSION_CODES.N) {
                        initializeProperties(it)
                    }
                }
            }
        }

    internal var owner: View? = null
        set(value) {
            value?.let { view ->
                if (view.isLaidOut) {
                    boneManager.getBone().compute(view)
                    invalidateSelf()
                } else {
                    view.doOnLayout {
                        boneManager.getBone().compute(it)
                        invalidateSelf()
                    }
                }
            }
            field = value
        }

    internal var enabled: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                owner?.let {
                    if (it.isLaidOut) {
                        boneManager.showBone(field)
                    } else {
                        it.doOnLayout {
                            boneManager.showBone(field)
                        }
                    }
                }
            }
        }

    private fun initialize() {
        boneManager.setDrawable(this)

        boneManager.addUpdateListener {
            invalidateSelf()
        }

        boneManager.addAnimationListener(
            onStart = {
                listeners?.forEach { it.onAnimationStart(this) }
            },
            onEnd = {
                listeners?.forEach { it.onAnimationEnd(this) }
            }
        )

        boneManager.onDiscarded {
            owner?.foreground = baseForeground
            if (boneManager.properties.allowWeakSavedState) {
                owner?.saveProps(BoneProperties.TAG, boneManager.properties, true)
            } else if (boneManager.properties.allowSavedState) {
                owner?.saveProps(BoneProperties.TAG, boneManager.properties)
            }
            listeners?.clear()
        }
    }

    @RequiresApi(VERSION_CODES.N)
    private fun initializeProperties(drawable: Drawable) {
        owner?.let {
            it.outlineProvider = ViewOutlineProvider.BACKGROUND
        }
        when (drawable) {
            is GradientDrawable -> {
                (this).apply {
                    val corners = tryGet { drawable.cornerRadii?.let { CornerRadii(it) } } ?: CornerRadii(drawable.cornerRadius)
                    this.shape = drawable.shape
                    this.alpha = drawable.alpha
                    this.color = drawable.color
                    this.colors = drawable.colors
                    this.cornerRadii = corners.corners
                    this.cornerRadius = drawable.cornerRadius
                    this.gradientRadius = drawable.gradientRadius
                    this.gradientType = drawable.gradientType
                    this.boneManager.background = drawable
                    if (this.boneManager.properties.cornerRadii == null) {
                        this.boneManager.properties.cornerRadii = corners
                    }
                    if (this.boneManager.properties.shapeType == null) {
                        this.boneManager.properties.shapeType = when (drawable.shape) {
                            RECTANGLE -> ShapeType.RECTANGULAR
                            else -> ShapeType.CIRCULAR
                        }
                    }
                }
            }
        }
    }

    /**
     * Retrieves the properties used by this **BoneDrawable**
     */
    @Synchronized
    fun getProps(): BoneProperties = boneManager.properties

    /**
     * Sets the properties use by this **BoneDrawable**
     * @param properties The properties to be set on this drawable
     */
    @Synchronized
    fun setProps(properties: BoneProperties) {
        boneManager.properties = properties.clone()
        owner?.let {
            if (it.isLaidOut) {
                boneManager.getBone().compute(it)
            }
        }
    }

    /**
     * Retrieves the property builder which can be used for building
     * this **BoneDrawable**
     */
    @Synchronized
    fun build(): BoneBuilder = boneManager.getBuilder()

    override fun resetForReuse() {
        owner = null
        enabled = false
        baseBackground = null
        baseForeground = null
        boneManager.resetForReuse()
        initialize()
    }

    override fun draw(canvas: Canvas) {
        baseForeground?.draw(canvas)
        boneManager.renderer.render(canvas)
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
        return boneManager.isAnimating
    }

    @Synchronized
    override fun startAnimation() {
        boneManager.startAnimation()
    }

    @Synchronized
    override fun endAnimation() {
        boneManager.stopAnimation()
    }

    @Synchronized
    override fun registerAnimationCallback(callback: AnimationCallback) {
        if (this.listeners == null) {
            this.listeners = mutableListOf()
        }
        this.listeners?.add(callback)
    }

    @Synchronized
    override fun unregisterAnimationCallback(callback: AnimationCallback): Boolean {
        return this.listeners?.remove(callback) ?: false
    }

    @Synchronized
    override fun clearAnimationCallbacks() {
        this.listeners?.clear()
    }

    @RequiresApi(VERSION_CODES.N)
    override fun getOutline(outline: Outline) {
        when (shape) {
            RECTANGLE -> {
                if (baseBackground != null && baseBackground is GradientDrawable) {
                    baseBackground?.getOutline(outline)
                } else {
                    owner?.let {
                        outline.setConvexPath(boneManager.renderer.getPath())
                    }
                }
            }
            OVAL -> {
                if (baseBackground != null && baseBackground is GradientDrawable) {
                    baseBackground?.getOutline(outline)
                } else {
                    outline.setOval(bounds)
                }
            }
            else -> {}
        }
    }

    companion object {

        /**
         * Creates an instance of a BoneDrawable. The drawable is directly
         * attached to the view passed in the constructor. The BoneDrawable can
         * be manipulated by changing its properties. The properties can be accessed
         * through getProps()
         *
         * @param view The parent View of this drawable loader which will become
         * a skeleton loader
         * @param enabled When true the loading animation will play right away
         *  @param builder The builder used for building the drawable loader
         */
        @JvmStatic
        fun create(view: View, enabled: Boolean = true, builder: BoneBuilder): BoneDrawable {
            return view.addBoneLoader(enabled, builder.boneProperties)
        }

        /**
         * Creates an instance of a BoneDrawable. The drawable is directly
         * attached to the view passed in the constructor. The BoneDrawable can
         * be manipulated by changing its properties. The properties can be accessed
         * through getProps()
         *
         * @param view The parent View of this drawable loader which will become
         * a skeleton loader
         * @param enabled When true the loading animation will play right away
         *  @param properties The properties used for building the drawable loader
         */
        @JvmStatic
        fun create(view: View, enabled: Boolean = true, properties: BoneProperties = BoneProperties()): BoneDrawable {
            return view.addBoneLoader(enabled, properties)
        }

        /**
         * Returns a builder which can be used for building bone drawable loaders
         */
        @JvmStatic
        fun builder(defaultProps: BoneProperties): BoneBuilder {
            return BoneBuilder(defaultProps)
        }
    }
}
