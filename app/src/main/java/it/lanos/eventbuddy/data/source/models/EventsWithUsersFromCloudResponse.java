package it.lanos.eventbuddy.data.source.models;

import java.util.Map;

public class EventsWithUsersFromCloudResponse {
    EventsCloudResponse event;
    Map<User, Boolean> user;

    public EventsWithUsersFromCloudResponse() {
    }

    public EventsWithUsersFromCloudResponse(EventsCloudResponse event, Map<User, Boolean> user) {
        this.event = event;
        this.user = user;
    }

    public EventsCloudResponse getEvent() {
        return event;
    }

    public void setEvent(EventsCloudResponse event) {
        this.event = event;
    }

    public Map<User, Boolean> getUsers() {
        return user;
    }

    public void setUser(Map<User, Boolean> user) {
        this.user = user;
    }

}
