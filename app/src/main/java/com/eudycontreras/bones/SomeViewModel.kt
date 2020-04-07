package com.eudycontreras.bones

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal class SomeViewModel : ViewModel() {

    private val repository: Repository = Repository(viewModelScope)

    val demoDataOne: LiveData<Resource<DemoData.One>> = repository.getDemoDataOne()

    val demoDataTwo: LiveData<Resource<DemoData.Two>> = repository.getDemoDataTwo()

    val demoDataThree: LiveData<Resource<DemoData.Three>> = repository.getDemoDataThree()
}
