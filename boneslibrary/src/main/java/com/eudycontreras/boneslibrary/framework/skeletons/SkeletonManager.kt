package com.eudycontreras.boneslibrary.framework.skeletons

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import com.eudycontreras.boneslibrary.Action
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.MIN_OFFSET
import com.eudycontreras.boneslibrary.common.Disposable
import com.eudycontreras.boneslibrary.extensions.animate
import com.eudycontreras.boneslibrary.extensions.build

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 */

internal class SkeletonManager(
    var properties: SkeletonProperties = SkeletonProperties()
): Disposable {

    private lateinit var drawable: SkeletonDrawable

    private var fadeAnimator = ValueAnimator.ofFloat(MIN_OFFSET, MAX_OFFSET)

    private var skeletonAnimator = ValueAnimator.ofFloat(MIN_OFFSET, MAX_OFFSET)

    private val updateListeners: MutableList<(Float) -> Unit> = mutableListOf()

    private val startListeners: MutableList<Action> = mutableListOf()

    private val endListeners: MutableList<Action> = mutableListOf()

    private val skeleton: Skeleton = Skeleton(this, properties)

    private val builder: SkeletonBuilder = SkeletonBuilder(properties)

    private var discardedListener: (() -> Unit)? = null

    private var discarded: Boolean = false

    val isDiscarded: Boolean
        get() = discarded

    val renderer: SkeletonRenderer =
        SkeletonRenderer(
            skeleton
        )

    val isAnimating: Boolean
        get() = skeletonAnimator?.isRunning ?: false


    fun setDrawable(skeletonDrawable: SkeletonDrawable) {
        this.drawable = skeletonDrawable
        this.properties.enabledListener = {
            this.drawable.enabled = it
        }
        this.properties.enabledProvider = {
            this.drawable.enabled
        }
    }

    @Synchronized
    fun showSkeleton(show: Boolean, useTransition: Boolean = properties.useStateTransition) {
        if (show) {
            this.renderer.shouldRender = true
            this.skeleton.concealContent()
            startAnimation()
        } else {
            if (useTransition) {
                animateFadeOut()
            } else {
                this.renderer.shouldRender = false
                this.skeleton.revealContent()
                stopAnimation()
                dispose()
            }
        }
    }

    @Synchronized
    override fun dispose() {
        this.discarded = true
        this.skeleton.dispose()
        this.updateListeners.clear()
        this.startListeners.clear()
        this.endListeners.clear()
        this.discardedListener?.invoke()
    }

    fun getBuilder(): SkeletonBuilder {
        return builder
    }

    fun getSkeleton(): Skeleton {
        return skeleton
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

    fun recompute() {
        skeleton.isDirty = true
    }

    fun loaded(bone: SkeletonBone) {
        properties.getBoneProps(bone.id).isLoaded = true
    }

    fun dispose(bone: SkeletonBone) {
        val duration = properties.stateTransitionDuration
        animate(
            duration = duration,
            onUpdate = {
                bone.onFade(it)
                if (bone.boneProperties.toggleView) {
                    bone.fadeContent(it)
                }
            },
            onStart = {
                if (bone.boneProperties.toggleView) {
                    bone.prepareContentFade()
                }
                bone.restore()
            },
            onEnd = {
                properties.getBoneProps(bone.id).isDisposed = true
                properties.removeStateOwner(bone.id)
                properties.addDisposed(bone.id)
            }
        )
    }

    private fun animateFadeOut() {
        val duration = properties.stateTransitionDuration
        fadeAnimator = fadeAnimator.build(
            duration = duration,
            onUpdate = {
                renderer.fade(it)
            },
            onStart = {
                skeleton.prepareContentFade()
                skeleton.restore()
            },
            onEnd = {
                stopAnimation()
                dispose()
            }
        )
    }

    fun startAnimation() {
        val properties = properties.shimmerRayProperties
        val multiplier = properties.shimmerRaySpeedMultiplier
        val duration = properties.shimmerRayAnimDuration

        val interpolator = if (properties.shimmerRaySharedInterpolator) {
            properties.shimmerRayInterpolator
        } else LinearInterpolator()

        skeletonAnimator = skeletonAnimator.build(
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
                startListeners.forEach { it.invoke() }
            },
            onEnd = {
                endListeners.forEach { it.invoke() }
            }
        )
    }

    fun stopAnimation() {
        if (skeletonAnimator != null) {
            skeletonAnimator.end()
            skeletonAnimator.cancel()
        }
        renderer.shouldRender = false
        skeletonAnimator = null
    }
}
