package it.lanos.eventbuddy.UI;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;

public class FriendsViewModelFactory implements ViewModelProvider.Factory {
    private final IUserRepository iUserRepository;

    public FriendsViewModelFactory(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FriendsViewModel(iUserRepository);
    }
}
