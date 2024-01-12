package it.lanos.eventbuddy.data.source.local.datasource;

import java.util.List;

import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.EventsWithUsersFromCloudResponse;

public abstract class BaseEventsLocalDataSource {
    protected EventsCallback eventsCallback;

    public void setEventsCallback(EventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
    }

    public abstract void getEvents();
    public abstract void insertEvent(List<EventsWithUsersFromCloudResponse> eventList);
    public abstract void insertEvent(EventWithUsers eventWithUsers);
    public abstract void joinEvent(String eventId, String uid);
    public abstract void leaveEvent(String eventId, String uid);
}
