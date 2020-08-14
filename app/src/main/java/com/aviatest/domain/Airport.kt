package com.aviatest.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Airport(
    val iataCode: String,
    val cityName: String,
    val cityId: String,
    val location: Location,
    val countryName: String
) : Parcelable