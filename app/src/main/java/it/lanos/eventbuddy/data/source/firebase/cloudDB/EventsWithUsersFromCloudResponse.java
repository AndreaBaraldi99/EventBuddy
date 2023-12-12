package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import java.util.List;

public class EventsWithUsersFromCloudResponse {
    EventsCloudResponse event;
    List<UsersCloudResponse> user;

    public EventsWithUsersFromCloudResponse() {
    }

    public EventsWithUsersFromCloudResponse(EventsCloudResponse event, List<UsersCloudResponse> user) {
        this.event = event;
        this.user = user;
    }

    public EventsCloudResponse getEvent() {
        return event;
    }

    public void setEvent(EventsCloudResponse event) {
        this.event = event;
    }

    public List<UsersCloudResponse> getUsers() {
        return user;
    }

    public void setUser(List<UsersCloudResponse> user) {
        this.user = user;
    }
}
