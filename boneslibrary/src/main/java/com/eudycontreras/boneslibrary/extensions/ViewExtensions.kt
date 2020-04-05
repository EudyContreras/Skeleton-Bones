package com.eudycontreras.boneslibrary.extensions

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup
import androidx.core.view.*
import com.eudycontreras.boneslibrary.framework.bones.BoneDrawable
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.properties.MutableColor
import java.lang.ref.WeakReference

/**
 * Copyright (C) 2019 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since January 2019
 */

internal val View.horizontalMargin: Float
    get() = (this.marginStart + this.marginEnd).toFloat()

internal val View.horizontalPadding: Float
    get() = (this.paddingStart + this.paddingEnd).toFloat()

internal val View.verticalMargin: Float
    get() = (this.marginTop + this.marginBottom).toFloat()

internal val View.verticalPadding: Float
    get() = (this.paddingTop + this.paddingBottom).toFloat()

internal fun View.generateId(): Int {
    return if (id == NO_ID) {
        View.generateViewId().apply {
            id = this
        }
    } else id
}

/**
 * Applies a skeleton drawable to this ViewGroup
 */
fun ViewGroup.applySkeletonDrawable(): SkeletonDrawable {
    return SkeletonDrawable.create(this)
}

/**
 * Applies a bone drawable to this View
 */
fun View.applyBoneDrawable(): BoneDrawable {
    return BoneDrawable.create(this)
}

fun View.hasValidBounds(): Boolean {
    return (measuredWidth > 0 && measuredHeight > 0)
}

fun View.hasDrawableBounds(boneProps: BoneProperties): Boolean {
    val minThickness = boneProps.minThickness ?: BoneProperties.MIN_THICKNESS
    return (measuredWidth > 0 && measuredHeight > minThickness)
}

internal fun View.getBackgroundColor(): MutableColor? {
    if (backgroundTintList != null) {
        return MutableColor.fromColor(backgroundTintList?.defaultColor)
    } else {
       background?.let {
           if (it is ColorDrawable) {
               return MutableColor.fromColor(it.color)
           } else if (it is GradientDrawable) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                   if (it.color != null) {
                       return MutableColor.fromColor(it.color?.defaultColor)
                   }
               }
           }
       }
    }
    return null
}

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T> View.getProps(propId: Int): T? {
    val props = getTag(propId)
    if (props != null) {
        if (props is WeakReference<*>) {
            val reference: WeakReference<T> = props as WeakReference<T>
            return reference.get()
        } else if (props is T) {
            return props
        }
    }
    return null
}

internal fun View.hasProps(propId: Int): Boolean {
    val props = getTag(propId)
    if (props != null) {
        return true
    }
    return false
}

internal inline fun <reified T> View.saveProps(propId: Int, props: T, weak: Boolean = false) {
    if (weak) {
        val reference = WeakReference(props)
        setTag(propId, reference)
    } else {
        setTag(propId, props)
    }
}

internal tailrec fun View.findParent(criteria: ((parent: View) -> Boolean)? = null): ViewGroup? {
    val parent: ViewGroup? = this.parent as? ViewGroup?

    if (parent != null) {
        if (criteria?.invoke(parent) == true) {
            return parent
        }
    } else {
        return if (criteria != null) {
            null
        } else this as ViewGroup
    }

    return parent.findParent(criteria)
}

internal fun View.findView(criteria: (child: View) -> Boolean): View? {
    if (criteria(this)) {
       return this
    }
    if (this is ViewGroup) {
        for (child in children) {
            val match = child.findView(criteria)
            if (match != null) {
                if (criteria.invoke(match)) {
                    return match
                }
            }
        }
    }
    return null
}

internal fun ViewGroup.descendantViews(predicate: ((view: View) -> Boolean)? = null): List<View> {
    val views = mutableListOf<View>()
    findViews(this, predicate, views)
    return views
}

private fun findViews(
    viewGroup: ViewGroup,
    predicate: ((View) -> Boolean)? = null,
    views: MutableList<View>
) {
    viewGroup.children.forEach {
        if (it !is ViewGroup) {
            if (predicate != null) {
                if (predicate(it)) {
                    views.add(it)
                }
            } else {
                views.add(it)
            }
        } else {
            findViews(it, predicate, views)
        }
    }
}
