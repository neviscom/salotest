package com.aviatest.presentation.city

import com.aviatest.domain.Airport

interface OnAirportClickListener {
    fun onAirportClick(airport: Airport)
}