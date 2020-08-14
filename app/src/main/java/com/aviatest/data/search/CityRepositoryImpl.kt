package com.aviatest.data.search

import com.aviatest.data.ServiceFactory
import com.aviatest.domain.City
import com.aviatest.domain.CityRepository
import io.reactivex.Single
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val serviceFactory: ServiceFactory,
    private val cityMapper: CityMapper
) : CityRepository {

    private val cityService: CityService by lazy { serviceFactory.create(CityService::class.java) }

    override fun getCities(query: String): Single<List<City>> = cityService.getCities(query)
        .map { it.cities }
        .map { it.map(cityMapper::map) }

}