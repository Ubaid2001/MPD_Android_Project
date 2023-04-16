//
// Name                 Ubaid Mahmood
// Student ID           S1906881
// Programme of Study   BS/c Hons Computing
//

package com.example.mahmood_ubaid_s1906881.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.mahmood_ubaid_s1906881.R;
import com.example.mahmood_ubaid_s1906881.adapters.InfoWindowAdapter;
import com.example.mahmood_ubaid_s1906881.model.Earthquake;
import com.example.mahmood_ubaid_s1906881.viewmodel.ViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class instantiates the map fragment
 * @author ubaid
 */
public class MapsFragment extends Fragment {

    private View mapLayout;
    private GoogleMap mMap;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

//          The try clause makes sure that the map style is loaded from the json file.
            try {
                boolean isSuccess = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_maps));
                if(!isSuccess)
                    Toast.makeText(getContext(), "Maps style loads failed", Toast.LENGTH_SHORT).show();
            }catch (Resources.NotFoundException ex){
                ex.printStackTrace();
            }

            ViewModel viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);

            mMap = googleMap;


//          The runnable thread retrieves the data from the viewModel and applies the data on the Map.
            Runnable runnable = () ->
                viewModel.getMutableLiveData().observe(getActivity(), mutableLiveDataList -> {

                    mMap.clear();
                    String title = "";

                    for (Earthquake eq : mutableLiveDataList) {

                        title = eq.getTitle().replace("UK Earthquake alert :", "");
                        title = title.substring(title.indexOf(":") + 1, title.lastIndexOf(",") - 3);
                        title = title.replace(",", ", ");
                        LatLng ping = new LatLng(eq.getLatitude(), eq.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(ping).title(title).snippet(eq.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(ping));

                    }
                });
            runnable.run();

//          Sets the infoWindow to the custom infoWindow created the by the InfoWindowAdapter class
            mMap.setInfoWindowAdapter(new InfoWindowAdapter(getActivity()));

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        mapLayout = (FrameLayout) view.findViewById(R.id.mapLayout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}