package com.example.corvinuscalendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

class MonthViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return ((MAXIMUM_DATE_EPOCH_MILLIS-MINIMUM_DATE_EPOCH_MILLIS) / MONTH_MILLIS).toInt()
    }

    override fun createFragment(position: Int): Fragment {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = MINIMUM_DATE_EPOCH_MILLIS
        calendar.add(Calendar.MONTH, position)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return MonthViewFragment.newInstance(calendar.timeInMillis)
    }

}