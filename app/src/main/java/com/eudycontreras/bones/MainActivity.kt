package com.eudycontreras.bones

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.eudycontreras.bones.databinding.MainBinding

internal class MainActivity : AppCompatActivity() {

    private val viewModel: SomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: MainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        /**
         * Creation of skeletonDrawable that gets directly added to a ViewGroup
         * The drawable is manipulated by changing its mutable properties. The properties
         * are accessible after the loader has been added
         *
         *  val viewGroup: ViewGroup = getContainer()
         *
         *  viewGroup.addSkeletonLoader().apply {
         *      this.enabled = false
         *  }
         *
         *  SkeletonDrawable.create(internal).apply {
         *      this.enabled = false
         *  }
         */

        binding.also {
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
    }
}
