package it.lanos.eventbuddy.data.source.models;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithEvents {
    @Embedded
    private User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "eventId",
            associateBy = @Junction(UserEventCrossRef.class)
    )
    private List<Event> events;

    User getUser() {
        return user;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}


