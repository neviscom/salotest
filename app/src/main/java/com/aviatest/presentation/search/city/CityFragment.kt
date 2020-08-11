package com.aviatest.presentation.search.city

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aviatest.R
import com.aviatest.domain.CityRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CityFragment : Fragment(R.layout.city_fragment) {

    @Inject lateinit var cityRepository: CityRepository

    private val viewModel: CitiesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCities()
    }
}