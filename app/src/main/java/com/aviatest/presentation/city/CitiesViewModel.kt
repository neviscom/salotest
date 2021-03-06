package com.aviatest.presentation.city

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aviatest.coreui.livedata.SingleLiveEvent
import com.aviatest.domain.Airport
import com.aviatest.domain.SearchCityUseCase
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class CitiesViewModel @ViewModelInject constructor(
    private val useCase: SearchCityUseCase
) : ViewModel() {

    private val queriesSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    private val _airports = MutableLiveData<List<Airport>>(emptyList())
    val airports: LiveData<List<Airport>> get() = _airports

    private val _showLoadingError = SingleLiveEvent<Any?>()
    val showLoadingError: LiveData<Any?> get() = _showLoadingError

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> get() = _progress

    private var disposable: Disposable? = null

    init {
        disposable = queriesSubject
            .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .switchMapMaybe { query ->
                useCase.findCityAirports(query)
                    .toMaybe()
                    .doOnSubscribe { _progress.postValue(true) }
                    .doOnEvent { _, _ -> _progress.postValue(false) }
                    .retry(3)
                    .doOnError(::handleError)
                    .onErrorComplete()
            }
            .doOnNext(_airports::postValue)
            .subscribe(Functions.emptyConsumer(), Consumer(::handleError))
    }

    override fun onCleared() {
        disposable?.dispose()
    }

    fun onQueryChanged(query: String) {
        if (query == queriesSubject.value) {
            return
        }
        queriesSubject.onNext(query)
    }

    private fun handleError(throwable: Throwable) = _showLoadingError.postCall()

    companion object {
        const val DEBOUNCE_TIMEOUT = 300L
    }
}