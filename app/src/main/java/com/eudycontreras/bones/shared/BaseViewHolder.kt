package com.eudycontreras.bones.shared

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent) {
    abstract fun onViewRecycled()
}
