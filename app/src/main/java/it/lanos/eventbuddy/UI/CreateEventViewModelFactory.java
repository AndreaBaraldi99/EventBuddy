package it.lanos.eventbuddy.UI;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.ISuggestionsRepository;

public class CreateEventViewModelFactory implements ViewModelProvider.Factory{
    private final ISuggestionsRepository iSuggestionsRepository;

    public CreateEventViewModelFactory(ISuggestionsRepository iSuggestionsRepository) {
        this.iSuggestionsRepository = iSuggestionsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CreateEventViewModel(iSuggestionsRepository);
    }

}
