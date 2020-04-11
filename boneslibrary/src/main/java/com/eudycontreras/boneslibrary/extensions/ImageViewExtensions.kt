package com.eudycontreras.boneslibrary.extensions

import android.widget.ImageView
import com.eudycontreras.boneslibrary.bindings.getParentSkeletonDrawable
import com.eudycontreras.boneslibrary.doWith
import com.eudycontreras.boneslibrary.framework.bones.BoneDrawable

/**
 * Copyright (C) 2020 Bones
 *
 * @Project Project Bones
 * @author Eudy Contreras.
 * @since March 2020
 *
 * Notifies the backing bone for this ImageView that the image has
 * been loaded.
 */

fun ImageView.notifySkeletonImageLoaded(): Boolean {
    val id = generateId()
    val parent = getParentSkeletonDrawable()
    if (parent != null) {
        parent.getProps().setStateOwner(id, false)
        parent.getProps().getBoneProps(id).apply {
            this.enabled = false
            return true
        }
    }
    doWith(foreground) {
        if (it is BoneDrawable) {
            it.getProps().enabled = false
            return true
        }
    }
    return false
}