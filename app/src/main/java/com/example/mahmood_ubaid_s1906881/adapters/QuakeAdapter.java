//
// Name                 Ubaid Mahmood
// Student ID           S1906881
// Programme of Study   BS/c Hons Computing
//

package com.example.mahmood_ubaid_s1906881.adapters;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mahmood_ubaid_s1906881.R;
import com.example.mahmood_ubaid_s1906881.model.Earthquake;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * This instantiates the recyler view adapter.
 * @author ubaid
 */
public class QuakeAdapter extends RecyclerView.Adapter<QuakeAdapter.ViewHolder> {

    private List<Earthquake> adapterDataSet;

    /**
     * Instantiating ViewHolder
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private Button button;
        private View relLayout2;
        private TextView dateView;
        private TextView urlView;
        private TextView categoryView;
        private TextView latView;
        private TextView longView;
        private TextView theMagnitude;
        private TextView theDepth;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            titleView = (TextView) view.findViewById(R.id.titleView);
            button = (Button) view.findViewById(R.id.listButton);
            relLayout2 = (RelativeLayout) view.findViewById(R.id.relLayout2);
            dateView = (TextView) view.findViewById(R.id.dateView);
            urlView = (TextView) view.findViewById(R.id.urlView);
            categoryView = (TextView) view.findViewById(R.id.categoryView);
            latView = (TextView) view.findViewById(R.id.latView);
            longView = (TextView) view.findViewById(R.id.longView);
            theMagnitude = (TextView) view.findViewById(R.id.theMagnitude);
            theDepth = (TextView) view.findViewById(R.id.theDepth);
        }

        public TextView getTextView() {
            return titleView;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public QuakeAdapter(List<Earthquake> dataSet) {
        adapterDataSet = dataSet;
    }

    public QuakeAdapter(){}

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * This method binds all the data to the view holder.
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String title = adapterDataSet.get(position).getTitle().replace("UK Earthquake alert :", "");
        title = title.substring(title.indexOf(":") + 1, title.lastIndexOf(",") -3);
        title = title.replace(",", ", ");

        double magnitude = adapterDataSet.get(position).getMagnitude();
        viewHolder.theMagnitude.append(String.valueOf(magnitude));

        double depth = adapterDataSet.get(position).getDepth();
        viewHolder.theDepth.append(String.valueOf(depth));

        Date dateObject = adapterDataSet.get(position).getDate();
        String dateFormatted = dateObject.toString();
        String date = dateFormatted.replace("GMT+01:00 ", "");
        String dates = date.replace("GMT ", "");

        viewHolder.dateView.setText(dates);

        URL url = adapterDataSet.get(position).getLink();
        viewHolder.urlView.append(url.toString());

        String category = adapterDataSet.get(position).getCategory();
        viewHolder.categoryView.append(category);

        String latitude = String.valueOf(adapterDataSet.get(position).getLatitude());
        viewHolder.latView.append(latitude);

        String longitude = String.valueOf(adapterDataSet.get(position).getLongitude());
        viewHolder.longView.append(longitude);

        viewHolder.getTextView().setText(title);
        viewHolder.relLayout2.setVisibility(View.GONE);

        viewHolder.button.setOnClickListener(view -> {
            if(viewHolder.relLayout2.getVisibility() == View.GONE) {

                viewHolder.relLayout2.setVisibility(View.VISIBLE);
                viewHolder.button.setText("Hide");
            }else{

                viewHolder.relLayout2.setVisibility(View.GONE);
                viewHolder.button.setText("Expand");
            }
        });

        if(adapterDataSet.get(position).getColour() == "red"){
            viewHolder.titleView.setTextColor(Color.parseColor("#fc0505"));
        } else if (adapterDataSet.get(position).getColour() == "yellow"){
            viewHolder.titleView.setTextColor(Color.parseColor("#ffea00"));
        } else if (adapterDataSet.get(position).getColour() == "green"){
            viewHolder.titleView.setTextColor(Color.parseColor("#05f705"));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return adapterDataSet.size();
    }
}

