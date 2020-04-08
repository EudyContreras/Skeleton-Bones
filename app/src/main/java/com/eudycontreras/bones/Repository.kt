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

@ExperimentalCoroutinesApi
class Repository(scope: CoroutineScope) {

    private val database: DataBase = DataBase()

    private val demoOneData = MutableLiveData(Resource<DemoData.One>())

    private val demoTwoData = MutableLiveData(Resource<DemoData.Two>())

    private val demoThreeData = MutableLiveData(Resource<DemoData.Three>())

    fun getDemoDataOne(): LiveData<Resource<DemoData.One>> = demoOneData


    fun getDemoDataTwo(): LiveData<Resource<DemoData.Two>> = demoTwoData


    fun getDemoDataThree(): LiveData<Resource<DemoData.Three>> = demoThreeData

    init {
        scope.launch(Dispatchers.Main) {
            val dataOne = DemoData.One(
                textOne = database.textOne,
                textTwo = database.textTwo,
                imageUrl = database.urlMaleAvatar
            )
            val dataTwo = DemoData.Two(
                text = database.textOne,
                imageOneUrl = database.urlMaleAvatar,
                imageTwoUrl = database.urlFemaleAvatar
            )
            val dataThree = DemoData.Three(
                text = database.textOne,
                imageUrl = database.urlMaleAvatar
            )

            while (true) {
                delay(LONG_DELAY)
                demoOneData.postValue(Resource(dataOne, false))
                delay(SHORT_DELAY)
                demoTwoData.postValue(Resource(dataTwo, false))
                delay(SHORT_DELAY)
                demoThreeData.postValue(Resource(dataThree, false))

                delay(SHORT_DELAY)
                demoOneData.postValue(Resource(null, true))
                demoTwoData.postValue(Resource(null, true))
                demoThreeData.postValue(Resource(null, true))
            }
        }
    }

    companion object {
        const val SHORT_DELAY = 1500L
        const val LONG_DELAY = 3500L
    }
}