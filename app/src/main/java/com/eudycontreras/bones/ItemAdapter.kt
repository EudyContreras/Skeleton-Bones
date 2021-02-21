package com.eudycontreras.bones

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eudycontreras.boneslibrary.extensions.dp
import com.eudycontreras.boneslibrary.framework.bones.BoneBuilder
import com.eudycontreras.boneslibrary.framework.shimmer.ShimmerRayBuilder
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor
import com.eudycontreras.boneslibrary.properties.ShapeType
import kotlinx.android.synthetic.main.list_item_a.view.*
import kotlinx.android.synthetic.main.list_item_b.view.*

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
        holder.bind(data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is DemoData.A -> VIEW_TYPE_A
            is DemoData.B -> VIEW_TYPE_B
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
            LOADING_A -> ItemViewHolder.Loading(
                itemView = inflater.inflate(R.layout.list_item_a, parent, false)
            ) as ItemViewHolder<T>
            else -> ItemViewHolder.Loading(
                itemView = inflater.inflate(R.layout.list_item_b, parent, false)
            ) as ItemViewHolder<T>
        }
    }

    override fun getItemCount(): Int = data.size

    sealed class ItemViewHolder<T : DemoData>(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(data: T?)

        class A(itemView: View) : ItemViewHolder<DemoData.A>(itemView) {
            override fun bind(data: DemoData.A?) {
                data?.let {
                    itemView.ItemAText.text = it.text
                    itemView.ItemAImage.loadImage(it.imageUrl)
                }
            }
        }

        class B(itemView: View) : ItemViewHolder<DemoData.B>(itemView) {
            override fun bind(data: DemoData.B?) {
                data?.let {
                    itemView.ItemBOuterText.text = it.textOne
                    itemView.ItemBInnerTextA.text = it.textTwo
                    itemView.ItemBInnerTextB.text = it.textThree
                    itemView.ItemBImage.loadImage(it.imageUrl)
                }
            }
        }

        class Loading(itemView: View) : ItemViewHolder<DemoData>(itemView) {
            override fun bind(data: DemoData?) {
                itemView.findViewById<ViewGroup>(R.id.ItemContainer)?.let { parent ->
                    SkeletonDrawable.create(parent, true).build()
                        .setAllowSavedState(false)
                        .setUseStateTransition(true)
                        .setStateTransitionDuration(200L)
                        .withBoneBuilder(R.id.ItemAText, textBoneBuilder(parent))
                        .withBoneBuilder(R.id.ItemBInnerTextA, textBoneBuilder(parent))
                        .withBoneBuilder(R.id.ItemBInnerTextB, textBoneBuilder(parent))
                        .withBoneBuilder(R.id.ItemBOuterText, textBoneBuilder(parent))
                        .withBoneBuilder(R.id.ItemAImage, imageBoneBuilder(parent))
                        .withBoneBuilder(R.id.ItemBImage, imageBoneBuilder(parent))
                        .withShimmerBuilder(shimmerRayBuilder(parent))
                        .setEnabled(true)
                }
            }
        }

        companion object {
            val imageBoneBuilder: (view: View) -> BoneBuilder.() -> Unit = {
                {   setColor(MutableColor.fromColor(ContextCompat.getColor(it.context, R.color.bone_color_alt)))
                    setShaderMultiplier(1.021f)
                    setShapeType(ShapeType.CIRCULAR)
                }
            }
            val textBoneBuilder: (view: View) -> BoneBuilder.() -> Unit = {
                {   setColor(MutableColor.fromColor(ContextCompat.getColor(it.context, R.color.bone_color)))
                    setCornerRadii(CornerRadii(10.dp))
                    setMaxThickness(10.dp)
                    setMinThickness(10.dp)
                    setDissectBones(true)
                }
            }
            val shimmerRayBuilder: (view: View) ->  ShimmerRayBuilder.() -> Unit = {
                {   setColor(MutableColor.fromColor(ContextCompat.getColor(it.context, R.color.bone_ray_color)))
                    setCount(3)
                    setInterpolator(FastOutSlowInInterpolator())
                    setSharedInterpolator(true)
                    setSpeedMultiplier(1.1f)
                    setThicknessRatio(0.6f)
                    setTilt(-0.1f)
                }
            }
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