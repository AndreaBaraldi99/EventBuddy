package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import it.lanos.eventbuddy.data.source.EventsCallback;

public abstract class BaseEventsCloudDBDataSource {
    protected EventsCallback eventsCallback;
    public void setEventsCallback(EventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
    }
    public abstract void getEvents(String uid);
    public abstract void addEvent(EventsCloudResponse event);
    public abstract void joinEvent(String eventId, String uid);

}
