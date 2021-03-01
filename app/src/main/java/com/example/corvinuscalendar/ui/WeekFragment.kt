package com.example.corvinuscalendar.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corvinuscalendar.R
import com.example.corvinuscalendar.data.DayItem
import kotlinx.android.synthetic.main.fragment_week.*
import java.util.*

private const val ARG_FIRST_DAY_OF_WEEK_EPOCH = "ARG_FIRST_DAY_OF_WEEK_EPOCH"
private const val ARG_FADE_OTHER_MONTH_DAYS = "ARG_FADE_OTHER_MONTH_DAYS"
private const val ARG_CURRENT_MONTH = "ARG_CURRENT_MONTH"

class WeekFragment : Fragment(R.layout.fragment_week) {
    private var firstDayOfWeekEpoch: Long = 0L
    private var fadeOtherMonthDays: Boolean = false
    private var currentMonth: Int = 0
    private val calendarViewModel: CalendarViewModel by viewModels({requireActivity()})

    private lateinit var weekAdapter: WeekAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            firstDayOfWeekEpoch = it.getLong(ARG_FIRST_DAY_OF_WEEK_EPOCH)
            fadeOtherMonthDays = it.getBoolean(ARG_FADE_OTHER_MONTH_DAYS)
            currentMonth = it.getInt(ARG_CURRENT_MONTH)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dayList = createDayItems()
        weekAdapter = WeekAdapter(
            dayList,
            calendarViewModel
        )

        rvWeekDays.adapter = weekAdapter
        rvWeekDays.layoutManager = LinearLayoutManager(
            this.activity, LinearLayoutManager.HORIZONTAL, false)

        calendarViewModel.selectedDay.observe(requireActivity(), Observer { day ->
            if(dayIsInThisWeek(day))
                weekAdapter.notifyDataSetChanged()
        })

        calendarViewModel.previousSelectedDay.observe(requireActivity(), Observer { day ->
            if(dayIsInThisWeek(day))
                weekAdapter.notifyDataSetChanged()
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun dayIsInThisWeek(day: DayItem) : Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = firstDayOfWeekEpoch
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val firstDayOfNextWeekEpoch = calendar.timeInMillis
        if (day.timeEpoch in firstDayOfWeekEpoch until firstDayOfNextWeekEpoch) {
            return true
        }
        return false
    }

    private fun createDayItems() : List<DayItem> {
        val dayCalendar = Calendar.getInstance()
        dayCalendar.timeInMillis = firstDayOfWeekEpoch

        val dayList = mutableListOf<DayItem>()
        repeat(7) {
            val dayMonth = dayCalendar.get(Calendar.MONTH)
            val isOtherMonth = fadeOtherMonthDays && dayMonth != currentMonth
            dayList.add(
                DayItem(
                    dayCalendar.timeInMillis,
                    isOtherMonth
                )
            )
            dayCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dayList.toList()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            firstDayOfWeekEpoch: Long,
            fadeOtherMonthDays: Boolean,
            currentMonth: Int
        ) = WeekFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_FIRST_DAY_OF_WEEK_EPOCH, firstDayOfWeekEpoch)
                    putBoolean(ARG_FADE_OTHER_MONTH_DAYS, fadeOtherMonthDays)
                    putInt(ARG_CURRENT_MONTH, currentMonth)
                }
            }
    }
}