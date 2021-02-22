package com.eudycontreras.boneslibrary.properties

import com.eudycontreras.boneslibrary.AndroidColor

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 *
 * Class which holds information about color channels allowing
 * access to the alpha, red, green and blue channels of a color
 */

abstract class Color(
    open var alpha: Int = MAX_COLOR,
    open var red: Int = MIN_COLOR,
    open var green: Int = MIN_COLOR,
    open var blue: Int = MIN_COLOR
) {
    protected var mTempColor: Int = -1

    protected var dirty: Boolean = false

    abstract fun getOpacity(): Float

    abstract fun toColor(): Int

    abstract fun reset()

    protected fun clamp(color: Int): Int {
        return when {
            color > MAX_COLOR -> MAX_COLOR
            color < MIN_COLOR -> MIN_COLOR
            else -> color
        }
    }

    companion object {

        const val MAX_COLOR_F: Float = 255f

        const val MAX_COLOR: Int = 255

        const val MIN_COLOR: Int = 0

        fun from(color: Int): Color {
            return MutableColor.fromColor(color)
        }

        fun mutate(): MutableColor {
            return MutableColor()
        }

        fun colorDecToHex(r: Int, g: Int, b: Int): Int {
            return AndroidColor.parseColor(colorDecToHexString(r, g, b))
        }

        fun colorDecToHex(a: Int, r: Int, g: Int, b: Int): Int {
            return AndroidColor.parseColor(colorDecToHexString(a, r, g, b))
        }

        fun colorDecToHexString(r: Int, g: Int, b: Int): String {
            return colorDecToHexString(MAX_COLOR, r, g, b)
        }

        fun colorDecToHexString(a: Int, r: Int, g: Int, b: Int): String {
            var red = Integer.toHexString(r)
            var green = Integer.toHexString(g)
            var blue = Integer.toHexString(b)
            var alpha = Integer.toHexString(a)

            if (red.length == 1) {
                red = "0$red"
            }
            if (green.length == 1) {
                green = "0$green"
            }
            if (blue.length == 1) {
                blue = "0$blue"
            }
            if (alpha.length == 1) {
                alpha = "0$alpha"
            }

            return "#$alpha$red$green$blue"
        }
    }
}
