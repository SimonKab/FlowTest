package com.simonk.flowtest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.withContext

open class ChannelFlowWrapper<T>(capacity: Int = Channel.RENDEZVOUS,
                                 onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND) {

    private val undeliveredElements = mutableListOf<T>()
    protected val channel = Channel<T>(capacity, onBufferOverflow) {
        undeliveredElements.add(it)
    }

    fun asFlow() = flow {
        emitAll(undeliveredElements.asFlow())
        undeliveredElements.clear()
        for (value in channel) {
            emit(value)
        }
    }

    open suspend fun emit(value: T) {
        channel.send(value)
    }
}

class SingleEvent<T> : ChannelFlowWrapper<T>(1, BufferOverflow.DROP_OLDEST)

class WorkerToMainThread<T> : ChannelFlowWrapper<T>(Channel.UNLIMITED) {

    override suspend fun emit(value: T) {
        emitAll(flowOf(value))
    }

    suspend fun emitAll(workerFlow: Flow<T>) {
        withContext(Dispatchers.IO) {
            workerFlow.collect {
                channel.send(it)
            }
        }
    }

}