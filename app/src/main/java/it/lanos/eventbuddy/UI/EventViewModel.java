package it.lanos.eventbuddy.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;

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
            fetchEvents(lastUpdate);
        return eventListLiveData;
    }

    public void joinEvent(String eventId){
        iEventsRepository.joinEvent(eventId);
    }

    public void leaveEvent(String eventId){
        iEventsRepository.leaveEvent(eventId);
    }

}
