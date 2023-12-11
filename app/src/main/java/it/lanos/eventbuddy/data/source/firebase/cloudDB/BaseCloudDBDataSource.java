package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import it.lanos.eventbuddy.data.source.firebase.auth.AuthCallback;

public abstract class BaseCloudDBDataSource {
    protected CloudDBCallback dbCallback;
    public void setAuthCallback(CloudDBCallback dbCallback) {
        this.dbCallback = dbCallback;
    }
    public abstract void getEvents(String uid);
    //public abstract void addEvent(Event)

}
