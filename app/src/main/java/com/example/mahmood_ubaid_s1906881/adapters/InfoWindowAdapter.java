//
// Name                 Ubaid Mahmood
// Student ID           S1906881
// Programme of Study   BS/c Hons Computing
//

package com.example.mahmood_ubaid_s1906881.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.mahmood_ubaid_s1906881.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Date;

/**
 * This class instantiates the InfoWindowAdapter.
 * Implements GoogleMap.InfoWindowAdapter.
 * @author ubaid
 */
public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private View view;

    private FragmentActivity myContext;

    public InfoWindowAdapter(FragmentActivity aContext) {
        this.myContext = aContext;

        LayoutInflater inflater = (LayoutInflater) myContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.info_window,
                null);
    }

    @Override
    public View getInfoContents(Marker marker) {

        if (marker != null
                && marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {

        String location = marker.getTitle();
        TextView locationUi = (TextView) view.findViewById(R.id.location);
        if (location != null) {

            locationUi.setText(location);
        } else {

            locationUi.setText("");
            locationUi.setVisibility(View.GONE);
        }

        String snippet = marker.getSnippet();
        TextView magWindow = (TextView) view.findViewById(R.id.magWindow);
        TextView depWindow = (TextView) view.findViewById(R.id.depWindow);
        TextView latlongWindow = (TextView) view.findViewById(R.id.latlongWindow);

        if (snippet != null) {

            String[] snippets = snippet.split(" ;", 6);

            String[] magfullVal = snippets[4].split(":", 2);
            String magValue = magfullVal[1];
            magWindow.setText(magValue);

            String[] depfullVal = snippets[3].split(":", 2);
            String depValue = depfullVal[1];
            depWindow.setText(depValue);

            latlongWindow.setText(snippets[2]);


        }

        return view;
    }


}


