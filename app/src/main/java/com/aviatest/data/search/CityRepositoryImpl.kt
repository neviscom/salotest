package com.aviatest.data.search

import com.aviatest.data.ServiceFactory
import com.aviatest.domain.CityRepository
import io.reactivex.Single
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val serviceFactory: ServiceFactory
) : CityRepository {

    private val cityService: CityService by lazy { serviceFactory.create(CityService::class.java) }

    override fun getCities(query: String): Single<List<Any>> = cityService.getCities(query)
        .map { it.cities }
}