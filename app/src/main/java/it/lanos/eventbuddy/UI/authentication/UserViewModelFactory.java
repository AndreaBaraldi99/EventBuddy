package it.lanos.eventbuddy.UI.authentication;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.lanos.eventbuddy.data.IUserRepository;

public class UserViewModelFactory implements ViewModelProvider.Factory {
    private final IUserRepository iUserRepository;

    public UserViewModelFactory(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    /*
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(iUserRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
     */

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserViewModel(iUserRepository);
    }

}
