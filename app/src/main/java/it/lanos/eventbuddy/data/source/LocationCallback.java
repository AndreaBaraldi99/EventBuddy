package it.lanos.eventbuddy.data.source;

import it.lanos.eventbuddy.data.source.models.Location;

public interface LocationCallback {
    void onFailureFromRemoteDatabase(Exception e);
    void onReceivedLocationFromRemoteSuccess(Location location);
}
