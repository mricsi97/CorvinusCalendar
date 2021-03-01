package com.example.corvinuscalendar.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.corvinuscalendar.R
import com.example.corvinuscalendar.data.DayItem
import kotlinx.android.synthetic.main.item_week.view.*
import java.text.SimpleDateFormat
import java.util.*

class WeekAdapter(
    private val days: List<DayItem>,
    private val calendarViewModel: CalendarViewModel
) : RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    private val dateFormatter = SimpleDateFormat("dd", Locale.ROOT)

    inner class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val day = days[adapterPosition]
                    val selectedDay = calendarViewModel.selectedDay.value
                    if(selectedDay != null) {
                        calendarViewModel.previousSelectedDay.value = selectedDay
                    }
                    calendarViewModel.selectedDay.value = day
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        return WeekViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_week,
            parent,
            false))
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val day = days[position]

        holder.itemView.apply {
            tvDayOfMonth_weekItem.text = dateFormatter.format(day.timeEpoch)
            if (day.isOtherMonth) {
                tvDayOfMonth_weekItem.setTextColor(ContextCompat.getColor(context, R.color.colorGray))
            } else {
                tvDayOfMonth_weekItem.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
            }

            if (day.timeEpoch == calendarViewModel.selectedDay.value?.timeEpoch) {
                weekItemLayout.background = ContextCompat.getDrawable(context, R.drawable.shape_round_primarydarker)
            } else {
                weekItemLayout.background = ContextCompat.getDrawable(context, R.color.colorPrimaryDark)
            }
        }


    }
}