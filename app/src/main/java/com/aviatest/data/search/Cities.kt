package com.aviatest.data.search

import com.google.gson.annotations.SerializedName

data class Cities(
    @SerializedName("cities")
    val cities: List<City>
)