package com.aviatest.presentation.city

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aviatest.R
import com.aviatest.coreui.extentions.getCallback
import com.aviatest.coreui.extentions.hideSoftKeyboard
import com.aviatest.domain.Airport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.city_fragment.*

@AndroidEntryPoint
class CityFragment : Fragment(R.layout.city_fragment),
    OnAirportClickListener {

    interface Callback {
        fun onAirportSelected(airport: Airport)
    }

    private val viewModel: CitiesViewModel by viewModels()

    private lateinit var adapter: CitiesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = CitiesAdapter(context, this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        results_recycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        results_recycler.adapter = adapter
        results_recycler.setOnTouchListener { _, _ -> hideSoftKeyboard(); false; }

        search_text.doOnTextChanged { text, _, _, _ -> viewModel.onQueryChanged(text.toString()) }

        viewModel.airports.observe(viewLifecycleOwner, Observer { airports ->
            showEmptyState(airports.isEmpty())
            if (airports.isNotEmpty()) showProgress(false)
            adapter.airports = airports
        })
        viewModel.showLoadingError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT)
                .show()
        })
        viewModel.progress.observe(viewLifecycleOwner, Observer { show ->
            showProgress(adapter.airports.isEmpty() && show)
            if (show) showEmptyState(false)
        })
    }

    override fun onAirportClick(airport: Airport) {
        getCallback<Callback>()?.onAirportSelected(airport)
    }

    private fun showEmptyState(isVisible: Boolean) {
        empty_state.isVisible = isVisible
    }

    private fun showProgress(isVisible: Boolean) {
        progress.isVisible = isVisible
    }
}