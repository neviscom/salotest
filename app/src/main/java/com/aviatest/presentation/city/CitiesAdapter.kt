package com.aviatest.presentation.city

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aviatest.R
import com.aviatest.domain.Airport

class CitiesAdapter(
    context: Context,
    private val onAirportClickListener: OnAirportClickListener
) : RecyclerView.Adapter<CityAirportViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    var airports: List<Airport> = listOf()
        set(value) {
            field = value.toList()
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAirportViewHolder {
        val view = inflater.inflate(R.layout.airport_view, parent, false)
        return CityAirportViewHolder(view)
    }

    override fun getItemCount(): Int = airports.size

    override fun onBindViewHolder(holder: CityAirportViewHolder, position: Int) =
        airports[position].let { item ->
            holder.itemView.setOnClickListener { onAirportClickListener.onAirportClick(item) }
            holder.bind(item)
        }
}