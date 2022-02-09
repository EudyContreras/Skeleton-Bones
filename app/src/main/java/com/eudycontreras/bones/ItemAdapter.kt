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
import com.eudycontreras.boneslibrary.framework.skeletons.SkeletonDrawable
import com.eudycontreras.boneslibrary.properties.CornerRadii
import com.eudycontreras.boneslibrary.properties.MutableColor
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since February 2021
 */

@Suppress("UNCHECKED_CAST")
class ItemAdapter(
    private var data: List<DemoData?>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    fun updateData(newData: List<DemoData?>) {
        if (data != newData) {
            updateData(data, newData)
        }
    }

    private fun updateData(oldData: List<DemoData?>, newData: List<DemoData?>) {
        DiffUtil.calculateDiff(DiffCallback(oldData, newData)).run {
            data = newData
            dispatchUpdatesTo(AdapterListUpdateCallback(this@ItemAdapter))
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (this.data[position]) {
            is DemoData -> VIEW_TYPE
            else -> LOADING_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @LayoutRes viewType: Int
    ): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val isLoading = viewType == LOADING_VIEW_TYPE
        return ItemViewHolder(
            isLoading = isLoading,
            itemView = inflater.inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    class ItemViewHolder(
        val isLoading: Boolean,
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView)  {

        init {
            if (isLoading) {
                showSkeleton()
            }
        }

        fun bind(data: DemoData?) {
            data?.let {
                itemView.ItemBOuterText.text = it.textOne
                itemView.ItemBInnerTextA.text = it.textTwo
                itemView.ItemBInnerTextB.text = it.textThree
                itemView.ItemBImage.loadImage(it.imageUrl)
            }
        }

        private fun showSkeleton() {
            /**
             * As you can see we can easily compose the bone and skeletons
             * that we want to display
             */
            itemView.findViewById<ViewGroup>(R.id.ItemContainer)?.let { parent ->
                SkeletonDrawable.create(parent, true)
                    .builder()
                    .setAllowSavedState(false)
                    .setUseStateTransition(true)
                    .setStateTransitionDuration(200L)
                    .withBoneBuilder {
                        setDissectBones(true)
                        setColor(MutableColor.fromColor(ContextCompat.getColor(parent.context, R.color.bone_color)))
                        setCornerRadii(CornerRadii(10.dp))
                        setMaxThickness(10.dp)
                        setMinThickness(10.dp)
                    }
                    .withBoneBuilder(R.id.ItemBInnerTextA) {
                        setDissectBones(true)
                        setColor(MutableColor.fromColor(ContextCompat.getColor(parent.context, R.color.bone_color)))
                        setCornerRadii(CornerRadii(10.dp))
                        setMaxThickness(10.dp)
                        setMinThickness(10.dp)
                    }
                    .withShimmerBuilder{
                        setColor(MutableColor.fromColor(ContextCompat.getColor(parent.context, R.color.bone_color_alt)))
                        setCount(3)
                        setInterpolator(FastOutSlowInInterpolator())
                        setSharedInterpolator(false)
                        setSpeedMultiplier(1.1f)
                        setThicknessRatio(1.2f)
                        setTilt(-0.1f)
                    }
            }
        }
    }

    companion object {
        const val VIEW_TYPE = 0
        const val LOADING_VIEW_TYPE = 1
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