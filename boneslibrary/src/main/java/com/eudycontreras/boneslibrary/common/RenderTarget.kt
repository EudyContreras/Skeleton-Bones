package com.eudycontreras.boneslibrary.common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 */

@FunctionalInterface
internal interface RenderTarget {
    fun onRender(canvas: Canvas, paint: Paint, path: Path)
}
