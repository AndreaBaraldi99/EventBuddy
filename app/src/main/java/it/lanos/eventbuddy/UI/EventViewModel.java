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

    /*TODO: mi vengono restiuiti tutti gli eventi ma io li voglio di un det. utente fetchEvents(User user)
    *  affinchè riesca a visualizzarli nel modo corretto */
    public void fetchEvents() {
        eventListLiveData = iEventsRepository.fetchEvents();
    }

    public MutableLiveData<Result> getEvents() {
        if (eventListLiveData == null) {
            fetchEvents();
        }
        return eventListLiveData;
    }

}
