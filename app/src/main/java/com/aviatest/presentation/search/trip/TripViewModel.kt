package com.aviatest.presentation.search.trip

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aviatest.coreui.livedata.SingleLiveEvent
import com.aviatest.domain.Airport
import com.aviatest.presentation.search.Trip

class TripViewModel @ViewModelInject constructor() : ViewModel() {

    private var tripViewState
        set(value) {
            _tripViewStateLiveData.postValue(value)
        }
        get() = _tripViewStateLiveData.value!!

    private val _tripViewStateLiveData = MutableLiveData<TripViewState>(TripViewState())
    val tripViewStateLiveData: LiveData<TripViewState> get() = _tripViewStateLiveData

    private val _showSearchProgressLiveData = SingleLiveEvent<Trip>()
    val showSearchProgressLiveData: LiveData<Trip> get() = _showSearchProgressLiveData

    private val _showEmptyErrorLiveData = SingleLiveEvent<Any?>()
    val showEmptyErrorLiveData : LiveData<Any?> get() = _showEmptyErrorLiveData

    fun onDepartureChanged(airport: Airport) {
        tripViewState = tripViewState.copy(departure = airport)
    }

    fun onDestinationChanged(airport: Airport) {
        tripViewState = tripViewState.copy(destination = airport)
    }

    fun onSearchButtonClick() {
        val departure = tripViewState.departure
        val destination = tripViewState.destination
        if (departure == null || destination == null) {
            showEmptyError()
            return
        }
        _showSearchProgressLiveData.postValue(Trip(departure, destination))
    }

    private fun showEmptyError() = _showEmptyErrorLiveData.postCall()
}