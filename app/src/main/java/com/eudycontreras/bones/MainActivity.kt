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
            demoViewModel.getDemoData().collect {
                when(it) {
                    is Resource.Loading -> recyclerView.setItemData(it.cache ?: dummyData.toList())
                    is Resource.Success -> recyclerView.setItemData(it.data)
                    is Resource.Failure -> TODO("Show some error screen")
                }
            }
        }
    }
}
