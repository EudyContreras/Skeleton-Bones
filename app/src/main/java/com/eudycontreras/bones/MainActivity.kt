package com.eudycontreras.bones

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect

internal class MainActivity : AppCompatActivity() {

    private val demoViewModel: DemoViewModel by viewModels()
    private val dummyData = arrayOfNulls<DemoData>(Database.ENTRY_COUNT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenResumed {
            val adapter = ItemAdapter(lifecycleScope, emptyList())
            recyclerView.adapter = adapter
            demoViewModel.getDemoData().collect {
                when(it) {
                    is Resource.Loading -> adapter.updateData(it.cache ?: dummyData.toList())
                    is Resource.Success -> adapter.updateData(it.data)
                    is Resource.Failure -> TODO("Show some error screen")
                }
            }
        }
    }
}
