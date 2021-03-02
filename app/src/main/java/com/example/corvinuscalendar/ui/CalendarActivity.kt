package com.example.corvinuscalendar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.corvinuscalendar.BuildConfig
import com.example.corvinuscalendar.ui.month.MonthViewPagerAdapter
import com.example.corvinuscalendar.R
import kotlinx.android.synthetic.main.activity_calendar.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

// 1970-01-01 00:00 - 2100-01-01 00:00
const val MINIMUM_DATE_EPOCH_MILLIS = 0L
const val MAXIMUM_DATE_EPOCH_MILLIS = 4102444800000L
const val MONTH_MILLIS = 2629743000L

class CalendarActivity : AppCompatActivity() {

    private val topDateFormatter = SimpleDateFormat("yyyy. MM.", Locale.ROOT)
    private val bottomDateFormatter = SimpleDateFormat("MM. dd.", Locale.ROOT)

    private val calendarViewModel: CalendarViewModel by viewModels()

    private var pageSelectedCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = MINIMUM_DATE_EPOCH_MILLIS
            calendar.add(Calendar.MONTH, position)
            calendarViewModel.selectedMonthEpoch = calendar.timeInMillis
            tvDate_Toolbar.text = topDateFormatter.format(calendar.timeInMillis)
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
        // TODO: nicer custom date formatting
        tvDate_Toolbar.text = topDateFormatter.format(Calendar.getInstance().timeInMillis)
        calendarViewPager.registerOnPageChangeCallback(pageSelectedCallback)

        calendarViewModel.selectedDay.observe(this, Observer { day ->
            val dayTime = day.timeEpoch
            tvDate_BottomSheet.text = bottomDateFormatter.format(dayTime)

            if (day.isOtherMonth) {
                if (dayTime < calendarViewModel.selectedMonthEpoch!!) {
                    calendarViewPager.currentItem--
                } else {
                    calendarViewPager.currentItem++
                }
            }
        })
    }

    override fun onDestroy() {
        calendarViewPager.unregisterOnPageChangeCallback(pageSelectedCallback)
        super.onDestroy()
    }

    //    curl --request POST
//    --url 'https://www.eventbrite.com/oauth/token'
//    --header 'content-type: application/x-www-form-urlencoded'
//    --data grant_type=authorization_code
//    --data 'client_id=API_KEY
//    --data client_secret=CLIENT_SECRET
//    --data code=ACCESS_CODE
//    --data 'redirect_uri=REDIRECT_URI'
//    private fun authorizeEventbriteUser() {
//        val apiKey = BuildConfig.EVENTBRITE_API_KEY
//        val clientSecret = BuildConfig.EVENTBRITE_CLIENT_SECRET
//        val redirectUri = "http://www.corvinuscalendar.com"
//        val authUrl = "https://www.eventbrite.com/oauth/authorize?response_type=code" +
//                "&client_id=" + apiKey +
//                "&redirect_uri=" + redirectUri
//
//        val queue = Volley.newRequestQueue(this)
//        val url = "https://www.eventbrite.com/oauth/token"
//        val jsonPostRequest = JSONObject()
//        jsonPostRequest.put("client_id", apiKey)
//        jsonPostRequest.put("client_secret", clientSecret)
//        jsonPostRequest.put("code", ACCESS_CODE)
//        jsonPostRequest.put("redirect_uri", redirectUri)
//
//        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null,
//            Response.Listener { response ->
//
//            },
//            Response.ErrorListener { error ->
//                // TODO: Handle error
//            }
//        )
//    }

    private fun getCurrentMonthPosition(): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val currentMonthEpoch = calendar.timeInMillis
        calendarViewModel.selectedMonthEpoch = currentMonthEpoch
        val monthOffset = (currentMonthEpoch - MINIMUM_DATE_EPOCH_MILLIS) / MONTH_MILLIS +
                if ((currentMonthEpoch - MINIMUM_DATE_EPOCH_MILLIS) % MONTH_MILLIS > 0) 1 else 0
        return monthOffset.toInt()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_calendar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_JumpToToday -> {
            // TODO: implement "jump to today" feature
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}