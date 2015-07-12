package me.jathusan.wheelzo.framework;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Ride implements Parcelable {

    private int id;
    private int driverId;
    private String origin;
    private String destination;
    private int capacity;
    private double price;
    private String start;
    private String lastUpdated;
    private ArrayList<String> dropOffs = new ArrayList<String>();
    private boolean isPersonal;
    private int color;

    public Ride() {}

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public ArrayList<String> getDropOffs() {
        return dropOffs;
    }

    public void setDropOffs(ArrayList<String> dropOffs) {
        this.dropOffs = dropOffs;
    }

    public void addDropoff(String dropOff) {
        dropOffs.add(dropOff);
    }

    public boolean isPersonal() {
        return isPersonal;
    }

    public void setPersonal(boolean isPersonal) {
        this.isPersonal = isPersonal;
    }

    protected Ride(Parcel in) {
        id = in.readInt();
        driverId = in.readInt();
        origin = in.readString();
        destination = in.readString();
        capacity = in.readInt();
        price = in.readDouble();
        start = in.readString();
        lastUpdated = in.readString();
        if (in.readByte() == 0x01) {
            dropOffs = new ArrayList<String>();
            in.readList(dropOffs, String.class.getClassLoader());
        } else {
            dropOffs = null;
        }
        isPersonal = in.readByte() != 0x00;
        color = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(driverId);
        dest.writeString(origin);
        dest.writeString(destination);
        dest.writeInt(capacity);
        dest.writeDouble(price);
        dest.writeString(start);
        dest.writeString(lastUpdated);
        if (dropOffs == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(dropOffs);
        }
        dest.writeByte((byte) (isPersonal ? 0x01 : 0x00));
        dest.writeInt(color);
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
