package com.eudycontreras.bones.shared

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var items = emptyList<BaseViewModel>()
        private set

    open fun setItems(items: List<BaseViewModel>) {
        this.items = items
    }

    init {
        @Suppress("LeakingThis")
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val baseViewModel = items[position]
        return baseViewModel.hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun refreshList(recyclerView: RecyclerView?, dataSource: BaseDataSource) {
        if (recyclerView == null) { // To avoid crashes when call after fragment is finished
            return
        }

        val items = dataSource.buildViewModels()
        recyclerView.post {
            setItems(items)
            notifyDataSetChanged()
        }
    }

    override fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        (viewHolder as? BaseViewHolder)?.onViewRecycled()
        super.onViewRecycled(viewHolder)
    }
}
