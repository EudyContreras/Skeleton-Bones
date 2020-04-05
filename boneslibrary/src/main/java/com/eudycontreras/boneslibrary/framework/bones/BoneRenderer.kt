package com.eudycontreras.boneslibrary.framework.bones

import android.graphics.*

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 */

internal class BoneRenderer(
    private val bone: Bone
) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val bonePath: Path = Path()

    var shouldRender: Boolean = true

    fun getPath() = bonePath

    fun update(fraction: Float) {
        bone.onUpdate(fraction)
    }

    fun fade(fraction: Float) {
        bone.onFade(fraction)
    }

    fun render(canvas: Canvas) {
        if (shouldRender) {
            canvas.save()

            bonePath.rewind()

            bone.onRender(canvas, paint, bonePath, bonePath)

            bonePath.close()

            canvas.clipPath(bonePath)

            canvas.restore()
        }
    }
}
