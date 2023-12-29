package it.lanos.eventbuddy.UI.authentication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;

public class UserViewModel extends ViewModel {
    private IUserRepository userRepository;
    private MutableLiveData<Result> userLiveData;

    public UserViewModel(IUserRepository iUserRepository) {
        this.userRepository = iUserRepository;
    }

    public MutableLiveData<Result> register(String fullName, String userName, String email, String password) {
        return userLiveData = userRepository.register(fullName, userName, email, password);
    }

    public MutableLiveData<Result> signIn(String email, String password) {
        return userLiveData = userRepository.signIn(email, password);
    }

    public MutableLiveData<Result> changePassword(String oldPassword, String newPassword) {
        return userLiveData = userRepository.changePassword(oldPassword, newPassword);
    }

    public MutableLiveData<Result> resetPassword(String email) {
        return userLiveData = userRepository.resetPassword(email);
    }

    public void signOut() {
        userRepository.signOut();
    }

    public MutableLiveData<Result> deleteUser() {
        return userLiveData = userRepository.deleteUser();
    }

    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }
}
