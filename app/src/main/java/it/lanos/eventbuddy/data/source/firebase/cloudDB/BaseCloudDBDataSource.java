package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.entities.User;

public abstract class BaseCloudDBDataSource {
    protected EventsCallback eventsCallback;
    public void setEventsCallback(EventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
    }
    public abstract void getEvents(String uid);
    public abstract void addUser(User user);
    public abstract void addEvent(EventsCloudResponse event);
    //public abstract void addEvent(Event)

}
