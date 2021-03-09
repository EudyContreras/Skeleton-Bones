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

        val items: LiveData<List<DemoData?>> = repository.getDemoData().map {
            when (it) {
                is Resource.Loading -> dummyData.toList()
                is Resource.Success -> it.data
            }
        }

        items.observeForever {
            recyclerView.setItemData(it)
        }
    }
}
