package it.lanos.eventbuddy.data.source.local.datasource;

import java.util.Map;

import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.User;

public abstract class BaseEventsLocalDataSource {
    protected EventsCallback eventsCallback;

    public void setEventsCallback(EventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
    }

    public abstract void getEvents();
    public abstract void insertEvent(EventWithUsers eventList);
    public abstract void insertEvent(Event event, Map<User, Boolean> users);
}
