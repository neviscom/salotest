package com.aviatest.domain

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: CityRepository
) {

    fun findCityAirports(query: String): Single<List<Airport>> = when {
        query.isBlank() -> Single.just(emptyList())
        else -> repository.getCities(query)
            .observeOn(Schedulers.computation())
            .flatMapObservable { Observable.fromIterable(it) }
            .concatMapSingle { city ->
                Single.just(city.iataCodes.map { createAirport(it, city) })
            }
            .toList()
            .map(::foldAirports)
    }

    private fun createAirport(iataCode: String, city: City) = Airport(
        iataCode = iataCode,
        cityName = city.fullName,
        cityId = city.id,
        countryName = city.country,
        location = city.location
    )

    private fun foldAirports(list: List<List<Airport>>): List<Airport> = list
        .fold(
            ArrayList(),
            { acc, airports -> acc.apply { addAll(airports) } }
        )
}