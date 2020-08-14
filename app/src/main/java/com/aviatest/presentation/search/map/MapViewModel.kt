package com.aviatest.presentation.search.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aviatest.domain.Airport
import com.aviatest.presentation.search.Trip
import com.google.android.gms.maps.model.LatLng

class MapViewModel @ViewModelInject constructor() : ViewModel() {

    private val _tripLiveData = MutableLiveData<MapTrip>()
    val tripLiveData: LiveData<MapTrip> get() = _tripLiveData

    fun onFirstStart(trip: Trip) {
        _tripLiveData.postValue(mapTrip(trip))
    }

    private fun mapTrip(trip: Trip): MapTrip = MapTrip(
        from = mapMapPoint(trip.departure),
        to = mapMapPoint(trip.destination)
    )

    private fun mapMapPoint(airport: Airport) = MapPoint(
        location = LatLng(
            airport.location.latitude,
            airport.location.longitude
        ),
        name = airport.iataCode
    )
}