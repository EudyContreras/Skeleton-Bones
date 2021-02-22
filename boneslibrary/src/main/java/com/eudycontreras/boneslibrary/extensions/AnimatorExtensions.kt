package com.eudycontreras.boneslibrary.extensions

import android.animation.ValueAnimator
import android.view.animation.Interpolator
import androidx.core.animation.addListener
import com.eudycontreras.boneslibrary.MAX_OFFSET
import com.eudycontreras.boneslibrary.MIN_OFFSET

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since March 2020
 */

internal fun ValueAnimator?.build(
    duration: Long,
    repeatCount: Int = 0,
    repeatMode: Int = ValueAnimator.RESTART,
    interpolator: Interpolator? = null,
    onUpdate: (fraction: Float) -> Unit,
    onStart: (() -> Unit)? = null,
    onEnd: (() -> Unit)? = null
): ValueAnimator {
    if (this != null && this.isRunning) {
        this.end()
        this.cancel()
    }

    return animate(
        duration = duration,
        repeatCount = repeatCount,
        repeatMode = repeatMode,
        interpolator = interpolator,
        onUpdate = onUpdate,
        onStart = onStart,
        onEnd = onEnd
    )
}

internal fun animate(
    duration: Long,
    repeatCount: Int? = null,
    repeatMode: Int? = null,
    interpolator: Interpolator? = null,
    onUpdate: (fraction: Float) -> Unit,
    onStart: (() -> Unit)? = null,
    onEnd: (() -> Unit)? = null
): ValueAnimator {
    val animator = ValueAnimator.ofFloat(MIN_OFFSET, MAX_OFFSET)

    animator.duration = duration
    if (repeatCount != null) {
        animator.repeatCount = repeatCount
    }
    if (repeatMode != null) {
        animator.repeatMode = repeatMode
    }
    animator.interpolator = interpolator
    animator.addListener(
        onStart = {
            onStart?.invoke()
        },
        onEnd = {
            onEnd?.invoke()
        }
    )
    animator.addUpdateListener {
        val fraction = it.animatedValue as Float
        onUpdate(fraction)
    }
    animator.start()
    return animator
}