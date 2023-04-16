//
// Name                 Ubaid Mahmood
// Student ID           S1906881
// Programme of Study   BS/c Hons Computing
//

package com.example.mahmood_ubaid_s1906881.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmood_ubaid_s1906881.R;
import com.example.mahmood_ubaid_s1906881.model.Earthquake;
import com.example.mahmood_ubaid_s1906881.adapters.QuakeAdapter;
import com.example.mahmood_ubaid_s1906881.viewmodel.ViewModel;

import java.util.ArrayList;

/**
 * This class contains instantiates everything regarding the lists.
 * This class is a container for the list.
 * @author ubaid
 */
public class ListFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private RecyclerView recyclerView;
    private ArrayList<Earthquake> earthquakeList;
    private Spinner sortingSpinner;
    private ViewModel viewModel;
    private EditText searchBar;
    private ImageView searchIcon;
    private TextView errorText;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        sortingSpinner = (Spinner) view.findViewById(R.id.sortingSpinner);
        searchBar = (EditText) view.findViewById(R.id.searchBar);
        searchIcon = (ImageView) view.findViewById(R.id.searchIcon);
        errorText = (TextView) view.findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sortingSpinner, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortingSpinner.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);

        initialiseRecyclerView();

        earthquakeList = (ArrayList<Earthquake>) viewModel.getEarthquakes();

//       This is probably why all the frames are being skipped, pushed this to the background thread.
        sortingSpinner.setOnItemSelectedListener(this);

        searchIcon.setOnClickListener(this);

        return view;
    }

    /**
     * This method gets the data from the viewModel which retrieves it from the data config class.
     * Upon retrieving the data, it is set to the recycler view to display.
     */
    public void initialiseRecyclerView(){

        Runnable runnable = () -> {

            viewModel.getMutableLiveData().observe(getActivity(), mutableLiveDataList -> {
                System.out.println("this is in the -> function " + mutableLiveDataList.size());
                QuakeAdapter customAdapter = new QuakeAdapter(mutableLiveDataList);

                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            });
        };

        runnable.run();

    }


    /**
     * This method calls the corresponding methods in the DataConfig class, depending on the item selected.
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Runnable runnable = () -> {

            if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("Nearest Northern")) {

                viewModel.sortByNorthernEarthquake(earthquakeList);
            } else if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("All")) {

                viewModel.sortByAll(earthquakeList);
            } else if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("Nearest Eastern")) {

                viewModel.sortByEasternEarthquake(earthquakeList);
            } else if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("Nearest Southern")) {

                viewModel.sortBySouthernEarthquake(earthquakeList);
            } else if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("Nearest Western")) {

                viewModel.sortByWesternEarthquake(earthquakeList);
            } else if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("Shallowest Depth")) {

                viewModel.sortByDepth(earthquakeList);
            } else if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("Greatest Magnitude")) {

                viewModel.sortByMagnitude(earthquakeList);
            }
        };
        runnable.run();
    }

    /**
     * This methods is called when no item is selected from the spinner.
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This method is called when a view with a listener attached is clicked on.
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(view == searchIcon){
            if(searchBar.getText().toString().length() > 0) {

                String query = searchBar.getText().toString();
                searchBar.clearFocus();


                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                Runnable runnable = () -> {

                    if (!query.contains("in")) {
                        viewModel.searchByDate(query, earthquakeList);
                        if (!viewModel.isData()) {
                            errorText.setText("No Earthquakes For This Date !!!");
                            errorText.setVisibility(View.VISIBLE);
                        } else {
                            Log.e("SearchData", "Search Data by Date is Displaying");
                            errorText.setVisibility(View.GONE);
                        }
                    } else {
                        viewModel.searchByDateAndLocation(query, earthquakeList);
                        if (!viewModel.isData()) {
                            errorText.setText("No Earthquakes For This Date And Location !!!");
                            errorText.setVisibility(View.VISIBLE);
                        } else {
                            Log.e("SearchData", "Search Data by Date and Location is Displaying");
                            errorText.setVisibility(View.GONE);
                        }
                    }

                };
                runnable.run();
            } else {
                searchBar.setError("When Searching this field cannot be left blank");
            }
        }
    }

}