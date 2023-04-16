//
// Name                 Ubaid Mahmood
// Student ID           S1906881
// Programme of Study   BS/c Hons Computing
//

package com.example.mahmood_ubaid_s1906881.viewmodel;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mahmood_ubaid_s1906881.model.Earthquake;
import com.example.mahmood_ubaid_s1906881.model.DataConfig;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class facilitates in transferring requests and data to and from the UI Layer.
 * @author ubaid
 *
 */
public class ViewModel extends androidx.lifecycle.ViewModel {

    private MutableLiveData<List<Earthquake>> mutableLiveData;
    private List<Earthquake> earthquakeList;

    private boolean data = false;

    DataConfig dataConfig = new DataConfig();

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    /**
     *
     * This method gets the data from the dataConfig class, if data is not present.
     * @return mutableLiveData
     */
    public LiveData<List<Earthquake>> getMutableLiveData(){

        if (mutableLiveData == null){

            mutableLiveData = new MutableLiveData<>();

            earthquakeList = dataConfig.getEarthquakeList();
            loadMutableLiveData(earthquakeList);

        }
        return mutableLiveData;
    }

    /**
     *
     * This method sets the mutableLiveData with the earthquake list.
     * @param earthquakeList
     */
    private void loadMutableLiveData(List<Earthquake> earthquakeList) {

        Handler myHandler = new Handler();

        myHandler.postDelayed(() -> {

            List<Earthquake> earthquakes = new ArrayList<>();

            earthquakes.addAll(earthquakeList);
            mutableLiveData.setValue(earthquakes);

        }, 1000);
    }


    private void setMutableLiveData(List<Earthquake> earthquakes){
        mutableLiveData.setValue(earthquakes);
    }

    /**
     * Sorts by all earthquake method.
     * @param list
     */
    public void sortByAll(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        earthquakes = list;
        if(earthquakes.isEmpty()){
            System.out.println("No Data to display All Earthquakes");
        }else {
            mutableLiveData.setValue(earthquakes);
        }

    }

    /**
     * Calls DataConfig sort by nearest northern earthquake method.
     * @param list
     */
    public void sortByNorthernEarthquake(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        earthquakes = dataConfig.sortByNorthernEarthquake(list);
        if(earthquakes.isEmpty()) {
            Log.e("sortData", "No Earthquakes North of Glasgow");
            mutableLiveData.setValue(list);
        }else{
            mutableLiveData.setValue(earthquakes);
        }

    }

    /**
     * Calls DataConfig sort by nearest southern earthquake method.
     * @param list
     */
    public void sortBySouthernEarthquake(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        earthquakes = dataConfig.sortBySouthernEarthquake(list);
        if(earthquakes.isEmpty()) {
            Log.e("sortData", "No Earthquakes South of Glasgow");
            mutableLiveData.setValue(list);
        }else{
            mutableLiveData.setValue(earthquakes);
        }
    }

    /**
     * Calls DataConfig sort by nearest eastern earthquake method.
     * @param list
     */
    public void sortByEasternEarthquake(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        earthquakes = dataConfig.sortByEasternEarthquake(list);
        if(earthquakes.isEmpty()) {
            Log.e("sortData", "No Earthquakes East of Glasgow");
            mutableLiveData.setValue(list);
        }else{
            mutableLiveData.setValue(earthquakes);
        }
    }

    /**
     * Calls DataConfig sort by nearest western earthquake method.
     * @param list
     */
    public void sortByWesternEarthquake(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        earthquakes = dataConfig.sortByWesternEarthquake(list);
        if(earthquakes.isEmpty()) {
            Log.e("sortData", "No Earthquakes West of Glasgow");
            mutableLiveData.setValue(list);
        }else{
            mutableLiveData.setValue(earthquakes);
        }
    }

    /**
     * Calls DataConfig sort by depth method
     * @param list
     */
    public void sortByDepth(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        earthquakes = dataConfig.sortByDepth(list);
        if(earthquakes.isEmpty()) {
            Log.e("sortData", "No data for sorting Earthquakes by depth");
            mutableLiveData.setValue(list);
        }else{
            mutableLiveData.setValue(earthquakes);
        }
    }

    /**
     * Calls DataConfig sort by magnitude method
     * @param list
     */
    public void sortByMagnitude(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        earthquakes = dataConfig.sortByMagnitude(list);
        if(earthquakes.isEmpty()) {
            Log.e("sortData", "No data for sorting Earthquakes by magnitude");
            mutableLiveData.setValue(list);
        }else{
            mutableLiveData.setValue(earthquakes);
        }

    }

    /**
     * Calls method in DataConfig class to search by specified date.
     * @param query
     * @param list
     */
    public void searchByDate(String query, List<Earthquake> list) {
        List<Earthquake> earthquakesArrayList = new ArrayList<>();
        earthquakesArrayList = dataConfig.searchByData(query, list);
        if(earthquakesArrayList.isEmpty()) {
            setData(false);
            mutableLiveData.setValue(list);
        }else{
            mutableLiveData.setValue(earthquakesArrayList);
            setData(true);
        }

    }

    /**
     * Calls method in DataConfig class to search by specified date and location.
     * @param query
     * @param list
     */
    public void searchByDateAndLocation(String query, List<Earthquake> list) {
        List<Earthquake> earthquakesArrayList = new ArrayList<>();
        earthquakesArrayList = dataConfig.searchByDataAndLocation(query, list);
        if(earthquakesArrayList.isEmpty()) {
            setData(false);
            mutableLiveData.setValue(list);
        }else{
            mutableLiveData.setValue(earthquakesArrayList);
            setData(true);
        }

    }

    public void startProgressOfService() throws InterruptedException {

        dataConfig.startProgress();
    }

    public List<Earthquake> getEarthquakes() {
        return dataConfig.getEarthquakeList();
    }
}
