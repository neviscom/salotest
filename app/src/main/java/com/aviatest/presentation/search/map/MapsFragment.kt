package com.aviatest.presentation.search.map

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.aviatest.R
import com.aviatest.coreui.extentions.dp
import com.aviatest.coreui.utils.BitmapUtils
import com.aviatest.presentation.search.Trip
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.max

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {

    @Inject
    lateinit var bitmapUtils: BitmapUtils

    companion object {
        private const val PROGRESS_STEP = 0.01f
        private const val PLANE_ANIMATION_DURATION = 15_000L
        private const val PLANE_ANIMATION_START_DELAY = 1_000L
    }

    private val planeAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        .apply { interpolator = LinearInterpolator() }
        .apply { duration = PLANE_ANIMATION_DURATION }
        .apply { startDelay = PLANE_ANIMATION_START_DELAY }

    private val viewModel: MapViewModel by viewModels()

    private lateinit var pathEvaluator: PathEvaluator
    private lateinit var planeMarker: Marker

    private var googleMap: GoogleMap? = null

    private var animatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        viewModel.tripLiveData.value?.let { initMap(googleMap, it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.onFirstStart(arguments!!.getParcelable(TRIP_KEY)!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        viewModel.tripLiveData.observe(viewLifecycleOwner,
            Observer { trip -> googleMap?.let { initMap(it, trip) } })
    }

    override fun onPause() {
        planeAnimator.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (planeAnimator.isStarted) planeAnimator.resume()
    }

    private fun initMap(googleMap: GoogleMap, trip: MapTrip) {
        val points = getNormalizedPoints(trip.from.location, trip.to.location)
        pathEvaluator = CubicBezier(
            points.first, points.second,
            0f, 0.5f,
            1f, 0.5f
        )

        addPlanePath(googleMap)
        planeMarker = addPlaneMarker(googleMap, trip)
        addCityMarker(googleMap, trip.from)
        addCityMarker(googleMap, trip.to)

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                getLatLngBounds(trip.from.location, trip.to.location),
                32.dp.toInt()
            )
        )

        animatorUpdateListener?.let(planeAnimator::removeUpdateListener)
        animatorUpdateListener = ValueAnimator.AnimatorUpdateListener {
            val value = it.animatedValue as Float
            planeMarker.position = getPosition(value)
            planeMarker.rotation = getAngle(value) - 90f
        }
        planeAnimator.addUpdateListener(animatorUpdateListener)
        planeAnimator.start()
    }

    private fun addCityMarker(googleMap: GoogleMap, mapPoint: MapPoint): Marker =
        googleMap.addMarker(
            MarkerOptions()
                .position(mapPoint.location)
                .icon(BitmapDescriptorFactory.fromBitmap(createCityMarker(mapPoint.name)))
        )

    private fun addPlaneMarker(googleMap: GoogleMap, trip: MapTrip): Marker =
        googleMap.addMarker(
            MarkerOptions()
                .position(trip.from.location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
                .anchor(0.5f, 0.5f)
                .rotation(getAngle(0f) - 90f)
        )

    private fun addPlanePath(googleMap: GoogleMap): Polyline {
        val points = generateSequence(0.0f, { it + PROGRESS_STEP })
            .map { getPosition(it) }
            .take(101)
            .toList()

        return googleMap.addPolyline(
            PolylineOptions()
                .addAll(points)
                .geodesic(true)
                .pattern(listOf(Dot(), Gap(10f)))
        )
    }

    private fun getPosition(@FloatRange(from = 0.0, to = 1.0) progress: Float): LatLng =
        pathEvaluator.evaluate(progress).let { LatLng(it.x.toDouble(), it.y.toDouble()) }

    private fun getAngle(@FloatRange(from = 0.0, to = 1.0) progress: Float): Float {
        val current = getPosition(progress)
        val next = getPosition(max(progress + PROGRESS_STEP, 0f))
        return getAngle(current, next)
    }

    private fun getAngle(start: LatLng, final: LatLng): Float {
        var angle = Math.toDegrees(
            atan2(
                final.longitude - start.longitude,
                final.latitude - start.latitude
            )
        ).toFloat()
        if (angle < 0) {
            angle += 360f
        }
        return angle
    }

    private fun createCityMarker(text: String): Bitmap? =
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_city_marker)
            ?.let { bitmapUtils.drawTextOnCenterOfDrawable(it, text) }

    private fun getLatLngBounds(first: LatLng, second: LatLng) =
        LatLngBounds.builder()
            .include(first)
            .include(second)
            .build()

    private fun getNormalizedPoints(from: LatLng, to: LatLng): Pair<PointF, PointF> {
        var fromPointF = PointF(from.latitude.toFloat(), from.longitude.toFloat())
        var toPointF = PointF(to.latitude.toFloat(), to.longitude.toFloat())
        if ((to.longitude - from.longitude).absoluteValue > 180) {
            if (from.longitude < to.longitude) {
                fromPointF = PointF(from.latitude.toFloat(), from.longitude.toFloat() + 360f)
            } else {
                toPointF = PointF(to.latitude.toFloat(), to.longitude.toFloat() + 360f)
            }
        }
        return fromPointF to toPointF
    }
}