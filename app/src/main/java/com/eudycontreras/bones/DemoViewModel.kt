package com.eudycontreras.bones

import androidx.lifecycle.*
import androidx.lifecycle.ViewModel

internal class DemoViewModel : ViewModel() {

    private val repository: Repository = Repository(viewModelScope)

    private val dummyData = arrayOfNulls<DemoData>(DUMMY_ENTRY_COUNT)

    val items: LiveData<Resource<List<DemoData?>?>> = repository.getDemoData().map {
        if (it.loading) {
            Resource.Loading(dummyData.toList())
        } else it
    }

    val itemBinding: ItemBinding<DemoData> = { data ->
        when (data) {
            is DemoData.A -> R.layout.list_item_a
            is DemoData.B -> R.layout.list_item_b
            else -> R.layout.list_item_a
        }
    }

    companion object {
        const val DUMMY_ENTRY_COUNT = 12
    }
}
