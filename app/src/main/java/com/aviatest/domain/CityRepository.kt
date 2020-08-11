package com.aviatest.domain

import io.reactivex.Single

interface CityRepository {

    fun getCities(query: String): Single<List<Any>>
}