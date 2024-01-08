package it.lanos.eventbuddy.UI;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.lanos.eventbuddy.data.ILocationRepository;

public class LocationViewModelFactory implements ViewModelProvider.Factory {
    private final ILocationRepository iLocationRepository;

    public LocationViewModelFactory(ILocationRepository iLocationRepository) {
        this.iLocationRepository = iLocationRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LocationViewModel(iLocationRepository);
    }
}