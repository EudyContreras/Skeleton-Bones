package com.eudycontreras.boneslibrary.common

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since March 2020
 */

interface AnimateableDrawable {
    /**
     * Starts an animation assigned to this drawable
     */
    fun startAnimation()

    /**
     * Ends all running animations assigned to this drawable
     */
    fun endAnimation()

    /**
     * Returns true if any animation assigned to this drawable
     * is currently running.
     */
    fun isAnimationRunning(): Boolean
}