package com.eudycontreras.boneslibrary.framework

import android.animation.LayoutTransition
import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.eudycontreras.boneslibrary.extensions.verticalPadding
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties
import com.eudycontreras.boneslibrary.framework.bones.BoneProperties.LayoutTransitionData
import com.eudycontreras.boneslibrary.or
import com.eudycontreras.boneslibrary.properties.Dimension
import com.eudycontreras.boneslibrary.tryGet

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since April 2020
 */

internal object BoneBoundsHandler {

    private fun getSaveTransitionData(wasNull: Boolean, layoutTransition: LayoutTransition): LayoutTransitionData {
        return LayoutTransitionData(
            nullTransition = wasNull,
            changingDuration = layoutTransition.getDuration(LayoutTransition.CHANGING),
            isChangingEnabled = layoutTransition.isTransitionTypeEnabled(LayoutTransition.CHANGING),
            isDisappearingEnabled = layoutTransition.isTransitionTypeEnabled(LayoutTransition.DISAPPEARING)
        )
    }

    private fun restoreLayoutTransition(viewGroup: ViewGroup, layoutTransitionData: LayoutTransitionData?) {

        val layoutTransition = tryGet { viewGroup.layoutTransition } or { LayoutTransition() }

        layoutTransitionData?.apply {
            layoutTransition.setDuration(LayoutTransition.CHANGING, changingDuration)
            if (isChangingEnabled) {
                layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            } else {
                layoutTransition.disableTransitionType(LayoutTransition.CHANGING)
            }
            if (isDisappearingEnabled) {
                layoutTransition.enableTransitionType(LayoutTransition.DISAPPEARING)
            } else {
                layoutTransition.disableTransitionType(LayoutTransition.DISAPPEARING)
            }
        }

        viewGroup.layoutTransition = if (layoutTransitionData?.nullTransition == false) layoutTransition else null
    }

    fun applyTemporaryBounds(view: View, boneProps: BoneProperties, animateRestoration: Boolean) {
        with(view) {
            boneProps.originalBounds = Dimension(
                width = minimumWidth.toFloat(),
                height = minimumHeight.toFloat()
            )
            if (boneProps.minWidth == null && boneProps.minHeight == null) {
                applyMinTemporaryBounds(view, boneProps, animateRestoration)
                return
            }
            val parent = parent as? ViewGroup?

            if (parent != null && animateRestoration) {
                applyLayoutAnimationOut(parent, boneProps)
            }
            if (minimumWidth <= 0) {
                minimumWidth = boneProps.minWidth?.toInt() ?: minimumWidth
            }
            if (minimumHeight <= 0) {
                minimumHeight = boneProps.minHeight?.toInt() ?: minimumHeight
            }
        }
    }

    fun applyTemporaryBoundsForValid(view: View, boneProps: BoneProperties, animateRestoration: Boolean) {
        with(view) {
            boneProps.originalBounds = Dimension(
                width = minimumWidth.toFloat(),
                height = minimumHeight.toFloat()
            )

            if (boneProps.minWidth == null && boneProps.minHeight == null) {
                applyMinTemporaryBounds(view, boneProps, animateRestoration)
                return
            }

            minimumWidth = boneProps.minWidth?.toInt() ?: minimumWidth
            minimumHeight = boneProps.minHeight?.toInt() ?: minimumHeight

            val parent = parent as? ViewGroup?

            if (parent != null && animateRestoration) {
                applyLayoutAnimationOut(parent, boneProps)
            }
        }
    }

    private fun applyMinTemporaryBounds(view: View, boneProps: BoneProperties, animateRestoration: Boolean) {
        with(view) {
            val verticalPadding = view.verticalPadding

            if (minimumHeight <= 0) {
                boneProps.minThickness.let {
                    val newValue = it + verticalPadding
                    minimumHeight = newValue.toInt()
                }
            }

            val parent = parent as? ViewGroup?

            if (parent != null && animateRestoration) {
                applyLayoutAnimationOut(parent, boneProps)
            }
        }
    }

    private fun applyLayoutAnimationOut(
        parent: ViewGroup,
        boneProps: BoneProperties
    ) {
        var wasNull = false

        val layoutTransition = tryGet { parent.layoutTransition } or {
            wasNull = true
            LayoutTransition()
        }

        if (boneProps.originalParentTransition == null) {
            boneProps.originalParentTransition = getSaveTransitionData(wasNull, layoutTransition)
        }

        parent.layoutTransition = layoutTransition
    }

    private fun applyLayoutAnimationIn(parent: ViewGroup, animDuration: Long) {
        val layoutTransition = tryGet { parent.layoutTransition } or { LayoutTransition() }

        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        layoutTransition.disableTransitionType(LayoutTransition.DISAPPEARING)
        layoutTransition.setDuration(LayoutTransition.CHANGING, animDuration)
        layoutTransition.setInterpolator(LayoutTransition.CHANGING, FastOutSlowInInterpolator())
        parent.layoutTransition = layoutTransition
    }

    fun restoreBounds(view: View, boneProps: BoneProperties, animateRestoration: Boolean, animDuration: Long? = null) {
        with(view) {
            val parent = parent as? ViewGroup?

            if (parent != null && animDuration != null && animateRestoration) {
                applyLayoutAnimationIn(parent, animDuration)
            }

            val originalBounds = boneProps.originalBounds

            originalBounds?.let {
                with(it.width.toInt()) {
                    if (minimumWidth != this) {
                        minimumWidth = this
                    }
                }
                with(it.height.toInt()) {
                    if (minimumHeight != this) {
                        minimumHeight = this
                    }
                }
            }
        }
    }

    fun restoreTransitions(view: View, boneProps: BoneProperties, animateRestoration: Boolean, animDuration: Long? = null) {
        with(view) {
            val parent = parent as? ViewGroup?

            if (parent != null && animDuration != null && animateRestoration) {
                restoreLayoutTransition(parent, boneProps.originalParentTransition)
            }
        }
    }
}