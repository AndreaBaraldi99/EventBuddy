package it.lanos.eventbuddy.UI;

import androidx.lifecycle.ViewModel;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;

public class EventViewModel extends ViewModel {

    private final IEventsRepository iEventsRepository;

    public EventViewModel(IEventsRepository iEventsRepository) {
        this.iEventsRepository = iEventsRepository;
    }

    public void addEvent(EventWithUsers event) {iEventsRepository.insertEvent(event);}
}
