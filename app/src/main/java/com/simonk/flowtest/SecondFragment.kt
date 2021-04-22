package com.simonk.flowtest

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.collect

class SecondFragment : Fragment(R.layout.fragment_two) {

    private val viewModel: SecondViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlow(viewModel.customFastFlow) {
            view.findViewById<TextView>(R.id.customFastFlow).text = it
        }
        collectFlow(viewModel.stateFlow) {
            view.findViewById<TextView>(R.id.stateFlow).text = it
        }
        collectFlow(viewModel.plainFlow) {
            view.findViewById<TextView>(R.id.plainFlow).text = it
        }

        viewModel.liveData.observe(viewLifecycleOwner, {
            Thread.sleep(1000)
            view.findViewById<TextView>(R.id.liveData).text = it
        })

        viewModel.start()
    }

}