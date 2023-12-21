package it.lanos.eventbuddy.UI.authentication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;

public class UserViewModel extends ViewModel {
    private IUserRepository userRepository;
    private MutableLiveData<Result> userLiveData;

    public UserViewModel(IUserRepository iUserRepository) {
        this.userRepository = iUserRepository;
        this.userLiveData = userRepository.getUserMutableLiveData();
    }

    public MutableLiveData<Result> getUserMutableLiveData() {
        return userLiveData;
    }

    public void register(String fullName, String userName, String email, String password) {
        userRepository.register(fullName, userName, email, password);
    }

    public void signIn(String email, String password) {
        userRepository.signIn(email, password);
    }

}
