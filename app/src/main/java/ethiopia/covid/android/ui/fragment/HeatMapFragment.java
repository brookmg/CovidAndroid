package ethiopia.covid.android.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ethiopia.covid.android.App;
import ethiopia.covid.android.R;
import ethiopia.covid.android.data.WorldCovid;
import ethiopia.covid.android.util.Utils;
import timber.log.Timber;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class HeatMapFragment extends BaseFragment {

    private MapView mainMapView;
    private FloatingActionButton backButton;
    private List<Circle> circles = new ArrayList<>();
//    float mapZoomLevel = 0f;

    public static HeatMapFragment newInstance() {
        Bundle args = new Bundle();
        HeatMapFragment fragment = new HeatMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainMapView.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.heat_map_fragment, container, false);
        mainMapView = mainView.findViewById(R.id.main_map_view);
        backButton = mainView.findViewById(R.id.back_button);

        MapsInitializer.initialize(getActivity());
        mainMapView.getMapAsync(googleMap -> {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(),
                            Utils.getCurrentTheme(getActivity()) == 0 ? R.raw.light_map_style : R.raw.dark_map_style
                    ));

            if (!success) {
                Timber.e("Some problem happened around parsing the style.");
            }

            setUpClusterManager(googleMap);
        });

        mainMapView.onCreate(savedInstanceState);
        backButton.setOnClickListener( v -> getActivity().onBackPressed() );
        return mainView;
    }

    private void setUpClusterManager(GoogleMap map) {
        if (getActivity() == null) return;
        App.getInstance().getMainAPI().getWorldStat((items, err) -> addCountryCircles(map, items));
    }

    private void addCountryCircles(GoogleMap map, List<WorldCovid> items) {
        int maximum = Integer.MIN_VALUE;
        for (WorldCovid item: items) {
            if (maximum < item.getCases()) maximum = item.getCases();
        }

        maximum /= items.size();

        for (WorldCovid item : items) {
            int[] colors = Utils.calculateTheDelta(new int[]{ 234, 20, 20 }, new int[]{ 20, 180, 12 },
                    Math.max(0f, Math.min(1f , (float) (1f - ((float) item.getCases() / (float) maximum)))));

            circles.add(
                map.addCircle(new CircleOptions()
                        .center(new LatLng(item.getCountryInfo().getLat(), item.getCountryInfo().getLon()))
                        .radius(Math.max(80_000  , Math.min(400_000 , 10*((double) item.getCases() / (double) maximum))))
                        .fillColor(Color.argb(186 , colors[0] , colors[1], colors[2]))
                        .strokeColor(Color.argb(0 , 255,255,255))
                        .clickable(false)
                        .strokeWidth(0))
            );

        }

    }

//    We should consider working on this method
//    private void scaleAllCircles(float mapZoomLevel, boolean isZoomingIn) {
//        for (Circle circle : circles) {
//            circle.setVisible(true);
//            circle.setRadius(isZoomingIn ? Math.max(20, circle.getRadius() / (mapZoomLevel)) : Math.min(400_000, circle.getRadius() * (mapZoomLevel * 3.8)));
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
        mainMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mainMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainMapView.onDestroy();
    }
}
