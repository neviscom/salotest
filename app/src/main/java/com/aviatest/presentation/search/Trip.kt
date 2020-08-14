package com.aviatest.presentation.search

import android.os.Parcelable
import com.aviatest.domain.Airport
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trip(
    val departure: Airport,
    val destination: Airport
) : Parcelable