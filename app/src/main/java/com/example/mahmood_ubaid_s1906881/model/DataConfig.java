//
// Name                 Ubaid Mahmood
// Student ID           S1906881
// Programme of Study   BS/c Hons Computing
//

package com.example.mahmood_ubaid_s1906881.model;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * This class retrieves the data from the RSS file.
 * Stores the data in an Earthquake list.
 * Does all the required logic with the data.
 * @author ubaid
 */
public class DataConfig {

    private List<Earthquake> earthquakeList;
    private String result;

    private final String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    private double latitiude = 55.8642;
    private double longitude = -4.2518;

    public DataConfig() {
    }

    public List<Earthquake> getEarthquakeList(){
        return earthquakeList;
    }

    public void setEarthquakeList(List<Earthquake> earthquakes){
        earthquakeList = earthquakes;
    }

    /**
     *
     * This method searches for the earthquake in the list by the requested date.
     * @param query
     * @param list
     * @return earthquakes
     */
    public List<Earthquake> searchByData(String query, List<Earthquake> list) {

        List<Earthquake> earthquakes = new ArrayList<>();

        if(query.length() > 1) {
            String[] queryParts = query.split(" ", 3);
            String queryMonth = queryParts[0];
            String queryDate = queryParts[1] + " ";
            String queryYear = queryParts[2];

            double totalMagnitude = 0;
            for (Earthquake earthquake : list) {

                String dateString = earthquake.getDate().toString();
                String[] dateParts = dateString.split(" ", 6);
                String trueString = dateParts[0] + " " + dateParts[1] + " " + dateParts[2] + " " + dateParts[4] + " " + dateParts[5];


                if ((containsIgnoreCase(trueString, queryMonth)) && (containsIgnoreCase(trueString, queryDate)) && (containsIgnoreCase(trueString, queryYear))) {
                    totalMagnitude += earthquake.getMagnitude();
                    earthquakes.add(earthquake);
                }

            }

            double averageMagnitude = totalMagnitude / earthquakes.size();
            for (int i = 0; i < earthquakes.size(); i++) {
                if (earthquakes.get(i).getMagnitude() > averageMagnitude) {
                    earthquakes.get(i).setColour("red");
                } else if (earthquakes.get(i).getMagnitude() < averageMagnitude) {
                    earthquakes.get(i).setColour("yellow");
                } else {
                    earthquakes.get(i).setColour("green");
                }
            }
        }else {
            Log.e("mySearch","query is too short");
        }

        return earthquakes;
    }

    /**
     *
     * This method searches for the earthquake in the list by the requested date and location.
     * @param query
     * @param list
     * @return earthquakes
     */
    public List<Earthquake> searchByDataAndLocation(String query, List<Earthquake> list) {

        List<Earthquake> earthquakes = new ArrayList<>();

        if(query.length() > 1) {
            String[] queryParts = query.split(" in", 2);
            String queryDate = queryParts[0];
            String queryLocation = queryParts[1];


            String[] queryDateParts = queryDate.split(" ", 3);
            String queryMonth = queryDateParts[0];
            String queryDay = queryDateParts[1];
            String queryYear = queryDateParts[2];

            String location = queryLocation;

            for (Earthquake earthquake : list) {
                String dateString = earthquake.getDate().toString();
                String[] dateParts = dateString.split(" ", 6);

                String trueString = dateParts[0] + " " + dateParts[1] + " " + dateParts[2] + " " + dateParts[4] + " " + dateParts[5];
                String earthquakeLocation = earthquake.getLocation();

                if (earthquakeLocation.contains(",")) {
                    earthquakeLocation = earthquakeLocation.replace(",", " ");
                }


                if ((containsIgnoreCase(trueString, queryMonth)) && (containsIgnoreCase(trueString, queryDay)) && (containsIgnoreCase(trueString, queryYear)) && (containsIgnoreCase(earthquakeLocation, location))) {

                    earthquakes.add(earthquake);
                }
            }

        } else {
            Log.e("mySearch","query is too short");
        }

        return earthquakes;
    }

    /**
     * Sorts the list by the nearest northern earthquake from glasgow.
     * Sends the updated list back to the UI layer.
     * @param list
     * @return earthquakes
     */
    public List<Earthquake> sortByNorthernEarthquake(List<Earthquake> list){

        double bearing = 0;
        List<Earthquake> earthquakes = new ArrayList<>();
        for(Earthquake eq: list){
            if(eq.getLatitude() > latitiude){
                bearing = findBearing(latitiude, longitude, eq.getLatitude(), eq.getLongitude());
                if((bearing >= 315 && bearing <= 360) || (bearing >= 0 && bearing <= 45)) {
                    earthquakes.add(eq);
                }
            }
        }


        Collections.sort(earthquakes, (earthquake, t1) -> {
            if(earthquake.getLatitude() > t1.getLatitude()){
                return 1;
            }
            else if(earthquake.getLatitude() == t1.getLatitude()){
                return 0;
            }
            return -1;
        });
        return earthquakes;
    }

