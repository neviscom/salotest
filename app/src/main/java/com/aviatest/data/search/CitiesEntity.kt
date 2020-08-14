package com.aviatest.data.search

import com.google.gson.annotations.SerializedName

data class CitiesEntity(
    @SerializedName("cities")
    val cities: List<CityEntity>
)