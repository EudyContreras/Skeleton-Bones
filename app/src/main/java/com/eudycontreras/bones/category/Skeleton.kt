package com.eudycontreras.bones.category

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.eudycontreras.bones.R
import com.eudycontreras.boneslibrary.bindings.getBoneDrawable
import com.eudycontreras.boneslibrary.bindings.getSkeletonDrawable
import com.eudycontreras.boneslibrary.extensions.dp
import com.eudycontreras.boneslibrary.framework.bones.BoneBuilder
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayBuilder
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayProperties
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonBuilder
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonProperties
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType

object Skeleton {
    fun createIfNeeded(root: ViewGroup): SkeletonBuilder {
        val skeleton = root.getSkeletonDrawable()
        return if (skeleton != null) {
            skeleton.builder().setEnabled(true)
            skeleton.builder()
        } else {
            SkeletonDrawable.create(root, true, builder(root)).builder()
        }
    }

    fun SkeletonBuilder.forImageView(
        imageView: ImageView,
        parent: ViewGroup,
        round: Boolean = false
    ): SkeletonBuilder {
        if (imageView.getBoneDrawable() != null) {
            return withBoneBuilder(null) {}
        }
        return withBoneBuilder(
            imageView,
            Skeleton.imageBoneBuilder(
                parent,
                if (round) ShapeType.CIRCULAR else ShapeType.RECTANGULAR
            )
        )
    }

    fun SkeletonBuilder.forTextView(textView: TextView, parent: ViewGroup): SkeletonBuilder {
        if (textView.getBoneDrawable() != null) {
            return withBoneBuilder(null) {}
        }
        return withBoneBuilder(textView, Skeleton.textBoneBuilder(parent, false))
    }

    private fun builder(root: ViewGroup): SkeletonBuilder {
        val properties = SkeletonProperties().apply {
            allowSavedState = false
            useStateTransition = true
            stateTransitionDuration = 200L
            shimmerRayProperties = shimmerRayPropertiesBuilder(root, R.color.bone_ray_color)
        }
        return SkeletonDrawable.builder(properties)
    }

    private val imageBoneBuilder: (view: View, shape: ShapeType) -> BoneBuilder.() -> Unit =
        { view, shape ->
            {
                textBoneBuilder(view, false)(this)
                setWidth(32.dpToPx)
                setColor(
                    MutableColor.fromColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.bone_color_alt
                        )
                    )
                )
                setShaderMultiplier(1.021f)
                setShapeType(shape)
                withShimmerBuilder(shimmerRayBuilder(view, R.color.bone_ray_color_alt))
            }
        }

    private val textBoneBuilder: (view: View, dissect: Boolean) -> BoneBuilder.() -> Unit =
        { view, dissect ->
            {
                setDissectBones(dissect)
                setColor(
                    MutableColor.fromColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.bone_color
                        )
                    )
                )
                setCornerRadii(CornerRadii(3.dp))
                setMaxThickness(500.dp)
                setMinThickness(12.dp)
            }
        }

    private val shimmerRayBuilder: (view: View, color: Int) -> ShimmerRayBuilder.() -> Unit =
        { view, color ->
            {
                setColor(MutableColor.fromColor(ContextCompat.getColor(view.context, color)))
                setCount(3)
                setInterpolator(FastOutSlowInInterpolator())
                setSharedInterpolator(true)
                setSpeedMultiplier(0.8f)
                setThicknessRatio(1.2f)
                setTilt(-0.3f)
            }
        }

    @Suppress("SameParameterValue")
    private fun shimmerRayPropertiesBuilder(
        view: View,
        @ColorRes color: Int
    ): ShimmerRayProperties {
        val properties = ShimmerRayProperties()
        properties.shimmerRayColor =
            MutableColor.fromColor(ContextCompat.getColor(view.context, color))
        properties.shimmerRayCount = 3
        properties.shimmerRayInterpolator = FastOutSlowInInterpolator()
        properties.shimmerRaySharedInterpolator = true
        properties.shimmerRaySpeedMultiplier = 1f
        properties.shimmerRayThicknessRatio = 1.2f
        properties.shimmerRayTilt = -0.1f

        return properties
    }
}

val Int.pxToDp: Float
    get() = this / Resources.getSystem().displayMetrics.density
val Int.dpToPx: Float
    get() = this * Resources.getSystem().displayMetrics.density