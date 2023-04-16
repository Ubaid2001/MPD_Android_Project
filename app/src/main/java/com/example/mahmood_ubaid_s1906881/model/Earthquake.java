//
// Name                 Ubaid Mahmood
// Student ID           S1906881
// Programme of Study   BS/c Hons Computing
//

package com.example.mahmood_ubaid_s1906881.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * This class the instantiates and models how the earthquake data will be conceived.
 * Implements the Parcelable class.
 * @author ubaid
 */
public class Earthquake implements Parcelable {

    private String title;
    private String description;
    private Date date;
    private URL link;
    private String category;
    private double latitude;
    private double longitude;

    private double depth;
    private double magnitude;

    private String location;
    private String colour;

    public Earthquake() {}

    /**
     *
     * @param title
     * @param description
     * @param date
     * @param link
     * @param category
     * @param latitude
     * @param longitude
     * @param depth
     * @param magnitude
     * @param location
     * @param colour
     */
    public Earthquake(String title, String description, Date date, URL link, String category, double latitude, double longitude, double depth, double magnitude, String location, String colour) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.link = link;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
        this.magnitude = magnitude;
        this.location = location;
        this.colour = colour;
    }


    protected Earthquake(Parcel in) {
        title = in.readString();
        description = in.readString();
        category = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        depth = in.readDouble();
        magnitude = in.readDouble();
        location = in.readString();
        colour = in.readString();
    }

    public static final Creator<Earthquake> CREATOR = new Creator<Earthquake>() {
        @Override
        public Earthquake createFromParcel(Parcel in) {
            return new Earthquake(in);
        }

        @Override
        public Earthquake[] newArray(int size) {
            return new Earthquake[size];
        }
    };

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public String toString() {
        return "Earthquake{" +
                " title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", link=" + link +
                ", category='" + category + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", depth=" + depth +
                ", magnitude="+ magnitude +
                ", location="+ location +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(String url) throws MalformedURLException {
        this.link = new URL(url);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(category);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
