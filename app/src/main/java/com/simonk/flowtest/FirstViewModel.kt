package com.simonk.flowtest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.launch

class FirstViewModel : ViewModel() {

    val nextLiveData = MutableLiveData<Unit>()
    val nextMutableSharedFlow = MutableSharedFlow<Unit>()
    val nextSingleEvent = SingleEvent<Unit>()

    val incorrectObservedTextFlow = MutableSharedFlow<String>()
    val correctObservedTextFlow = MutableSharedFlow<String>()

    fun onViewCreated() {
        viewModelScope.launch {
            delay(1000)
            incorrectObservedTextFlow.emit("Incorrect flow. Collected: ")
            correctObservedTextFlow.emit("Correct flow. Collected: ")
        }
    }

    fun onNextLiveData() {
        nextLiveData.value = Unit
    }

    fun onNextMutableShared() {
        viewModelScope.launch {
            delay(3000)
            Log.d("TEST", "Emit mutable shared")
            nextMutableSharedFlow.emit(Unit)
        }
    }

    fun onNextSingleEvent() {
        viewModelScope.launch {
            Log.d("TEST", "Emit single event")
            nextSingleEvent.emit(Unit)
        }
    }

}