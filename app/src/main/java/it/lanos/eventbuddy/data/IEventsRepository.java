package it.lanos.eventbuddy.data;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.Result;

public interface IEventsRepository {
    MutableLiveData<Result> fetchEvents();
    void insertEvent(EventWithUsers event);
}
