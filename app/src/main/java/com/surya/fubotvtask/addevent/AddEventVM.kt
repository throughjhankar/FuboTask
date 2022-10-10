package com.surya.fubotvtask.addevent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.surya.fubotvtask.R
import com.surya.fubotvtask.database.AppDatabase
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.java.KoinJavaComponent

class AddEventVM : ViewModel() {

    private val vmIoScope = viewModelScope + Dispatchers.IO
    private val _currentState: MutableLiveData<AddEventScreenState> =
        MutableLiveData(AddEventScreenState.AcceptInput)
    val currentState: LiveData<AddEventScreenState> = _currentState
    private val database: AppDatabase by KoinJavaComponent.inject(AppDatabase::class.java)

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    private var meetingStartDateTime: LocalDateTime? = null
    private var meetingEndDateTime: LocalDateTime? = null

    fun onMeetingDateSelected(day: Int, month: Int, year: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    fun onMeetingStartTimeSelected(hour: Int, minute: Int) {
        meetingStartDateTime = LocalDateTime.of(year, month, day, hour, minute)
    }

    fun onMeetingEndTimeSelected(hour: Int, minute: Int) {
        meetingEndDateTime = LocalDateTime.of(year, month, day, hour, minute)
    }

    fun onCreateEventTapped(context: Context, meetingTitle: String) {
        when {
            meetingTitle.isEmpty() || meetingTitle.isBlank() -> {
                _currentState.postValue(AddEventScreenState.Error(context.getString(R.string.err_meeting_title_missing)))
            }
            meetingStartDateTime == null -> {
                _currentState.postValue(AddEventScreenState.Error(context.getString(R.string.err_meeting_start_time_missing)))
            }
            meetingEndDateTime == null -> {
                _currentState.postValue(AddEventScreenState.Error(context.getString(R.string.err_meeting_end_time_missing)))
            }
            meetingEndDateTime!!.isBefore(meetingStartDateTime) -> {
                _currentState.postValue(AddEventScreenState.Error(context.getString(R.string.err_meeting_end_time_before_start_time)))
            }
            else -> {
                vmIoScope.launch {
                    database.eventDao().insertEvent(com.surya.fubotvtask.database.Event(
                        UUID.randomUUID(),
                        meetingTitle,
                        meetingStartDateTime ?: throw IllegalArgumentException("meetingStartDateTime cannot be null"),
                        meetingEndDateTime ?: throw IllegalArgumentException("meetingEndDateTime cannot be null")
                    ))
                    _currentState.postValue(AddEventScreenState.EventAdded)
                }
            }
        }
    }
}

sealed class AddEventScreenState {
    object AcceptInput : AddEventScreenState()
    object EventAdded : AddEventScreenState()
    data class Error(val errorMessage: String) : AddEventScreenState()
}
