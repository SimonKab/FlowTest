package com.simonk.flowtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

class FirstFragment : Fragment(R.layout.fragment_one) {

    private val viewModel: FirstViewModel by viewModels()

    private var incorrectCollectedCount = 0
    private var correctCollectedCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incorrectCollectedCount = 0
        correctCollectedCount = 0

        initListeners(view)
        observeViewModel()

        viewModel.onViewCreated()
    }

    private fun initListeners(view: View) {
        view.findViewById<Button>(R.id.nextLifecycle).setOnClickListener {
            viewModel.onNextLiveData()
        }
        view.findViewById<Button>(R.id.nextMutableSharedFlow).setOnClickListener {
            viewModel.onNextMutableShared()
        }
        view.findViewById<Button>(R.id.nextSingleEvent).setOnClickListener {
            viewModel.onNextSingleEvent()
        }
        view.findViewById<Button>(R.id.next).setOnClickListener {
            (activity as MainActivity).openSecondFragment()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.incorrectObservedTextFlow.collect {
                incorrectCollectedCount++
                view?.findViewById<TextView>(R.id.tvIncorrectCollected)?.text = it + incorrectCollectedCount
            }
        }
        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.correctObservedTextFlow.collect {
                correctCollectedCount++
                view?.findViewById<TextView>(R.id.tvCorrectCollected)?.text = it + correctCollectedCount
            }
        }

        viewModel.nextLiveData.observe(viewLifecycleOwner, {
            (activity as MainActivity).openSecondFragment()
        })
        collectFlow(viewModel.nextMutableSharedFlow) {
            Log.d("TEST", "Open after Mutable Shared")
            (activity as MainActivity).openSecondFragment()
        }
        collectFlow(viewModel.nextSingleEvent) {
            Log.d("TEST", "Open after single event")
            (activity as MainActivity).openSecondFragment()
        }
    }

}