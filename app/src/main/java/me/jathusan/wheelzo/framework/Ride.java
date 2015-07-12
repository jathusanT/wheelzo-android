package me.jathusan.wheelzo.framework;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Ride {

    @SerializedName("id")
    private int mId;
    @SerializedName("driver_id")
    private int mDriverId;
    @SerializedName("origin")
    private String mOrigin;
    @SerializedName("destination")
    private String mDestination;
    @SerializedName("capacity")
    private int mCapacity;
    @SerializedName("price")
    private double mPrice;
    @SerializedName("departure_date")
    private String mDepartureDate;
    @SerializedName("departure_time")
    private String mDepartureTime;
    @SerializedName("last_updated")
    private String mLastUpdated;
    @SerializedName("is_personal")
    private boolean mIsPersonal;

    // Don't serialise these fields with gson
    private transient int mColor;
    private transient ArrayList<String> dropOffs = new ArrayList<>();

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        mLastUpdated = lastUpdated;
    }

    public int getDriverId() {
        return mDriverId;
    }

    public void setDriverId(int driverId) {
        mDriverId = driverId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String origin) {
        mOrigin = origin;
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public int getCapacity() {
        return mCapacity;
    }

    public void setCapacity(int capacity) {
        mCapacity = capacity;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public String getDepartureDate() {
        return mDepartureDate;
    }

    public void setDepartureDate(String departureDate) {
        mDepartureDate = departureDate;
    }

    public String getDepartureTime() {
        return mDepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        mDepartureTime = departureTime;
    }

    public ArrayList<String> getDropOffs() {
        return dropOffs;
    }

    public void setDropOffs(ArrayList<String> dropOffs) {
        dropOffs = dropOffs;
    }

    public void addDropoff(String dropOff) {
        dropOffs.add(dropOff);
    }

    public boolean isPersonal() {
        return mIsPersonal;
    }

    public void setPersonal(boolean isPersonal) {
        mIsPersonal = isPersonal;
    }
}
