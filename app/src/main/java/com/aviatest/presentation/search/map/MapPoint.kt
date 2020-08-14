package com.aviatest.presentation.search.map

import com.google.android.gms.maps.model.LatLng

data class MapPoint(
    val location: LatLng,
    val name: String
)