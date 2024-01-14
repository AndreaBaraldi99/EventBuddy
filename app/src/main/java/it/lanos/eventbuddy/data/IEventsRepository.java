package it.lanos.eventbuddy.data;

import androidx.lifecycle.MutableLiveData;

import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;

public interface IEventsRepository {
    MutableLiveData<Result> fetchEvents(long lastUpdate);
    void insertEvent(EventWithUsers event);
    void joinEvent(String eventId);
    void leaveEvent(String eventId);
}
