package com.eudycontreras.bones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since February 2021
 */

class Repository(scope: CoroutineScope) {

    private val database: Database = Database()

    private val demoData: MutableLiveData<Resource<List<DemoData>>> = MutableLiveData(Resource.Loading())

    fun getDemoData(): LiveData<Resource<List<DemoData>>> = demoData

    init {
        scope.launch(Dispatchers.Main) {
            demoData.postValue(Resource.Loading())

            val dataCollection = List(Database.ENTRY_COUNT) {
                DemoData(
                    id = it.toString(),
                    textOne = database.textOne,
                    textTwo = database.textTwo,
                    textThree = database.textThree,
                    imageUrl = database.urlAvatar
                )
            }

            delay(SHORT_DELAY)
            demoData.postValue(Resource.Success(dataCollection))

            delay(SHORT_DELAY)
            demoData.postValue(Resource.Loading(dataCollection))

            delay(SHORT_DELAY)
            demoData.postValue(Resource.Success(dataCollection))
        }
    }

    companion object {
        const val SHORT_DELAY = 2500L
        const val LONG_DELAY = 4000L
    }
}