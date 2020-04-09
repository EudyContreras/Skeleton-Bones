package com.eudycontreras.boneslibrary.framework.bones

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.LinearInterpolator
import com.eudycontreras.boneslibrary.Action
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.MIN_OFFSET
import com.eudycontreras.boneslibrary.common.Disposable
import com.eudycontreras.boneslibrary.extensions.animate
import com.eudycontreras.boneslibrary.extensions.build
import com.eudycontreras.boneslibrary.properties.Color

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 */

internal class BoneManager(
    private val owner: View,
    internal var foreground: Drawable?,
    internal var background: Drawable?,
    internal var properties: BoneProperties = BoneProperties()
) : Disposable {

    private lateinit var drawable: BoneDrawable

    private var fadeAnimator = ValueAnimator.ofFloat(MIN_OFFSET, MAX_OFFSET)

    private var boneAnimator = ValueAnimator.ofFloat(MIN_OFFSET, MAX_OFFSET)

    private val updateListeners: MutableList<(Float) -> Unit> = mutableListOf()

    private val startListeners: MutableList<Action> = mutableListOf()

    private val endListeners: MutableList<Action> = mutableListOf()

    private var discardedListener: (() -> Unit)? = null

    private var discarded: Boolean = false

    private val bone: Bone = Bone.build(owner, properties, this)

    private val builder: BoneBuilder = BoneBuilder(properties)

    val renderer: BoneRenderer = BoneRenderer(bone)

    val isDiscarded: Boolean
        get() = discarded

    val isAnimating: Boolean
        get() = boneAnimator?.isRunning ?: false

    var allowShadows: Boolean = false

    fun setDrawable(boneDrawable: BoneDrawable) {
        this.drawable = boneDrawable
        this.properties.enabledListener = {
            this.drawable.enabled = it
        }
        this.properties.enabledProvider = {
            this.drawable.enabled
        }
    }

    fun showBone(show: Boolean) {
        if (show) {
            if (foreground != null) {
                owner.background = foreground
            }
            this.renderer.shouldRender = true
            startAnimation()
        } else {
            if (properties.transitionDuration > 0L) {
                owner.background = background
                animateFadeOut()
            } else {
                this.renderer.shouldRender = false
                stopAnimation()
                dispose()
            }
        }
    }

    override fun dispose() {
        this.stopAnimation()
        this.discarded = true
        this.bone.dispose()
        this.updateListeners.clear()
        this.startListeners.clear()
        this.endListeners.clear()
        this.discardedListener?.invoke()
    }

    fun getBuilder(): BoneBuilder {
        return builder
    }

    fun getBone(): Bone {
        return bone
    }

    fun onDiscarded(listener: () -> Unit) {
        this.discardedListener = listener
    }

    fun addUpdateListener(listener: (fraction: Float) -> Unit) {
        updateListeners.add(listener)
    }

    fun addAnimationListener(onStart: (() -> Unit)? = null, onEnd: (() -> Unit)? = null) {
        onStart?.let { startListeners.add(it) }
        onEnd?.let { endListeners.add(it) }
    }

    fun removeUpdateListener(listener: (fraction: Float) -> Unit) {
        updateListeners.remove(listener)
    }

    fun dispose(bone: Bone) {
        val duration = properties.transitionDuration
        animate(
            duration = duration,
            onUpdate = {
                bone.onFade(it)
                bone.fadeContent(it)
            },
            onStart = {
                bone.prepareContentFade()
            },
            onEnd = {
                properties.isDisposed = true
            }
        )
    }

    private fun animateFadeOut() {
        val duration = properties.transitionDuration
        fadeAnimator = fadeAnimator.build(
            duration = duration,
            onUpdate = {
                foreground?.alpha =  Color.MAX_COLOR - (it * Color.MAX_COLOR).toInt()
                renderer.fade(it)
            },
            onStart = {
                foreground?.alpha = Color.MAX_COLOR
            },
            onEnd = {
                stopAnimation()
                dispose()
            }
        )
    }

    fun startAnimation() {
        val properties = properties.shimmerRayProperties

        properties?.let {
            val multiplier = properties.shimmerRaySpeedMultiplier
            val duration = properties.shimmerRayAnimDuration

            val interpolator = if (properties.shimmerRaySharedInterpolator) {
                properties.shimmerRayInterpolator
            } else LinearInterpolator()

            boneAnimator = boneAnimator.build(
                duration = (duration * multiplier).toLong(),
                interpolator = interpolator,
                repeatCount = ValueAnimator.INFINITE,
                repeatMode = ValueAnimator.RESTART,
                onUpdate = {
                    renderer.update(it)

                    updateListeners.forEach { listener ->
                        listener.invoke(it)
                    }
                },
                onStart = {
                    foreground?.alpha = Color.MAX_COLOR
                    startListeners.forEach { it.invoke() }
                },
                onEnd = {
                    endListeners.forEach { it.invoke() }
                }
            )
        }
    }

    fun stopAnimation() {
        this.renderer.shouldRender = false
        if (boneAnimator != null) {
            boneAnimator.end()
            boneAnimator.cancel()
        }
        boneAnimator = null
    }
}
