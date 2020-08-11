package com.aviatest.data.search

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("countryCode")
    val countryCode: String
)