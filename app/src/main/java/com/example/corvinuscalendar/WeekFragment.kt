package com.example.corvinuscalendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_week.*
import java.util.*

private const val TAG = "WeekFragment"
private const val ARG_FIRST_DAY_OF_WEEK_EPOCH = "ARG_FIRST_DAY_OF_WEEK_EPOCH"
private const val ARG_FADE_OTHER_MONTH_DAYS = "ARG_FADE_OTHER_MONTH_DAYS"
private const val ARG_CURRENT_MONTH = "ARG_CURRENT_MONTH"

class WeekFragment : Fragment(R.layout.fragment_week) {
    private var firstDayOfWeekEpoch: Long = 0L
    private var fadeOtherMonthDays: Boolean = false
    private var currentMonth: Int = 0

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
        weekAdapter = WeekAdapter(dayList)

        rvWeekDays.adapter = weekAdapter
        rvWeekDays.layoutManager = LinearLayoutManager(
            this.activity, LinearLayoutManager.HORIZONTAL, false)

        super.onViewCreated(view, savedInstanceState)
    }

    private fun createDayItems() : List<DayItem> {
        val dayCalendar = Calendar.getInstance()
        dayCalendar.timeInMillis = firstDayOfWeekEpoch

        val dayList = mutableListOf<DayItem>()
        repeat(7) {
            val dayMonth = dayCalendar.get(Calendar.MONTH)
            val isOtherMonth = fadeOtherMonthDays && dayMonth != currentMonth
            dayList.add(DayItem(dayCalendar.timeInMillis, isOtherMonth))
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