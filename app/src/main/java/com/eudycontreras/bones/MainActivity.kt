package com.eudycontreras.bones

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.eudycontreras.bones.databinding.ActivityMainBinding

internal class MainActivity : AppCompatActivity() {

    private val demoViewModel: DemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.also {
            it.lifecycleOwner = this
            it.viewModel = demoViewModel
        }
    }
}
