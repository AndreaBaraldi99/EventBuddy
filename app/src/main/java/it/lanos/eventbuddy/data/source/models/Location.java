package it.lanos.eventbuddy.data.source.models;

public class Location {

    public String userId;

    public String eventId;
    public double latitude;
    public double longitude;

    public Location(double latitude, double longitude, String eventId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventId = eventId;
    }

    public Location(){}

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public String getUserId() { return userId; }

    public String getEventId() { return eventId; }

}
