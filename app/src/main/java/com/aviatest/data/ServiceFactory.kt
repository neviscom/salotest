package com.aviatest.data

interface ServiceFactory {

    /**
     * Create an implementation of the API endpoints defined by the `service` interface.
     */
    fun <T> create(service: Class<T>): T
}