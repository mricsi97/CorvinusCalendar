package com.example.corvinuscalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_week.view.*
import java.text.SimpleDateFormat
import java.util.*

class WeekAdapter(
    private val days: List<DayItem>
) : RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    private val dateFormatter = SimpleDateFormat("dd", Locale.ROOT)

    class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        return WeekViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_week,
                parent,
                false)
        )
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val day = days[position]

        holder.itemView.apply {
            tvDayOfMonth_weekItem.text = dateFormatter.format(day.timeEpoch)
            if(day.isOtherMonth)
                tvDayOfMonth_weekItem.setTextColor(
                    ContextCompat.getColor(context, R.color.colorGray))
        }
    }


}