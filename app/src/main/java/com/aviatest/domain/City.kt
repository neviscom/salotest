package com.aviatest.domain

data class City(
    val id: String,

    val city: String,
//    val latinCity: String,

//    val countryId: String,
//    val countryCode: String,
    val country: String,
//    val latinCountry: String,

//    val latinFullName: String,
    val fullName: String,

//    val clar: String,
//    val latinClar: String,

    val location: Location,
    val iataCodes: List<String>

)