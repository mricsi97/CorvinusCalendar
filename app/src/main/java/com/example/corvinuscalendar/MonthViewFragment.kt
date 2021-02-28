package com.example.corvinuscalendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import java.util.*

private const val TAG = "MonthViewFragment"
private const val ARG_FIRST_DAY_OF_MONTH_EPOCH = "ARG_FIRST_DAY_OF_MONTH_EPOCH"

class MonthViewFragment : Fragment(R.layout.fragment_month) {
    private var firstDayOfMonthEpoch: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            firstDayOfMonthEpoch = it.getLong(ARG_FIRST_DAY_OF_MONTH_EPOCH)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set calendar to first day of the month shown
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = firstDayOfMonthEpoch
        val currentMonth = calendar.get(Calendar.MONTH)
        // Get week column of the first day of the month
        val firstDayOfMonthColumn = calendar.get(Calendar.DAY_OF_WEEK) - 2
        // Move calendar to the first day of the week
        calendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonthColumn)

        repeat(6) {
            // Create new week row
            val weekFragment = WeekFragment.newInstance(
                calendar.timeInMillis,
                true,
                currentMonth)
            // Add the week row to the layout
            childFragmentManager.beginTransaction()
                .add(R.id.weekFragmentContainer, weekFragment)
                .commit()
            // Jump to next week
            calendar.add(Calendar.DAY_OF_MONTH, 7)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(firstDayOfMonthEpoch: Long) =
            MonthViewFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_FIRST_DAY_OF_MONTH_EPOCH, firstDayOfMonthEpoch)
                }
            }
    }
}