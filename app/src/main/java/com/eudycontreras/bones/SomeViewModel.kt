package com.eudycontreras.bones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class SomeViewModel : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(true)
    private val _loadingAlt: MutableLiveData<Boolean> = MutableLiveData(true)
    private val _loadingAlt2: MutableLiveData<Boolean> = MutableLiveData(true)
    private val _loadingAlt3: MutableLiveData<Boolean> = MutableLiveData(true)

    val loading: LiveData<Boolean>
        get() = _loading

    val loadingAlt: LiveData<Boolean>
        get() = _loadingAlt

    val loadingAlt2: LiveData<Boolean>
        get() = _loadingAlt2

    val loadingAlt3: LiveData<Boolean>
        get() = _loadingAlt3

    init {
        viewModelScope.launch {
            var state = true
            while (true) {
                if (state) {
                    state = !state
                    delay(ADDED_DELAY * 3)
                    _loadingAlt2.postValue(false)
                    delay(ADDED_DELAY * 2)
                    _loadingAlt3.postValue(false)
                    delay(NORMAL_DELAY)
                    _loading.postValue(state)
                    delay(ADDED_DELAY)
                    _loadingAlt.postValue(state)
                } else {
                    state = !state
                    delay(NORMAL_DELAY)
                    _loading.postValue(state)
                    _loadingAlt.postValue(state)
                }
            }
        }
    }

    private companion object {
        const val NORMAL_DELAY = 2500L
        const val ADDED_DELAY = 600L
    }
}
