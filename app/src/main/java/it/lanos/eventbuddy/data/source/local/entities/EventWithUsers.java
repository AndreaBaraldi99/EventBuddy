package it.lanos.eventbuddy.data.source.local.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class EventWithUsers {
    @Embedded
    private LocalEvent event;
    @Relation(
            parentColumn = "eventId",
            entityColumn = "userId",
            associateBy = @Junction(UserEventCrossRef.class)
    )
    private List<LocalUser> users;

    public void setEvent(LocalEvent event) {
        this.event = event;
    }

    public void setUsers(List<LocalUser> users) {
        this.users = users;
    }
}
