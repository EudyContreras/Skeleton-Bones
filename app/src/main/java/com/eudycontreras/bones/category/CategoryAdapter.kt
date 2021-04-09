package com.eudycontreras.bones.category

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eudycontreras.bones.category.rows.CategoryCellDelegate
import com.eudycontreras.bones.extensions.toBinding
import com.eudycontreras.bones.shared.BaseAdapter
import com.eudycontreras.bones.category.rows.CategoryRowType
import com.eudycontreras.bones.category.rows.CategoryViewHolder
import com.eudycontreras.bones.category.rows.CategoryViewModel

class CategoryAdapter(var categoryCellDelegate: CategoryCellDelegate) : BaseAdapter() {

    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is CategoryViewModel -> {
                CategoryRowType.Category.viewType
            }
            else -> throw NotImplementedError("Not implemented for ViewModel of type ${item.javaClass}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CategoryRowType.Category.viewType -> {
                CategoryViewHolder(parent.toBinding())
            }
            else -> throw NotImplementedError("Not implemented for viewType $viewType")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val viewModel = items[position]
        (viewHolder as? CategoryViewHolder)?.bind(
            viewModel as CategoryViewModel,
            categoryCellDelegate
        )
    }
}
