package it.lanos.eventbuddy.data;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.EventWithUsers;

public interface IEventsRepository {
    List<EventWithUsers> fetchEvents();
    void insertEvent(EventWithUsers eventWithUsers);
}
