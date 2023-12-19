package it.lanos.eventbuddy.data.source.models;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class EventWithUsers {
    @Embedded
    private Event event;
    @Relation(
            parentColumn = "eventId",
            entityColumn = "userId",
            associateBy = @Junction(UserEventCrossRef.class)
    )
    private List<User> users;

    public EventWithUsers(Event event, List<User> users) {
        this.event = event;
        this.users = users;
    }

    @Relation(
            entity = UserEventCrossRef.class,
            parentColumn = "eventId",
            entityColumn = "eventId"
    )
    private List<UserEventCrossRef> userEventCrossRefs;

    public List<UserEventCrossRef> getUserEventCrossRefs() {
        return userEventCrossRefs;
    }

    public void setUserEventCrossRefs(List<UserEventCrossRef> userEventCrossRefs) {
        this.userEventCrossRefs = userEventCrossRefs;
    }

    public Event getEvent() {
        return event;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
