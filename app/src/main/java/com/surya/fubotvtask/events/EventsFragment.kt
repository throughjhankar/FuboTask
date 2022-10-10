package com.surya.fubotvtask.events

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.surya.fubotvtask.R
import com.surya.fubotvtask.utils.gone
import com.surya.fubotvtask.utils.visible

class EventsFragment : Fragment(R.layout.fragment_events) {

    private val viewModel: EventsVM by lazy { ViewModelProvider.NewInstanceFactory().create(EventsVM::class.java) }

    private lateinit var eventRV: RecyclerView
    private lateinit var noEventsFoundTV: TextView
    private lateinit var loadingPB: ProgressBar
    private lateinit var addEventFAB: FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupListeners()
        viewModel.currentState.observe(viewLifecycleOwner, Observer(::reactToState))
        viewModel.onViewCreated(requireContext())
    }

    private fun initViews() {
        view?.apply {
            eventRV = findViewById(R.id.rv_events)
            noEventsFoundTV = findViewById(R.id.tv_no_events_found)
            loadingPB = findViewById(R.id.pb_loading)
            addEventFAB = findViewById(R.id.fab_add_events)
        }
    }

    private fun setupListeners() {
        addEventFAB.setOnClickListener {
            viewModel.onAddNewEventTapped(findNavController())
        }
    }

    private fun reactToState(state: EventsScreenState) {
        when(state) {
            is EventsScreenState.NoData -> {
                handleNoDataState()
            }
            is EventsScreenState.Data -> {
                handleDataState(state)
            }
            is EventsScreenState.Loading -> {
                handleLoadingState()
            }
        }
    }

    private fun handleNoDataState() {
        eventRV.gone()
        loadingPB.gone()
        noEventsFoundTV.visible()
    }

    private fun handleDataState(state: EventsScreenState.Data) {
        noEventsFoundTV.gone()
        loadingPB.gone()
        eventRV.visible()

        val adapter = EventsAdapter(state.listOfEvents)
        eventRV.adapter = adapter
    }

    private fun handleLoadingState() {
        noEventsFoundTV.gone()
        loadingPB.visible()
        eventRV.gone()
    }
}
