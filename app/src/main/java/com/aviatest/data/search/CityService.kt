package com.aviatest.data.search

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CityService {

    @GET("autocomplete")
    fun getCities(
        @Query("term") query: String,
        @Query("lang") lang: String = "ru"
    ): Single<Cities>
}