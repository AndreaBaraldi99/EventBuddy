package it.lanos.eventbuddy.data.source.local.datasource;

import java.util.Map;

import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.entities.Event;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.User;

public abstract class BaseEventsLocalDataSource {
    protected EventsCallback eventsCallback;

    public void setEventsCallback(EventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
    }

    public abstract void getEvents();
    public abstract void insertEvent(EventWithUsers eventList);
    public abstract void insertEvent(Event event, Map<User, Boolean> users);
}
