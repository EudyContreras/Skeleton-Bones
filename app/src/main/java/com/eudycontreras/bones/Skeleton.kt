package com.eudycontreras.bones

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.eudycontreras.boneslibrary.bindings.isSkeletonLoader
import com.eudycontreras.boneslibrary.extensions.dp
import com.eudycontreras.boneslibrary.extensions.enableSkeletonLoading
import com.eudycontreras.boneslibrary.framework.bones.BoneBuilder
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayBuilder
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonBuilder
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType

object Skeleton {
    private val builder: SkeletonBuilder = SkeletonBuilder()
        .setAllowSavedState(false)
        .setUseStateTransition(true)
        .setStateTransitionDuration(200L)

        .setAnimateRestoreBounds(true)
        //.setAllowReuse(true)
        .setEnabled(true)

    fun buildSkeleton(): SkeletonBuilder {
        return builder
    }

    fun SkeletonBuilder.forImages(view: View, round: Boolean = false): SkeletonBuilder {
        return withBoneBuilder(view, imageBoneBuilder(view.context, if (round) ShapeType.CIRCULAR else ShapeType.RECTANGULAR))
    }

    fun SkeletonBuilder.forText(view: View, dissect: Boolean = false): SkeletonBuilder {
        return withBoneBuilder(view, textBoneBuilder(view.context, dissect))
    }

    fun SkeletonBuilder.forLongText(view: View, dissect: Boolean = true): SkeletonBuilder {
        return withBoneBuilder(view, longTextBoneBuilder(view.context, dissect))
    }

    fun SkeletonBuilder.ignore(view: View): SkeletonBuilder {
        return withIgnoredBones(view)
    }

    fun SkeletonBuilder.createIfNeeded(root: ViewGroup): SkeletonDrawable? {
        return if (root.isSkeletonLoader()) {
            root.enableSkeletonLoading()
        } else {
            get().apply {
                this.enable(root)
            }
        }
    }

    private val imageBoneBuilder: (context: Context, shape: ShapeType) -> BoneBuilder.() -> Unit = { context, shape -> {
        withShimmerBuilder(shimmerRayBuilder(context, R.color.bone_ray_color_alt, 0.1f, 1))
        setColor(MutableColor.fromColor(ContextCompat.getColor(context, R.color.bone_color_alt)))
        setCornerRadius(10.dp)
        setShaderMultiplier(1.021f)
        setShapeType(shape)
    } }

    private val textBoneBuilder: (context: Context, dissect: Boolean) -> BoneBuilder.() -> Unit = { context, dissect -> {
        withShimmerBuilder(shimmerRayBuilder(context, R.color.bone_ray_color, 2f, 3))
        setColor(MutableColor.fromColor(ContextCompat.getColor(context, R.color.bone_color)))
        setDissectBones(dissect)
        setCornerRadius(10.dp)
        setMaxThickness(10.dp)
        setMinThickness(10.dp)
    } }

    private val longTextBoneBuilder: (context: Context, dissect: Boolean) -> BoneBuilder.() -> Unit = { context, dissect -> {
        withShimmerBuilder(shimmerRayBuilder(context, R.color.bone_ray_color, 2f, 3))
        setColor(MutableColor.fromColor(ContextCompat.getColor(context, R.color.bone_color)))
        setDissectBones(dissect)
        setCornerRadius(10.dp)
        setMaxThickness(10.dp)
        setMinThickness(10.dp)
    } }

    private val shimmerRayBuilder: (context: Context, color: Int, speed: Float, count: Int) ->  ShimmerRayBuilder.() -> Unit = { context, color, speed, count -> {
        setColor(MutableColor.fromColor(ContextCompat.getColor(context, color)))
        setInterpolator(FastOutSlowInInterpolator())
        setSharedInterpolator(true)
        setSpeedMultiplier(speed)
        setThicknessRatio(1.2f)
        setTilt(-0.1f)
        setCount(count)
    } }
}