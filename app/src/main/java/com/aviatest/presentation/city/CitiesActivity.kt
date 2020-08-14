package com.aviatest.presentation.city

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aviatest.domain.Airport
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesActivity : AppCompatActivity(),
    CityFragment.Callback {

    companion object {
        fun createIntent(context: Context) = Intent(context, CitiesActivity::class.java)

        const val DESTINATION_EXTRA = "destination"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, CityFragment())
                .commit()
        }
    }

    override fun onAirportSelected(airport: Airport) = setResultAndFinish(airport)

    private fun setResultAndFinish(airport: Airport) {
        setResult(Activity.RESULT_OK, Intent().putExtra(DESTINATION_EXTRA, airport))
        finish()
    }
}