package it.lanos.eventbuddy.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import it.lanos.eventbuddy.data.source.UserCallback;
import it.lanos.eventbuddy.data.source.entities.Result;
import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.data.source.firebase.auth.UserDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.UserCloudDBDataSource;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.datasource.UserLocalDataSource;

public class UserRepository implements IUserRepository, UserCallback {
    private UserDataSource authDataSource;
    private UserCloudDBDataSource userCloudDBDataSource;
    private UserLocalDataSource userLocalDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    public UserRepository(UserDataSource authDataSource, UserCloudDBDataSource userCloudDBDataSource, UserLocalDataSource userLocalDataSource) {
        this.authDataSource = authDataSource;
        this.authDataSource.setAuthCallback(this);
        this.userCloudDBDataSource = userCloudDBDataSource;
        this.userLocalDataSource = userLocalDataSource;
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

    public static void onSignOutSuccess() {
        EventsRoomDatabase.nukeTables();
    }

    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        authDataSource.changePassword(oldPassword, newPassword);
    }


    @Override
    public void onSuccessFromFirebase(User user) {
        if(user == null) {
            //login
            userCloudDBDataSource.getUser(authDataSource.getCurrentUser().getUid());
        } else {
            //register
            userCloudDBDataSource.addUser(user);
        }
    }

    @Override
    public void onSuccessFromOnlineDB(User user) {
        userLocalDataSource.addUser(user);
    }

    @Override
    public void onSuccessFromLocalDB(User user) {

    }

    @Override
    public void onDeleteSuccess() {
        EventsRoomDatabase.nukeTables();
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
