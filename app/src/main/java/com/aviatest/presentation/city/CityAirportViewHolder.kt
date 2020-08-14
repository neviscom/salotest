package com.aviatest.presentation.city

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aviatest.R
import com.aviatest.domain.Airport

class CityAirportViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val cityName: TextView = view.findViewById(R.id.city_name)
    private val airportName: TextView = view.findViewById(R.id.airport_name)

    init {
        itemView.setOnClickListener {  }
    }

    fun bind(airport: Airport) {
        cityName.text = airport.cityName
        airportName.text = airport.iataCode
    }

}