    /**
     * Sorts the list by the nearest southern earthquake from glasgow.
     * Sends the updated list back to the UI layer.
     * @param list
     * @return earthquakes
     */
    public List<Earthquake> sortBySouthernEarthquake(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        double bearing = 0;
        for(Earthquake eq: list){
            if(eq.getLatitude() < latitiude){
                bearing = findBearing(latitiude, longitude, eq.getLatitude(), eq.getLongitude());
                if(bearing >= 135 && bearing <= 225) {
                    earthquakes.add(eq);
                }

            }
        }

        Collections.sort(earthquakes, (earthquake, t1) -> {
            if(earthquake.getLatitude() < t1.getLatitude()){
                return 1;
            }
            else if(earthquake.getLatitude() == t1.getLatitude()){
                return 0;
            }
            return -1;
        });
        return earthquakes;
    }

    /**
     * Sorts the list by the nearest eastern earthquake from glasgow.
     * Sends the updated list back to the UI layer.
     * @param list
     * @return earthquakes
     */
    public List<Earthquake> sortByEasternEarthquake(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        double bearing = 0;
        for(Earthquake eq: list){
            if(eq.getLongitude() > longitude){
                bearing = findBearing(latitiude, longitude, eq.getLatitude(), eq.getLongitude());
                if(bearing >= 45 && bearing <= 135) {
                    earthquakes.add(eq);
                }

            }
        }


        Collections.sort(earthquakes, (earthquake, t1) -> {
            if(earthquake.getLongitude() > t1.getLongitude()){
                return 1;
            }
            else if(earthquake.getLongitude() == t1.getLongitude()){
                return 0;
            }
            return -1;
        });

        return earthquakes;
    }

    /**
     * Sorts the list by the nearest western earthquake from glasgow.
     * Sends the updated list back to the UI layer.
     * @param list
     * @return earthquakes
     */
    public List<Earthquake> sortByWesternEarthquake(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();
        double bearing = 0;
        for(Earthquake eq: list){
            if(eq.getLongitude() < longitude){
                bearing = findBearing(latitiude, longitude, eq.getLatitude(), eq.getLongitude());
                if(bearing >= 225 && bearing <= 315) {
                    earthquakes.add(eq);
                    System.out.println(bearing);
                }
            }
        }

        Collections.sort(earthquakes, (earthquake, t1) -> {
            if(earthquake.getLongitude() < t1.getLongitude()){
                return 1;
            }
            else if(earthquake.getLongitude() == t1.getLongitude()){
                return 0;
            }
            return -1;
        });

        return earthquakes;
    }

    /**
     * Sorts the list by the shallowest depth earthquake.
     * Sends the updated list back to the UI layer.
     * @param list
     * @return earthquakes
     */
    public List<Earthquake> sortByDepth(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();

        earthquakes.addAll(list);

        Collections.sort(earthquakes, (earthquake, t1) -> {
            if(earthquake.getDepth() < t1.getDepth()){
                return 1;
            }
            else if(earthquake.getDepth() == t1.getDepth()){
                return 0;
            }
            return -1;
        });

        return earthquakes;
    }

    /**
     * Sorts the list by the greatest magnitude earthquake.
     * Sends the updated list back to the UI layer.
     * @param list
     * @return earthquakes
     */
    public List<Earthquake> sortByMagnitude(List<Earthquake> list){

        List<Earthquake> earthquakes = new ArrayList<>();

        earthquakes.addAll(list);

        Collections.sort(earthquakes, (earthquake, t1) -> {
            if(earthquake.getMagnitude() < t1.getMagnitude()){
                return 1;
            }
            else if(earthquake.getMagnitude() == t1.getMagnitude()){
                return 0;
            }
            return -1;
        });

        return earthquakes;
    }

    /**
     * This method begins retrieving the data and inserting it into the earthquake list.
     * @throws InterruptedException
     */
    public void startProgress() throws InterruptedException {
        // Run network access on a separate thread;

        Task task = new Task(urlSource);
        Thread thread = new Thread(task);
        thread.start();
        thread.join();

        earthquakeList = task.getEarthquakesWithinTask();
        setEarthquakeList(earthquakeList);

    }

    private class Task implements Runnable {
        private String url;
        private List<Earthquake> earthquakes;

        public Task(String url) {
            this.url = url;
        }

        public void setEarthquakesWithinTask(List<Earthquake> earthquakes) {
            this.earthquakes = earthquakes;
        }

