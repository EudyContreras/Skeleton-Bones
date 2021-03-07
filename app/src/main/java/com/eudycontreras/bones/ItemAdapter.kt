package com.eudycontreras.bones

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eudycontreras.boneslibrary.extensions.disableSkeletonLoading
import com.eudycontreras.boneslibrary.extensions.dp
import com.eudycontreras.boneslibrary.framework.bones.BoneBuilder
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayBuilder
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonBuilder
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType
import kotlinx.android.synthetic.main.list_item_a.view.*
import kotlinx.android.synthetic.main.list_item_b.view.*
import kotlinx.coroutines.*

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since February 2021
 */

@Suppress("UNCHECKED_CAST")
class ItemAdapter<T : DemoData>(
    private var data: List<T?>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder<T>>() {

    fun updateData(newData: List<T?>) {
        if (data != newData) {
            updateData(data, newData)
        }
    }

    private fun updateData(oldData: List<T?>, newData: List<T?>) {
        DiffUtil.calculateDiff(DiffCallback(oldData, newData)).run {
            data = newData
            dispatchUpdatesTo(AdapterListUpdateCallback(this@ItemAdapter))
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder<T>, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is DemoData.A -> VIEW_TYPE_A
            is DemoData.B -> VIEW_TYPE_B
            /**
             * Here I decide how we render the skeleton
             */
            else -> when {
                position.isEven() -> LOADING_A
                else -> LOADING_B
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @LayoutRes viewType: Int
    ): ItemViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_A -> ItemViewHolder.A(
                itemView = inflater.inflate(R.layout.list_item_a, parent, false)
            ) as ItemViewHolder<T>
            VIEW_TYPE_B -> ItemViewHolder.B(
                itemView = inflater.inflate(R.layout.list_item_b, parent, false)
            ) as ItemViewHolder<T>
            /**
             * As you can see we have loading view types and view holders too
             */
            LOADING_A -> ItemViewHolder.Loader(
                itemView = inflater.inflate(R.layout.list_item_a, parent, false)
            ) as ItemViewHolder<T>
            LOADING_B -> ItemViewHolder.Loader(
                itemView = inflater.inflate(R.layout.list_item_b, parent, false)
            ) as ItemViewHolder<T>
            else -> throw IllegalStateException()
        }
    }

    override fun getItemCount(): Int = data.size

    abstract class ItemViewHolder<T : DemoData>(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(data: T?, position: Int)

        class A(itemView: View) : ItemViewHolder<DemoData.A>(itemView) {
            override fun bind(data: DemoData.A?, position: Int) {
                data?.let {
                    itemView.ItemAText.text = it.text
                    itemView.ItemAImage.loadImage(it.imageUrl)
                }
            }
        }

        class B(itemView: View) : ItemViewHolder<DemoData.B>(itemView) {
            override fun bind(data: DemoData.B?, position: Int) {
                val context: Context = itemView.context
                data?.let {
                    itemView.ItemBOuterText.text = it.textOne
                    itemView.ItemBInnerTextA.text = it.textTwo
                    itemView.ItemBInnerTextB.text = it.textThree
                    itemView.ItemBImage.loadImage(it.imageUrl)
                }
                val drawable: SkeletonDrawable = SkeletonBuilder()
                    .setAllowSavedState(true)
                    .setUseStateTransition(true)
                    .setStateTransitionDuration(200L)
                    .withBoneBuilder(itemView.ItemBImage, imageBoneBuilder(context, ShapeType.RECTANGULAR))
                    .withBoneBuilder(itemView.ItemBOuterText, textBoneBuilder(context, true))
                    .withBoneBuilder(itemView.ItemBInnerTextA, textBoneBuilder(context, false))
                    .withBoneBuilder(itemView.ItemBInnerTextB, textBoneBuilder(context, false))
                    .withIgnoredBones(itemView.ItemBInnerTextA)
                    .setEnabled(true)
                    .get()

                if (position == 1) {
                    MainScope().launch {

                        delay(3000)
                        /**
                         * We can enable a loader in multiple ways
                         */
                        //itemView.ItemBContainer.addSkeletonLoader(true, drawable)
                        drawable.enable(itemView.ItemBContainer)

                        delay(1500)
                        itemView.ItemBContainer.disableSkeletonLoading()

                        delay(1500)
                        drawable.enable(itemView.ItemBContainer)

                        delay(1500)
                        drawable.disable()
                    }
                }
            }
        }

        class Loader(itemView: View) : ItemViewHolder<DemoData>(itemView) {
            override fun bind(data: DemoData?, position: Int) {
                /**
                 * As you can see we can easily compose the bone and skeletons
                 * that we want to display
                 */
                val container: ViewGroup? = itemView.ItemAContainer ?: itemView.ItemBContainer
                container?.let { parent ->
                    val context: Context = parent.context
                    SkeletonDrawable.create(parent, true)
                        .builder()
                        .setAllowSavedState(false)
                        .setUseStateTransition(true)
                        .setStateTransitionDuration(200L)
                        .withBoneBuilder(parent.ItemAImage, imageBoneBuilder(context, ShapeType.CIRCULAR))
                        .withBoneBuilder(parent.ItemBImage, imageBoneBuilder(context, ShapeType.RECTANGULAR))
                        .withBoneBuilder(parent.ItemAText, textBoneBuilder(context, true))
                        .withBoneBuilder(parent.ItemBInnerTextA, textBoneBuilder(context, false))
                        .withBoneBuilder(parent.ItemBInnerTextB, textBoneBuilder(context, false))
                        .withBoneBuilder(parent.ItemBOuterText, textBoneBuilder(context, false))
                        .setEnabled(true)
                }

                if (position == 1) {
                    /**
                     * To disable a skeleton after it has been created we have to do it on
                     * the next frame. This can be do using the main dispatcher or by posting
                     * to the message queue
                     */
                    container?.let {
                        MainScope().launch {
                            delay(500)
                            it.ItemAText?.disableSkeletonLoading()
                            it.ItemBInnerTextA?.disableSkeletonLoading()
                        }
                    }
                }
            }
        }

        companion object {
            val imageBoneBuilder: (context: Context, shape: ShapeType) -> BoneBuilder.() -> Unit = { context, shape -> {
                withShimmerBuilder(shimmerRayBuilder(context, R.color.bone_ray_color_alt, 0.1f, 1))
                setColor(MutableColor.fromColor(ContextCompat.getColor(context, R.color.bone_color_alt)))
                setCornerRadius(10.dp)
                setShaderMultiplier(1.021f)
                setShapeType(shape)
            } }

            val textBoneBuilder: (context: Context, dissect: Boolean) -> BoneBuilder.() -> Unit = { context, dissect -> {
                withShimmerBuilder(shimmerRayBuilder(context, R.color.bone_ray_color, 2f, 3))
                setColor(MutableColor.fromColor(ContextCompat.getColor(context, R.color.bone_color)))
                setDissectBones(dissect)
                setCornerRadius(10.dp)
                setMaxThickness(10.dp)
                setMinThickness(10.dp)
            } }

            val shimmerRayBuilder: (context: Context, color: Int, speed: Float, count: Int) ->  ShimmerRayBuilder.() -> Unit = { context, color, speed, count -> {
                setColor(MutableColor.fromColor(ContextCompat.getColor(context, color)))
                setInterpolator(FastOutSlowInInterpolator())
                setSharedInterpolator(true)
                setSpeedMultiplier(speed)
                setThicknessRatio(1.2f)
                setTilt(-0.1f)
                setCount(count)
            } }
        }
    }

    companion object {
        const val VIEW_TYPE_A = 0
        const val VIEW_TYPE_B = 1
        const val LOADING_A = 2
        const val LOADING_B = 3
    }
}

class DiffCallback<T : DiffComparable>(
    private var oldItems: List<T?>,
    private var newItems: List<T?>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItems.size == newItems.size) {
            return true
        }
        return oldItems[oldItemPosition]?.getIdentifier() == newItems[newItemPosition]?.getIdentifier()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size
}

interface DiffComparable {
    fun getIdentifier(): String
}