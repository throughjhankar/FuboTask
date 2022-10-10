package com.surya.fubotvtask.addevent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.surya.fubotvtask.R
import com.surya.fubotvtask.utils.getFormattedTime
import com.surya.fubotvtask.utils.showOnToast
import java.util.Calendar

class AddEventFragment : Fragment(R.layout.fragment_add_event) {

    private val viewModel: AddEventVM by lazy {
        ViewModelProvider.NewInstanceFactory().create(AddEventVM::class.java)
    }

    private lateinit var meetingNameET: EditText
    private lateinit var meetingDateET: EditText
    private lateinit var startTimeET: EditText
    private lateinit var endTimeET: EditText
    private lateinit var meetingDateView: View
    private lateinit var startTimeView: View
    private lateinit var endTimeView: View
    private lateinit var createButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupListeners()
        viewModel.currentState.observe(viewLifecycleOwner, Observer(::reactToState))
    }

    private fun initViews() {
        view?.apply {
            meetingNameET = findViewById(R.id.et_meeting_name)
            meetingDateET = findViewById(R.id.et_meeting_date)
            startTimeET = findViewById(R.id.et_start_date_time)
            endTimeET = findViewById(R.id.et_end_date_time)
            meetingDateView = findViewById(R.id.view_meeting_date)
            startTimeView = findViewById(R.id.view_start_time)
            endTimeView = findViewById(R.id.view_end_time)
            createButton = findViewById(R.id.button_create)
        }
    }

    private fun setupListeners() {

        val calender = Calendar.getInstance()
        val hourOfDay: Int = calender.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calender.get(Calendar.MINUTE)

        meetingDateView.setOnClickListener {
            val year: Int = calender.get(Calendar.YEAR)
            val month: Int = calender.get(Calendar.MONTH)
            val dayOfMonth: Int = calender.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireActivity(),
                { _, p1, p2, p3 ->
                    meetingDateET.setText(getString(R.string.meeting_date_template, p3, p2, p1))
                    viewModel.onMeetingDateSelected(p3, p2, p1)
                }, year, month, dayOfMonth
            )

            datePickerDialog.show()
        }

        startTimeView.setOnClickListener {
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, p1, p2 ->
                    startTimeET.setText(getFormattedTime(p1, p2))
                    viewModel.onMeetingStartTimeSelected(p1, p2)
                },
                hourOfDay,
                minute,
                false
            )
            timePicker.show()
        }

        endTimeView.setOnClickListener {
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, p1, p2 ->
                    endTimeET.setText(getFormattedTime(p1, p2))
                    viewModel.onMeetingEndTimeSelected(p1, p2)
                },
                hourOfDay,
                minute,
                false
            )
            timePicker.show()
        }

        createButton.setOnClickListener {
            viewModel.onCreateEventTapped(
                requireContext(),
                meetingNameET.text.toString()
            )
        }
    }

    private fun reactToState(state: AddEventScreenState) {
        when(state) {
            is AddEventScreenState.AcceptInput -> {
                // Default action is already handled
            }
            is AddEventScreenState.Error -> {
                handleErrorState(state)
            }
            is AddEventScreenState.EventAdded -> {
                handleEventAddedState()
            }
        }
    }

    private fun handleErrorState(state: AddEventScreenState.Error) {
        state.errorMessage.showOnToast(requireContext())
    }

    private fun handleEventAddedState() {
        getString(R.string.msg_event_created).showOnToast(requireContext())
    }
}
