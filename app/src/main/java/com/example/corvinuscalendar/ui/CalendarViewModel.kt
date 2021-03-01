package com.example.corvinuscalendar.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.corvinuscalendar.data.DayItem

class CalendarViewModel : ViewModel() {
    val selectedDay: MutableLiveData<DayItem> by lazy {
        MutableLiveData<DayItem>()
    }
    val previousSelectedDay: MutableLiveData<DayItem> by lazy {
        MutableLiveData<DayItem>()
    }
    var selectedMonthEpoch: Long? = null
}