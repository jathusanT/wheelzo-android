package me.jathusan.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Ride implements Parcelable {

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
    private int mPrice;

    @SerializedName("departure_date")
    private String mDepartureDate;

    @SerializedName("departure_time")
    private String mDepartureTime;

    @SerializedName("start")
    private String mStart;

    @SerializedName("last_updated")
    private String mLastUpdated;

    @SerializedName("driver_name")
    private String mDriverName;

    @SerializedName("driver_facebook_id")
    private String mDriverFacebookId;

    @SerializedName("is_personal")
    private boolean mIsPersonal;

    // Don't serialise these fields with gson
    private transient int mColor;
    private transient ArrayList<String> dropOffs = new ArrayList<String>();

    public Ride() {
    }

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

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public String getStart() {
        return mStart;
    }

    public void setStart(String start) {
        mStart = start;
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

    public String getDriverFacebookid() {
        return mDriverFacebookId;
    }

    public void setDriverFacebookid(String driverFacebookid) {
        mDriverFacebookId = driverFacebookid;
    }

    public String getDriverName() {
        return mDriverName;
    }

    public void setDriverName(String driverName) {
        mDriverName = driverName;
    }

    protected Ride(Parcel in) {
        mId = in.readInt();
        mDriverId = in.readInt();
        mOrigin = in.readString();
        mDestination = in.readString();
        mCapacity = in.readInt();
        mPrice = in.readInt();
        mDepartureDate = in.readString();
        mDepartureTime = in.readString();
        mStart = in.readString();
        mLastUpdated = in.readString();
        mDriverName = in.readString();
        mDriverFacebookId = in.readString();
        mIsPersonal = in.readByte() != 0x00;
        mColor = in.readInt();
        if (in.readByte() == 0x01) {
            dropOffs = new ArrayList<String>();
            in.readList(dropOffs, String.class.getClassLoader());
        } else {
            dropOffs = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mDriverId);
        dest.writeString(mOrigin);
        dest.writeString(mDestination);
        dest.writeInt(mCapacity);
        dest.writeInt(mPrice);
        dest.writeString(mDepartureDate);
        dest.writeString(mDepartureTime);
        dest.writeString(mStart);
        dest.writeString(mLastUpdated);
        dest.writeString(mDriverName);
        dest.writeString(mDriverFacebookId);
        dest.writeByte((byte) (mIsPersonal ? 0x01 : 0x00));
        dest.writeInt(mColor);
        if (dropOffs == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(dropOffs);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ride> CREATOR = new Parcelable.Creator<Ride>() {
        @Override
        public Ride createFromParcel(Parcel in) {
            return new Ride(in);
        }

        @Override
        public Ride[] newArray(int size) {
            return new Ride[size];
        }
    };
}