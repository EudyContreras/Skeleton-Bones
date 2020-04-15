package com.eudycontreras.bones

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope

internal class DemoViewModel : ViewModel() {

    private val repository: Repository = Repository(viewModelScope)

    private val dummyData = arrayOfNulls<DemoData>(Database.ENTRY_COUNT)

    val items: LiveData<Resource<List<DemoData?>?>> = repository.getDemoData().map {
        if (it.loading) {
            Resource.Loading(dummyData.toList())
        } else it
    }

    val itemBinding: ItemBinding<DemoData> = { data, position ->
        when (data) {
            is DemoData.A -> R.layout.list_item_a
            is DemoData.B -> R.layout.list_item_b
            else -> if (position % 2 == 0) { R.layout.list_item_a } else R.layout.list_item_b
        }
    }
}
