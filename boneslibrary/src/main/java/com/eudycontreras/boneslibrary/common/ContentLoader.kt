package com.eudycontreras.boneslibrary.common

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since March 2020
 */

internal interface ContentLoader {
    fun fadeContent(fraction: Float)
    fun prepareContentFade()
    fun concealContent()
    fun revealContent()
    fun restore()
}