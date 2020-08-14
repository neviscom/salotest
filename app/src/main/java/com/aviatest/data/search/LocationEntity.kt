package com.aviatest.data.search

import com.google.gson.annotations.SerializedName

data class LocationEntity(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)