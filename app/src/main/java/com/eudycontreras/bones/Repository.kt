package com.eudycontreras.bones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

/**
 * Copyright (C) 2020 Project X
 *
 * @Project ProjectX
 * @author Eudy Contreras.
 * @since April 2020
 */

class Repository(scope: CoroutineScope) {

    private val database: Database = Database()

    private val demoData: MutableLiveData<Resource<List<DemoData?>?>> = MutableLiveData(Resource.Loading())

    fun getDemoData(): LiveData<Resource<List<DemoData?>?>> = demoData

    init {
        scope.launch(Dispatchers.Main) {
            demoData.postValue(Resource.Loading())

            val dataCollection = List(Database.ENTRY_COUNT) {
                when {
                    it % 2 == 0 -> {
                        DemoData.A(
                            id = it.toString(),
                            text = database.textOne,
                            imageUrl = database.urlMaleAvatar
                        )
                    }
                    else -> {
                        DemoData.B(
                            id = it.toString(),
                            textOne = database.textOne,
                            textTwo = database.textTwo,
                            textThree = database.textThree,
                            imageUrl = database.urlFemaleAvatar
                        )
                    }
                }
            }

            while (true) {
                delay(SHORT_DELAY)
                demoData.postValue(Resource.Success(dataCollection))

                delay(LONG_DELAY)
                demoData.postValue(Resource.Loading())
            }
        }
    }

    companion object {
        const val SHORT_DELAY = 3500L
        const val LONG_DELAY = 3500L
    }
}