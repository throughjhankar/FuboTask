package com.surya.fubotvtask.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.surya.fubotvtask.R
import java.time.format.DateTimeFormatter

class EventsAdapter(private val listOfEvents: List<Event>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_event_header, parent, false)
                HeaderViewHolder(itemView)
            }
            TYPE_ITEM -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_event_data, parent, false)
                ItemViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEADER -> bindHeaderViewHolder(
                holder as HeaderViewHolder,
                listOfEvents[position] as Event.Header
            )
            TYPE_ITEM -> bindItemViewHolder(
                holder as ItemViewHolder,
                listOfEvents[position] as Event.Data
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (listOfEvents[position]) {
            is Event.Header -> TYPE_HEADER
            is Event.Data -> TYPE_ITEM
        }
    }

    override fun getItemCount(): Int = listOfEvents.size

    private fun bindHeaderViewHolder(holder: HeaderViewHolder, header: Event.Header) {
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        holder.headerTV.text = header.eventDate.format(formatter)
    }

    private fun bindItemViewHolder(holder: ItemViewHolder, data: Event.Data) {
        holder.dataTV.text = data.meetingDetails
    }
}

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val headerTV: TextView = itemView.findViewById(R.id.tv_header)
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dataTV: TextView = itemView.findViewById(R.id.tv_data)
}

private const val TYPE_HEADER = 0
private const val TYPE_ITEM = 1
