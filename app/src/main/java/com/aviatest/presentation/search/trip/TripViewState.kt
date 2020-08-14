package com.aviatest.presentation.search.trip

import com.aviatest.domain.Airport

data class TripViewState(
    val departure: Airport? = null,
    val destination: Airport? = null,
    val emptyError: Boolean = false
)