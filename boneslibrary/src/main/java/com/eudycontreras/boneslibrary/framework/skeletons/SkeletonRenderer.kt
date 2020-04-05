package com.eudycontreras.boneslibrary.framework.skeletons

import android.graphics.*

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 */

internal class SkeletonRenderer(
    private val skeleton: Skeleton
) {
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
    }

    private val skeletonPath: Path = Path()

    var shouldRender: Boolean = true

    fun getPath() = skeletonPath

    fun update(fraction: Float) {
        skeleton.onUpdate(fraction)
    }

    fun fade(fraction: Float) {
        skeleton.onFade(fraction)
    }

    fun render(canvas: Canvas) {
        if (shouldRender) {
            val top = skeleton.bounds.top
            val left = skeleton.bounds.left
            val bottom = skeleton.bounds.bottom
            val right = skeleton.bounds.right
            val corners = skeleton.corners.corners

            canvas.save()

            skeletonPath.rewind()
            skeletonPath.addRoundRect(left, top, right, bottom, corners, Path.Direction.CCW)
            skeletonPath.close()

            canvas.clipPath(skeletonPath)

            skeleton.onRender(canvas, paint, skeletonPath)

            canvas.restore()
        }
    }
}
