package me.jathusan.wheelzo.framework;

public class Ride {
    private int id;
    private int driverId;
    private String origin;
    private String destination;
    private int capacity;
    private double price;
    private String start;
    private String lastUpdated;
    private String[] dropOffs;
    private String[] passeners;
    private Comment[] comments;
    private boolean isPersonal;

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

    public String[] getDropOffs() {
        return dropOffs;
    }

    public void setDropOffs(String[] dropOffs) {
        this.dropOffs = dropOffs;
    }

    public String[] getPasseners() {
        return passeners;
    }

    public void setPasseners(String[] passeners) {
        this.passeners = passeners;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public boolean isPersonal() {
        return isPersonal;
    }

    public void setPersonal(boolean isPersonal) {
        this.isPersonal = isPersonal;
    }
}
