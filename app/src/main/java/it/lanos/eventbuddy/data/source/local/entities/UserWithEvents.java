package it.lanos.eventbuddy.data.source.local.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithEvents {
    @Embedded
    private LocalUser user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "eventId",
            associateBy = @Junction(UserEventCrossRef.class)
    )
    private List<LocalEvent> events;

    public void setUser(LocalUser user) {
        this.user = user;
    }

    public void setEvents(List<LocalEvent> events) {
        this.events = events;
    }
}


