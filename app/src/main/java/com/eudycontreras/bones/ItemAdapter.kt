package com.eudycontreras.bones

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eudycontreras.bones.Skeleton.createIfNeeded
import com.eudycontreras.bones.Skeleton.forImages
import com.eudycontreras.bones.Skeleton.forLongText
import com.eudycontreras.bones.Skeleton.forText
import com.eudycontreras.bones.Skeleton.ignore
import com.eudycontreras.boneslibrary.extensions.disableSkeletonLoading
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

    override fun getItemCount(): Int = data.size

    fun updateData(newData: List<DemoData?>) {
        if (data != newData) {
            updateData(data, newData)
        }
    }

    private fun getItem(position: Int): DemoData? {
        return data[position]
    }

    override fun onViewRecycled(holder: ItemViewHolder) {
        if (holder is ItemViewHolder.PlaceHolder) {
            holder.stopLoading()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position) == null) {
            true -> VIEW_TYPE_PLACEHOLDER
            else -> VIEW_TYPE_NORMAL
        }
    }

    private fun updateData(oldData: List<DemoData?>, newData: List<DemoData?>) {
        DiffUtil.calculateDiff(DiffCallback(oldData, newData)).run {
            data = newData
            dispatchUpdatesTo(AdapterListUpdateCallback(this@ItemAdapter))
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ItemViewHolder.Normal -> holder.bind(item)
            is ItemViewHolder.PlaceHolder -> holder.startLoading()
        }
    }

    override fun onViewDetachedFromWindow(holder: ItemViewHolder) {
        if (holder is ItemViewHolder.PlaceHolder) {
            holder.stopLoading()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @LayoutRes viewType: Int
    ): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            VIEW_TYPE_NORMAL -> ItemViewHolder.Normal(
                view = inflater.inflate(R.layout.list_item, parent, false) as ViewGroup
            )
            else-> ItemViewHolder.PlaceHolder(
                view = inflater.inflate(R.layout.list_item, parent, false) as ViewGroup
            )
        }
    }

    sealed class ItemViewHolder(
        itemView: ViewGroup
    ) : RecyclerView.ViewHolder(itemView) {
        class Normal(
            private val view: ViewGroup
        ): ItemViewHolder(view) {
            fun bind(data: DemoData?) {
                data?.let {
                    view.ItemBOuterTextA.text = it.textOne
                    view.ItemBOuterTextB.text = it.textOne
                    view.ItemBInnerTextA.text = it.textTwo
                    view.ItemBInnerTextB.text = it.textThree
                    view.ItemBImage.loadImage(it.imageUrl)
                }
            }
        }

        class PlaceHolder(
            private val view: ViewGroup
        ): ItemViewHolder(view) {
            fun startLoading() {
                Skeleton.buildSkeleton()
                    .forImages(view.ItemBImage)
                    .forText(view.ItemBInnerTextA, dissect = false)
                    .forText(view.ItemBInnerTextB, dissect = false)
                    .forLongText(view.ItemBOuterTextA, dissect = true)
                    .forLongText(view.ItemBOuterTextB, dissect = true)
                    .createIfNeeded(view)
            }

            fun stopLoading() {
                view.disableSkeletonLoading()
            }
        }
    }

    companion object {
        const val VIEW_TYPE_PLACEHOLDER = -1
        const val VIEW_TYPE_NORMAL = 1
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