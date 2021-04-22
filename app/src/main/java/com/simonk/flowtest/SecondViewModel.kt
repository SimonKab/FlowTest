package com.simonk.flowtest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SecondViewModel : ViewModel() {

    val stateFlow = MutableStateFlow("")
    val customFastFlow = WorkerToMainThread<String>()

    val liveData = MutableLiveData<String>()

    val plainFlow = flow {
        emit("Plain flow text")
    }.flowOn(Dispatchers.IO)

    fun onCreate() {
        viewModelScope.launch(Dispatchers.IO) {
            liveData.postValue("Live data text")
        }
        viewModelScope.launch {
            val anotherFlow = flowOf("Custom fast text").flowOn(Dispatchers.IO)
            customFastFlow.emitAll(anotherFlow)
        }
        viewModelScope.launch {
            stateFlow.emitAll(flowOf("MutableStateFlow text").flowOn(Dispatchers.IO))
        }
    }

    fun start() {

    }

}