package it.lanos.eventbuddy.UI;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.lanos.eventbuddy.data.IEventsRepository;

public class EventViewModelFactory implements ViewModelProvider.Factory{
    private final IEventsRepository iEventsRepository;

    public EventViewModelFactory(IEventsRepository iEventsRepository) {
        this.iEventsRepository = iEventsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventViewModel(iEventsRepository);
    }
}