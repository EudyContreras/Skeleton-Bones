package com.eudycontreras.bones

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since April 2020
 */

typealias ItemBinding <T> = (T?) -> Int

class ItemAdapter<T : DiffComparable>(
    private var data: List<T?>,
    private val itemBinding: ItemBinding<T>
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

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ItemViewHolder<T>, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemViewType(position: Int): Int {
        val item: T? = this.data[position]
        return itemBinding.invoke(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): ItemViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(DataBindingUtil.inflate(inflater, viewType, parent, false))
    }

    class ItemViewHolder<T: DiffComparable>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: T?) {
            binding.setVariable(BR.viewModel, data)
            binding.executePendingBindings()
        }
    }
}

class DiffCallback<T: DiffComparable>(
    private var oldItems: List<T?>,
    private var newItems: List<T?>
): DiffUtil.Callback() {

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