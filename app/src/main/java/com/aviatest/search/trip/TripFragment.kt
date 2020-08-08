package com.aviatest.search.trip

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aviatest.R
import com.aviatest.coreui.extentions.getCallback
import kotlinx.android.synthetic.main.trip_fragment.*

class TripFragment : Fragment(R.layout.trip_fragment) {

    interface Callback {
        fun onOriginClick()
        fun onDestinationClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        city_of_origin.setOnClickListener { getCallback<Callback>()?.onOriginClick() }
        city_of_destination.setOnClickListener { getCallback<Callback>()?.onDestinationClick() }
    }
}