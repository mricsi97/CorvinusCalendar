package com.example.corvinuscalendar.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.corvinuscalendar.BuildConfig
import com.example.corvinuscalendar.R
import com.example.corvinuscalendar.data.DayItem
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
    private val calendarViewModel: CalendarViewModel by viewModels({ requireActivity() })

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
        weekAdapter = WeekAdapter(dayList, calendarViewModel)

        rvWeekDays.adapter = weekAdapter
        rvWeekDays.layoutManager = LinearLayoutManager(
            this.activity, LinearLayoutManager.HORIZONTAL, false
        )

        calendarViewModel.selectedDay.observe(requireActivity(), Observer { day ->
            if (dayIsInThisWeek(day))
                weekAdapter.notifyDataSetChanged()
        })

        calendarViewModel.previousSelectedDay.observe(requireActivity(), Observer { day ->
            if (dayIsInThisWeek(day))
                weekAdapter.notifyDataSetChanged()
        })

        getWeekEvents()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun dayIsInThisWeek(day: DayItem): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = firstDayOfWeekEpoch
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val firstDayOfNextWeekEpoch = calendar.timeInMillis
        if (day.timeEpoch in firstDayOfWeekEpoch until firstDayOfNextWeekEpoch) {
            return true
        }
        return false
    }

    private fun createDayItems(): List<DayItem> {
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

//    "https://www.eventbrite.com/e/analizis-3-eloadas-tickets-144031888255"
    private fun getWeekEvents() {
        val url = "https://www.eventbriteapi.com/v3/events/"
        val token = BuildConfig.EVENTBRITE_TOKEN

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                val name = response.getString("name")
                Toast.makeText(requireActivity(), name, Toast.LENGTH_LONG).show()

            },
            Response.ErrorListener { error ->
                print(error.stackTrace.toString())
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                var params: MutableMap<String, String>? = super.getHeaders()
                if (params == null) params = HashMap()
                params["Authorization"] = "Bearer $token"
                        return params
            }
        }
        val queue = Volley.newRequestQueue(requireActivity())
        queue.add(jsonObjectRequest)
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