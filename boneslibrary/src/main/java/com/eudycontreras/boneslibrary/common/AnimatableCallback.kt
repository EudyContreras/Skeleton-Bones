package com.eudycontreras.boneslibrary.common

import android.graphics.drawable.Animatable2.AnimationCallback

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 */

interface AnimatableCallback {
    /**
     * Registers an animation callback
     */
    fun registerAnimationCallback(callback: AnimationCallback)

    /**
     * Unregisters an animation callback
     */
    fun unregisterAnimationCallback(callback: AnimationCallback): Boolean

    /**
     * Clears all registered animation callbacks
     */
    fun clearAnimationCallbacks()
}
