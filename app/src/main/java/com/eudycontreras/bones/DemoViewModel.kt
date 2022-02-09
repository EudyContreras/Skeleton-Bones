package com.eudycontreras.bones

import android.os.StatFs
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since February 2021
 */

class DemoViewModel: ViewModel() {

    private val database: Database = Database()

    fun getDemoData(): StateFlow<Resource<List<DemoData>>> {
        val demoData: MutableStateFlow<Resource<List<DemoData>>> = MutableStateFlow(Resource.Loading())

        /**
         * Simulate fetching from network
         */
        viewModelScope.launch(Dispatchers.Main) {
            while(true) {
                demoData.emit(Resource.Loading())
                delay(SHORT_DELAY)
                val dataCollection = List(Database.ENTRY_COUNT) {
                    DemoData(
                        id = it.toString(),
                        textOne = database.textOne,
                        textTwo = database.textTwo,
                        textThree = database.textThree,
                        imageUrl = database.urlFemaleAvatar
                    )
                }
                demoData.emit(Resource.Success(dataCollection))
                delay(SHORT_DELAY)
            }
        }
        return demoData
    }

    companion object {
        const val SHORT_DELAY = 3500L
    }
}