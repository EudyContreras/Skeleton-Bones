package com.eudycontreras.boneslibrary.common

import android.graphics.Shader
import android.view.View.NO_ID
import com.eudycontreras.boneslibrary.MIN_OFFSET
import com.eudycontreras.boneslibrary.properties.Bounds
import com.eudycontreras.boneslibrary.properties.Color.Companion.MAX_COLOR
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 */

abstract class DrawableShape {

    internal var id: Int = NO_ID

    internal var corners: CornerRadii = CornerRadii()

    internal var color: MutableColor? = null

    internal var backgroundColor: MutableColor? = null

    internal var bounds: Bounds = Bounds()

    internal var elevation: Float = MIN_OFFSET

    internal var strokeWidth: Float = MIN_OFFSET

    internal var shadowAlpha: Int = MAX_COLOR

    internal val alpha: Float
        get() = opacity.toFloat() / MAX_COLOR.toFloat()

    internal var opacity: Int = MAX_COLOR
        set(value) {
            field = value
            color?.updateAlpha(opacity)
            backgroundColor?.updateAlpha(opacity)
        }

    internal val drawShadows: Boolean
        get() = elevation > MIN_OFFSET && shadowAlpha > MIN_OFFSET

    internal var shadowColor: MutableColor? = null

    internal var shader: Shader? = null

    internal var x: Float
        get() = bounds.x
        set(value) {
            bounds.x = value
        }

    internal var y: Float
        get() = bounds.y
        set(value) {
            bounds.y = value
        }

    internal var width: Float
        get() = bounds.width
        set(value) {
            bounds.width = value
        }

    internal var height: Float
        get() = bounds.height
        set(value) {
            bounds.height = value
        }

    internal val radius: Float
        get() = ((width + height) / 2) / 2

    internal val radii: FloatArray
        get() = corners.corners

    val left: Float = bounds.left
    val right: Float = bounds.right
    val bottom: Float = bounds.bottom
    val top: Float = bounds.top

}
