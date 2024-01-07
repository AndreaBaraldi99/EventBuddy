package it.lanos.eventbuddy.data.source.firebase.realtimeDB;

import it.lanos.eventbuddy.data.source.LocationCallback;
import it.lanos.eventbuddy.data.source.models.Location;

public abstract class BaseLocationRemoteDataSource {

    protected LocationCallback locationCallback;
    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback = locationCallback;
    }
    public abstract void setLocation(Location location);
    public abstract void observeLocation(String eventId);
    public abstract void detachObserver(String eventId);
}
