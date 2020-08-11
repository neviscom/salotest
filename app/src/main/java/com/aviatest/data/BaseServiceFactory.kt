package com.aviatest.data

import retrofit2.Retrofit
import javax.inject.Inject

class BaseServiceFactory @Inject constructor(
    private val retrofit: Retrofit
): ServiceFactory {

    override fun <T> create(service: Class<T>): T = retrofit.create(service)

}