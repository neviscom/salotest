package com.aviatest.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aviatest.search.city.CityFragment
import com.aviatest.search.map.MapsFragment
import com.aviatest.search.trip.TripFragment

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

    override fun onOriginClick() {
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, CityFragment())
            .addToBackStack(CityFragment::class.java.canonicalName)
            .commit()
    }

    override fun onDestinationClick() {
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content,
                MapsFragment()
            )
            .addToBackStack(CityFragment::class.java.canonicalName)
            .commit()
    }
}