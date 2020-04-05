package com.eudycontreras.boneslibrary.common

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 */

@FunctionalInterface
internal interface FadeTarget {
    fun onFade(fraction: Float)
}
