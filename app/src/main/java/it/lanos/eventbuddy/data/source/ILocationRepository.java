package it.lanos.eventbuddy.data.source;

import androidx.lifecycle.MutableLiveData;

import it.lanos.eventbuddy.data.source.models.Location;
import it.lanos.eventbuddy.data.source.models.Result;

public interface ILocationRepository {
    MutableLiveData<Result> getLocation(String eventId);
    void setLocation(Location location);
    void stopLocationUpdates(String eventId);
}
