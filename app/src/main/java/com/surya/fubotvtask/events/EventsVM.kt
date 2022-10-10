package com.surya.fubotvtask.events

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.surya.fubotvtask.R
import com.surya.fubotvtask.database.AppDatabase
import com.surya.fubotvtask.utils.getTime
import java.time.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.java.KoinJavaComponent.inject

class EventsVM : ViewModel() {

    private val vmIoScope = viewModelScope + Dispatchers.IO
    private val _currentState: MutableLiveData<EventsScreenState> =
        MutableLiveData(EventsScreenState.Loading)
    val currentState: LiveData<EventsScreenState> = _currentState
    private val database: AppDatabase by inject(AppDatabase::class.java)

    fun onViewCreated(context: Context) {
        vmIoScope.launch {
            val eventDao = database.eventDao()
            val events = eventDao.getAllEvents()
            if (events.isNotEmpty()) {
                val eventsList = arrayListOf<Event>()
                events.forEach {
                    val startTime = it.startDateTime.getTime()
                    val endTime = it.endDateTime.getTime()
                    val durationMin =
                        Duration.between(it.startDateTime, it.endDateTime).seconds / 60
                    val details = context.getString(
                        R.string.meeting_details,
                        it.eventTitle,
                        startTime,
                        endTime,
                        durationMin
                    )

                    val header =
                        eventsList.find { header -> header is Event.Header && header.eventDate.monthValue == it.startDateTime.monthValue && header.eventDate.dayOfMonth == it.startDateTime.dayOfMonth }
                    if (header == null) {
                        eventsList.add(
                            Event.Header(it.startDateTime)
                        )
                    }
                    eventsList.add(Event.Data(details))
                }
                _currentState.postValue(EventsScreenState.Data(eventsList))
            } else {
                _currentState.postValue(EventsScreenState.NoData)
            }
        }
    }

    fun onAddNewEventTapped(navController: NavController) {
        navController.navigate(R.id.action_eventsFragment_to_addEventFragment)
    }
}

sealed class EventsScreenState {
    object NoData : EventsScreenState()
    object Loading : EventsScreenState()
    data class Data(val listOfEvents: List<Event>) : EventsScreenState()
}
