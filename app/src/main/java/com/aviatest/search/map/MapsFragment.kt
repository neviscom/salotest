package com.aviatest.search.map

import android.animation.ValueAnimator
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.PathInterpolator
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.aviatest.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlin.math.atan2
import kotlin.math.max

class MapsFragment : Fragment() {

    companion object {
        private const val PROGRESS_STEP = 0.01f
        private const val PLANE_ANIMATION_DURATION = 30_000L
        private const val PLANE_ANIMATION_START_DELAY = 1_000L
    }

    private val pathInterpolator = PathInterpolator(
        0f, 0.5f,
        1f, 0.5f
    )

    private val planeAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        .apply { duration =
            PLANE_ANIMATION_DURATION
        }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val spb = LatLng(59.966449, 30.309805)
        val ams = LatLng(52.384030, 4.845314)
        val bounds = LatLngBounds(ams, spb)
        googleMap.setLatLngBoundsForCameraTarget(bounds)

        addPlanePath(googleMap, spb, ams)
        val marker = addPlaneMarker(googleMap, spb, ams)
        addCityMarker(googleMap, spb, "LED")
        addCityMarker(googleMap, ams, "AMS")

        planeAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            marker.position = getPosition(value, spb, ams)
            marker.rotation = getAngle(value, spb, ams) - 90f
        }
        planeAnimator.startDelay =
            PLANE_ANIMATION_START_DELAY
        planeAnimator.start()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun addCityMarker(googleMap: GoogleMap, position: LatLng, cityTitle: String) {
        googleMap.addMarker(
            MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromBitmap(createCityMarker(cityTitle)))
        )
    }

    private fun addPlaneMarker(
        googleMap: GoogleMap,
        startPosition: LatLng,
        finalPosition: LatLng
    ): Marker = googleMap.addMarker(
        MarkerOptions()
            .position(startPosition)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
            .anchor(0.5f, 0.5f)
            .rotation(getAngle(0f, startPosition, finalPosition) - 90f)
    )

    private fun addPlanePath(
        googleMap: GoogleMap,
        startPosition: LatLng,
        finalPosition: LatLng
    ) {
        val points = generateSequence(0.0f, { it + PROGRESS_STEP })
            .map { getPosition(it, startPosition, finalPosition) }
            .take(101)
            .toList()

        googleMap.addPolyline(
            PolylineOptions()
                .addAll(points)
                .geodesic(true)
                .pattern(listOf(Dot(), Gap(10f)))
        )
    }


    private fun getPosition(
        @FloatRange(from = 0.0, to = 1.0) progress: Float,
        startPosition: LatLng,
        finalPosition: LatLng
    ): LatLng =
        LatLng(
            startPosition.latitude + (finalPosition.latitude - startPosition.latitude) * progress,
            startPosition.longitude + (finalPosition.longitude - startPosition.longitude) * pathInterpolator.getInterpolation(
                progress
            )
        )

    private fun getAngle(
        @FloatRange(from = 0.0, to = 1.0) progress: Float,
        from: LatLng,
        to: LatLng
    ): Float {
        val current = getPosition(progress, from, to)
        val next = getPosition(max(progress + PROGRESS_STEP, 0f), from, to)
        return getAngle(current, next)
    }

    private fun getAngle(start: LatLng, target: LatLng): Float {
        var angle = Math.toDegrees(
            atan2(
                target.longitude - start.longitude,
                target.latitude - start.latitude
            )
        ).toFloat()
        if (angle < 0) {
            angle += 360f
        }
        return angle
    }

    private fun createCityMarker(text: String): Bitmap? {

        val drawable = ContextCompat.getDrawable(requireContext(),
            R.drawable.ic_city_marker
        )
            ?: return null
        var bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null

        var bitmapConfig = bitmap.config;
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }

        bitmap = bitmap.copy(bitmapConfig, true)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 16 * resources.displayMetrics.density
        paint.color = Color.WHITE

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val x = (bitmap.width - bounds.width()) / 2f
        val y = (bitmap.height + bounds.height()) / 2f
        canvas.drawText(text, x, y, paint)

        return bitmap
    }
}