package ethiopia.covid.android.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ethiopia.covid.android.App.Companion.instance
import ethiopia.covid.android.R
import ethiopia.covid.android.data.WorldCovid
import ethiopia.covid.android.network.API.OnItemReady
import ethiopia.covid.android.util.Utils.calculateTheDelta
import ethiopia.covid.android.util.Utils.getCurrentTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.math.max

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class HeatMapFragment : BaseFragment() {
    private var mainMapView: MapView? = null
    private var backButton: FloatingActionButton? = null
    private val circles: MutableList<Circle> = mutableListOf()

    override fun onResume() {
        super.onResume()
        mainMapView?.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.heat_map_fragment, container, false)
        mainMapView = mainView.findViewById(R.id.main_map_view)
        backButton = mainView.findViewById(R.id.back_button)
        MapsInitializer.initialize(activity)

        mainMapView?.getMapAsync { googleMap: GoogleMap ->
            val success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            activity,
                            if (getCurrentTheme(activity) == 0) R.raw.light_map_style else R.raw.dark_map_style
                    ))
            if (!success) {
                Timber.e("Some problem happened around parsing the style.")
            }
            setUpClusterManager(googleMap)
        }
        mainMapView?.onCreate(savedInstanceState)
        backButton?.setOnClickListener { activity?.onBackPressed() }
        return mainView
    }

    private fun setUpClusterManager(map: GoogleMap) {
        if (activity == null) return
        instance.mainAPI.getWorldStat(object : OnItemReady<List<WorldCovid>?> {
            override fun onItem(item: List<WorldCovid>?, err: String?) {
                CoroutineScope(Dispatchers.Main).launch { addCountryCircles(map, item ?: listOf()) }
            }
        })
    }

    private suspend fun addCountryCircles(map: GoogleMap, items: List<WorldCovid>) {
        withContext(Dispatchers.Default) {
            var maximum = Int.MIN_VALUE
            for ((_, cases) in items) {
                if (maximum < cases) maximum = cases
            }
            maximum /= items.size
            for ((_, cases, _, _, _, _, _, _, _, _, _, countryInfo) in items) {
                val colors = calculateTheDelta(intArrayOf(234, 20, 20), intArrayOf(20, 180, 12),
                        max(0f, 1f.coerceAtMost((1f - cases.toFloat() / maximum.toFloat()))))
                withContext(Dispatchers.Main) {
                    circles.add(
                            map.addCircle(CircleOptions()
                                    .center(LatLng(countryInfo!!.lat, countryInfo.lon))
                                    .radius(80000.0.coerceAtLeast(400000.0.coerceAtMost(10 * (cases.toDouble() / maximum.toDouble()))))
                                    .fillColor(Color.argb(186, colors[0], colors[1], colors[2]))
                                    .strokeColor(Color.argb(0, 255, 255, 255))
                                    .clickable(false)
                                    .strokeWidth(0f))
                    )
                }

            }
        }
    }

    //    We should consider working on this method
    //    private void scaleAllCircles(float mapZoomLevel, boolean isZoomingIn) {
    //        for (Circle circle : circles) {
    //            circle.setVisible(true);
    //            circle.setRadius(isZoomingIn ? Math.max(20, circle.getRadius() / (mapZoomLevel)) : Math.min(400_000, circle.getRadius() * (mapZoomLevel * 3.8)));
    //        }
    //    }
    override fun onPause() {
        super.onPause()
        mainMapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mainMapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainMapView?.onDestroy()
    }

    companion object {
        //    float mapZoomLevel = 0f;
        fun newInstance(): HeatMapFragment {
            val args = Bundle()
            val fragment = HeatMapFragment()
            fragment.arguments = args
            return fragment
        }
    }
}