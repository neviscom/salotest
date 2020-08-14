package com.aviatest.data.search

import com.aviatest.domain.City
import com.aviatest.domain.Location
import javax.inject.Inject

class CityMapper @Inject constructor() {

    fun map(cityEntity: CityEntity): City = City(
        id = cityEntity.id,
        city = cityEntity.city,
        country = cityEntity.country,
        fullName = cityEntity.fullName,
        location = map(cityEntity.location),
        iataCodes = cityEntity.iata
    )

    private fun map(location: LocationEntity): Location = Location(
        latitude = location.lat,
        longitude = location.lon
    )
}