package com.simonk.flowtest

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect


inline fun <T> Fragment.collectFlow(flow: Flow<T>, crossinline action: suspend (value: T) -> Unit) {
    viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
        flow.collect(action)
    }
}

inline fun <T> ComponentActivity.collectFlow(flow: Flow<T>, crossinline action: suspend (value: T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        flow.collect(action)
    }
}

inline fun <T> Fragment.collectFlow(channelFlowWrapper: ChannelFlowWrapper<T>, crossinline action: suspend (value: T) -> Unit) {
    collectFlow(channelFlowWrapper.asFlow(), action)
}

inline fun <T> ComponentActivity.collectFlow(channelFlowWrapper: ChannelFlowWrapper<T>, crossinline action: suspend (value: T) -> Unit) {
    collectFlow(channelFlowWrapper.asFlow(), action)
}