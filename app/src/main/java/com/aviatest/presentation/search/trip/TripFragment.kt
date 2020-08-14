package com.aviatest.presentation.search.trip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.aviatest.R
import com.aviatest.coreui.extentions.getCallback
import com.aviatest.domain.Airport
import com.aviatest.presentation.city.CitiesActivity
import com.aviatest.presentation.search.Trip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.trip_fragment.*

@AndroidEntryPoint
class TripFragment : Fragment(R.layout.trip_fragment) {

    interface Callback {
        fun onTripSelected(trip: Trip)
    }

    companion object {
        const val DEPARTURE_REQ_CODE = 1
        const val DESTINATION_REQ_CODE = 2
    }

    private val viewModel: TripViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        city_of_origin.setOnClickListener { routeToChooseDepartureAirport() }
        city_of_destination.setOnClickListener { routeToChooseDestinationAirport() }
        search_btn.setOnClickListener { viewModel.onSearchButtonClick() }

        viewModel.tripViewStateLiveData.observe(viewLifecycleOwner, Observer { trip ->
            city_of_origin.text = getPlaceName(trip.departure) ?: getString(R.string.city_of_origin)
            city_of_destination.text =
                getPlaceName(trip.destination) ?: getString(R.string.city_of_destination)
        })
        viewModel.showSearchProgressLiveData.observe(viewLifecycleOwner,
            Observer { getCallback<Callback>()?.onTripSelected(it) }
        )
        viewModel.showEmptyErrorLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), R.string.fill_all_fields_error, Toast.LENGTH_SHORT)
                .show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) return
        when (requestCode) {
            DEPARTURE_REQ_CODE -> viewModel.onDepartureChanged(getAirport(data)!!)
            DESTINATION_REQ_CODE -> viewModel.onDestinationChanged(getAirport(data)!!)
        }
    }

    private fun getAirport(data: Intent): Airport? =
        data.getParcelableExtra(CitiesActivity.DESTINATION_EXTRA)

    private fun getPlaceName(airport: Airport?): String? =
        airport?.let { "${airport.cityName}, ${airport.iataCode}" }

    private fun routeToChooseDepartureAirport() =
        startActivityForResult(CitiesActivity.createIntent(requireActivity()), DEPARTURE_REQ_CODE)

    private fun routeToChooseDestinationAirport() =
        startActivityForResult(CitiesActivity.createIntent(requireActivity()), DESTINATION_REQ_CODE)

}