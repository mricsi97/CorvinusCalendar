package com.example.corvinuscalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_calendar.*
import java.text.SimpleDateFormat
import java.util.*

// 1970-01-01 00:00 - 2100-01-01 00:00
const val MINIMUM_DATE_EPOCH_MILLIS = 0L
const val MAXIMUM_DATE_EPOCH_MILLIS = 4102444800000L
const val MONTH_MILLIS = 2629743000L

private const val TAG = "CalendarActivity"

class CalendarActivity : AppCompatActivity() {

    private val dateFormatter = SimpleDateFormat("yyyy. MM", Locale.ROOT)

    private var pageSelectedCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = MINIMUM_DATE_EPOCH_MILLIS
            calendar.add(Calendar.MONTH, position)
            tvDate_Toolbar.text = dateFormatter.format(calendar.timeInMillis)
            super.onPageSelected(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        setSupportActionBar(toolbar_Calendar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        calendarViewPager.adapter = MonthViewPagerAdapter(this)
        calendarViewPager.offscreenPageLimit = 3
        calendarViewPager.setCurrentItem(getCurrentMonthPosition(), false)
        tvDate_Toolbar.text = dateFormatter.format(Calendar.getInstance().timeInMillis)
        tvDate_BottomSheet.text
        calendarViewPager.registerOnPageChangeCallback(pageSelectedCallback)
    }

    override fun onDestroy() {
        calendarViewPager.unregisterOnPageChangeCallback(pageSelectedCallback)
        super.onDestroy()
    }

    private fun getCurrentMonthPosition() : Int {
        val calendar = Calendar.getInstance()
        val monthOffset = (calendar.timeInMillis - MINIMUM_DATE_EPOCH_MILLIS) / MONTH_MILLIS
        Log.i(TAG, monthOffset.toString())
        return monthOffset.toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_calendar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.action_JumpToToday -> {
            // TODO: implement "jump to today" feature
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}