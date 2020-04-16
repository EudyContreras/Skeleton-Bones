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

    private val database: DataBase = DataBase()

    private val demoOneData: MutableLiveData<Resource<DemoData.One>> = MutableLiveData(Resource.Loading())

    private val demoTwoData: MutableLiveData<Resource<DemoData.Two>> = MutableLiveData(Resource.Loading())

    private val demoThreeData: MutableLiveData<Resource<DemoData.Three>> = MutableLiveData(Resource.Loading())

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
                demoOneData.postValue(Resource.Success(dataOne))
                delay(SHORT_DELAY)
                demoTwoData.postValue(Resource.Success(dataTwo))
                delay(SHORT_DELAY)
                demoThreeData.postValue(Resource.Success(dataThree))

                delay(SHORT_DELAY)
                demoOneData.postValue(Resource.Loading())
                demoTwoData.postValue(Resource.Loading())
                demoThreeData.postValue(Resource.Loading())
            }
        }
    }

    companion object {
        const val SHORT_DELAY = 1500L
        const val LONG_DELAY = 3500L
    }
}