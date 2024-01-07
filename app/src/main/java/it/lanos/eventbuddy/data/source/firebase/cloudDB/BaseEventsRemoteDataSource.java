package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;

public abstract class BaseEventsRemoteDataSource {
    protected EventsCallback eventsCallback;
    public void setEventsCallback(EventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
    }
    public abstract void getEvents(String uid);
    public abstract void addEvent(EventWithUsers event);
    public abstract void joinEvent(String eventId, String uid);
    public abstract void leaveEvent(String eventId, String uid);

}
