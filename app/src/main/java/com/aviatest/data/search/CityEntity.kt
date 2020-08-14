package com.aviatest.data.search

import com.google.gson.annotations.SerializedName

data class CityEntity(
    @SerializedName("id")
    val id: String,

    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("latinFullName")
    val latinFullName: String,
    @SerializedName("fullname")
    val fullName: String,
    @SerializedName("clar")
    val clar: String,
    @SerializedName("latinClar")
    val latinClar: String,
    @SerializedName("location")
    val location: LocationEntity,
    @SerializedName("hotelsCount")
    val hotelsCount: String,
    @SerializedName("iata")
    val iata: List<String>,
    @SerializedName("city")
    val city: String,
    @SerializedName("latinCity")
    val latinCity: String,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezonesec")
    val timezonesec: String,
    @SerializedName("latinCountry")
    val latinCountry: String,
    @SerializedName("countryId")
    val countryId: String,
    @SerializedName("_score")
    val score: Long,
    @SerializedName("isOutOfService")
    val isOutOfService: Boolean,
    @SerializedName("state")
    val state: String?
)