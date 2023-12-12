package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import com.google.firebase.firestore.DocumentReference;

import java.util.Map;
import java.util.Objects;

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
}
