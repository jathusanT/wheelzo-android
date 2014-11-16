package me.jathusan.wheelzo.framework;

public class Passenger {
    private int id;
    private int user_id;
    private int ride_id;
    private long passengerRating, driverRating;
    private String lastUpdated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRide_id() {
        return ride_id;
    }

    public void setRide_id(int ride_id) {
        this.ride_id = ride_id;
    }

    public long getPassengerRating() {
        return passengerRating;
    }

    public void setPassengerRating(long passengerRating) {
        this.passengerRating = passengerRating;
    }

    public long getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(long driverRating) {
        this.driverRating = driverRating;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