        public List<Earthquake> getEarthquakesWithinTask(){
            return earthquakes;
        }

        @Override
        public void run() {

            URL aurl;
            URLConnection urlConnection;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                urlConnection = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                // Throw away the first 2 header lines before parsing
                int count = 0;
                while ((inputLine = in.readLine()) != null) {
                    Log.e("MyTag","in while");
                    if(count > 12){
                        result = result + inputLine;
                        result = result.replace("null", "");
                        result = result.replace("geo:lat", "lat");
                        result = result.replace("geo:long", "long");
                        result = result.replace("</channel>", "");
                        result = result.replace("</rss>", "");
                        Log.e("MyTag",inputLine);
                    }count++;
                }
                earthquakeList = parseData(result);
                setEarthquakesWithinTask(earthquakeList);


                Log.e("ArraySize", "array size " + earthquakeList.size());
                Log.e("MyTag","exit while");
                in.close();
            }
            catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }
        }

    }

    /**
     * This method retrieves the data from the RSS file utilising XMLPullParser.
     * @param dataToParse
     * @return earthquakes
     */
    private List<Earthquake> parseData(String dataToParse){

        List<Earthquake> earthquakes = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if((eventType == XmlPullParser.START_TAG)){

                    if (xpp.getName().equalsIgnoreCase("item")) {

                        Earthquake earthquake1 = new Earthquake();
                        eventType = xpp.next();

                        if(eventType == XmlPullParser.START_TAG){
                            if(xpp.getName().equalsIgnoreCase("title")){

                                earthquake1.setTitle(xpp.nextText());
                                eventType = xpp.next();
                            }
                            if(xpp.getName().equalsIgnoreCase("description")){

                                earthquake1.setDescription(xpp.nextText());

                                String[] s1 = earthquake1.getDescription().split(";");
                                String depth = "";
                                String magnitude = "";
                                String location = "";
                                for(String string: s1){
                                    if(string.contains("Depth")){
                                        depth = string;
                                    }
                                    if(string.contains("Magnitude")){
                                        magnitude = string;
                                    }
                                    if(string.contains(("Location"))){
                                        location = string;
                                    }
                                }
                                depth = depth.replace("Depth:", "");
                                depth = depth.replace("km", "");
                                magnitude = magnitude.replace("Magnitude: ", "");
                                location = location.replace("Location: ", "");

                                earthquake1.setDepth(Double.parseDouble(depth));
                                earthquake1.setMagnitude(Double.parseDouble(magnitude));
                                earthquake1.setLocation(location);

                                eventType = xpp.next();
                            }
                            if(xpp.getName().equalsIgnoreCase("link")){
                                earthquake1.setLink(xpp.nextText());
                                eventType = xpp.next();
                            }
                            if(xpp.getName().equalsIgnoreCase("pubDate")){


                                SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
                                String temp = xpp.nextText();
                                Date dateObject = formatter.parse(temp);
                                earthquake1.setDate(dateObject);

                                eventType = xpp.next();
                            }
                            if (xpp.getName().equalsIgnoreCase("category")) {
                                String temp = xpp.nextText();
                                earthquake1.setCategory(temp);

                                eventType = xpp.next();
                            }
                            if (xpp.getName().equalsIgnoreCase("lat")) {
                                String temp = xpp.nextText();
                                earthquake1.setLatitude(Double.parseDouble(temp));

                                eventType = xpp.next();
                            }
                            if (xpp.getName().equalsIgnoreCase("long")) {
                                String temp = xpp.nextText();
                                earthquake1.setLongitude(Double.parseDouble(temp));

                                eventType = xpp.next();

                            }
                        }
                        if(eventType == XmlPullParser.END_TAG){
                            if(xpp.getName().equalsIgnoreCase("item")){

                                earthquakes.add(earthquake1);
                            }
                        }


                    }
                }

                // Get the next event
                eventType = xpp.next();
            } // End of while
        }
        catch (XmlPullParserException e1) {
            Log.e("MyTag","Parsing error " + e1.toString());
        }
        catch (IOException e1) {
            Log.e("MyTag","IO error during parsing");
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Log.e("MyTag","End document");

        return earthquakes;
    }

    /**
     * This method is similar to the contains method, but adds ignore casing to it.
     * @param str
     * @param searchStr
     * @return boolean
     */
    public boolean containsIgnoreCase(String str, String searchStr)     {
        if(str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }


    /**
     *
     * This method finds the bearing using longitude and latitudes of two locations.
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return bearing
     */
    private double findBearing(double lat1, double lon1, double lat2, double lon2){
        double longitude1 = lon1;
        double longitude2 = lon2;
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

}
