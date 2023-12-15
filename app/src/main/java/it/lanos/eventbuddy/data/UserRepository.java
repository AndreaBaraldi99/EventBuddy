package it.lanos.eventbuddy.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import it.lanos.eventbuddy.data.source.entities.Result;
import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.data.source.UserCallback;
import it.lanos.eventbuddy.data.source.firebase.auth.UserDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsEventsCloudDBDataSource;

public class UserRepository implements IUserRepository, UserCallback {
    private UserDataSource authDataSource;
    private EventsEventsCloudDBDataSource eventsCloudDBDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    public UserRepository(UserDataSource authDataSource) {
        this.authDataSource = authDataSource;
        this.authDataSource.setAuthCallback(this);
        this.eventsCloudDBDataSource = new EventsEventsCloudDBDataSource(FirebaseFirestore.getInstance());
        userMutableLiveData = new MutableLiveData<>();
    }
    @Override
    public void signIn(@NonNull String email, @NonNull String password) {
        authDataSource.signIn(email, password);
    }

    @Override
    public void register(@NonNull String fullName, @NonNull String userName, @NonNull String email, @NonNull String password) {
        authDataSource.register(fullName, userName, email, password);
    }

    @Override
    public void deleteUser() {
        authDataSource.deleteUser();
    }

    @Override
    public void signOut() {
        authDataSource.signOut();
    }

    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        authDataSource.changePassword(oldPassword, newPassword);
    }

    // TODO: 15/12/2023  add user in Local
    @Override
    public void onRegisterSuccess(User user) {
        eventsCloudDBDataSource.addUser(user);
    }

    @Override
    public void onLoginSuccess(FirebaseUser user) {

    }

    @Override
    public void onDeleteSuccess() {

    }

    @Override
    public void onChangePasswordSuccess() {

    }

    @Override
    public void onFailureFromRemote(Exception e) {
        Result.Error resultError = new Result.Error(e.getMessage());
        userMutableLiveData.postValue(resultError);
    }
}
