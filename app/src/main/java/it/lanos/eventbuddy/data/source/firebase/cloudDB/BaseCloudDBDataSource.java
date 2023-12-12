package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.firebase.auth.AuthCallback;

public abstract class BaseCloudDBDataSource {
    protected EventsCallback eventsCallback;
    public void setEventsCallback(EventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
    }
    public abstract void getEvents(String uid);
    //public abstract void addEvent(Event)

}
