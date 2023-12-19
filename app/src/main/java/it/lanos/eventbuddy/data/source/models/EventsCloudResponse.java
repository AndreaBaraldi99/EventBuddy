package it.lanos.eventbuddy.data.source.models;

import java.util.HashMap;
import java.util.Map;

public class EventsCloudResponse {
    private String uid;
    private String date;
    private String description;
    private Map<String, Boolean> invited;
    private String location;
    private String manager;
    private String name;
    public EventsCloudResponse() {}
    public EventsCloudResponse(String id, String date, String description, Map<String, Boolean> invited, String location, String manager, String name) {
        this.uid = id;
        this.date = date;
        this.description = description;
        this.invited = invited;
        this.location = location;
        this.manager = manager;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Boolean> getInvited() {
        return invited;
    }

    public String getLocation() {
        return location;
    }

    public String getManager() {
        return manager;
    }

    public String getName() {
        return name;
    }

    public static EventsCloudResponse fromEventsWithUsers(EventWithUsers event) {
        Map<String, Boolean> invited = new HashMap<>();
        for (User user : event.getUsers()) {
            invited.put(user.getUserId(), false);
        }
        return new EventsCloudResponse(event.getEvent().getEventId(), event.getEvent().getDate(), event.getEvent().getDescription(), invited, event.getEvent().getLocation(), event.getEvent().getManager(), event.getEvent().getName());
    }
}
