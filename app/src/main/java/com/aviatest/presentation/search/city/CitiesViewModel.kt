package com.aviatest.presentation.search.city

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.aviatest.domain.CityRepository
import io.reactivex.schedulers.Schedulers

class CitiesViewModel @ViewModelInject constructor(
    private val cityRepository: CityRepository
) : ViewModel() {

    fun getCities() {
        cityRepository.getCities("mow")
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}