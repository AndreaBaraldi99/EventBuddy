package it.lanos.eventbuddy.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.Result;
import it.lanos.eventbuddy.data.source.entities.User;

public class EventViewModel extends ViewModel {

    private final IEventsRepository iEventsRepository;
    private MutableLiveData<Result> eventListLiveData;

    public EventViewModel(IEventsRepository iEventsRepository) {
        this.iEventsRepository = iEventsRepository;
    }

    public void addEvent(EventWithUsers event) {
        iEventsRepository.insertEvent(event);
    }

    public void fetchEvents(long lastUpdate) {
        eventListLiveData = iEventsRepository.fetchEvents(lastUpdate);
    }

    public MutableLiveData<Result> getEvents(long lastUpdate) {
        if (eventListLiveData == null) {
            fetchEvents(lastUpdate);
        }
        return eventListLiveData;
    }

}
