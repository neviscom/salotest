package com.aviatest.presentation.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aviatest.presentation.search.map.MapsFragment
import com.aviatest.presentation.search.trip.TripFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(),
    TripFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, TripFragment())
                .commitAllowingStateLoss()
        }
    }

    override fun onTripSelected(trip: Trip) {
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, MapsFragment.newInstance(trip))
            .addToBackStack(MapsFragment::class.java.canonicalName)
            .commit()
    }
}