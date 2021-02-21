package com.eudycontreras.bones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import kotlinx.android.synthetic.main.activity_main.*

internal class MainActivity : AppCompatActivity() {

    private val repository: Repository by lazy { Repository(lifecycleScope) }

    private val dummyData = arrayOfNulls<DemoData>(Database.ENTRY_COUNT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items: LiveData<Resource<List<DemoData?>?>> = repository.getDemoData().map {
            if (it.loading) {
                Resource.Loading(dummyData.toList())
            } else it
        }
        items.observeForever {
            recyclerView.setItemData(it)
        }
    }
}
