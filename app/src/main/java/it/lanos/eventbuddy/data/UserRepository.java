package it.lanos.eventbuddy.data;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.List;

import it.lanos.eventbuddy.data.source.UserCallback;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.firebase.auth.BaseUserDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseUserCloudDBDataSource;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.datasource.BaseUserLocalDataSource;
import it.lanos.eventbuddy.util.DataEncryptionUtil;

public class UserRepository implements IUserRepository, UserCallback {

    private final BaseUserDataSource userDataSource;
    private final BaseUserCloudDBDataSource userCloudDBDataSource;
    private final BaseUserLocalDataSource userLocalDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> usersSearchedMutableLiveData;
    private final DataEncryptionUtil dataEncryptionUtil;
    private final Gson gson;

    public UserRepository(BaseUserDataSource userDataSource, BaseUserCloudDBDataSource userCloudDBDataSource, BaseUserLocalDataSource userLocalDataSource, DataEncryptionUtil dataEncryptionUtil, Gson gson){
        this.gson = gson;
        this.dataEncryptionUtil = dataEncryptionUtil;
        this.userDataSource = userDataSource;
        this.userDataSource.setAuthCallback(this);
        this.userCloudDBDataSource = userCloudDBDataSource;
        this.userCloudDBDataSource.setUserCallback(this);
        this.userLocalDataSource = userLocalDataSource;
        this.userLocalDataSource.setUserCallback(this);
        userMutableLiveData = new MutableLiveData<>();
        usersSearchedMutableLiveData = new MutableLiveData<>();
    }
    @Override
    public MutableLiveData<Result> signIn(@NonNull String email, @NonNull String password) {
        userDataSource.signIn(email, password);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> register(@NonNull String fullName, @NonNull String userName, @NonNull String email, @NonNull String password) {
        userDataSource.register(fullName, userName, email, password);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> resetPassword(@NonNull String email) {
        userDataSource.resetPassword(email);
        return userMutableLiveData;
    }

    @Override
    public void deleteUser() {
        userDataSource.deleteUser();
    }

    @Override
    public void signOut() {
        userDataSource.signOut();
    }

    public static void onSignOutSuccess() {
        EventsRoomDatabase.nukeTables();
    }

    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        userDataSource.changePassword(oldPassword, newPassword);
    }

    @Override
    public MutableLiveData<Result> searchUsers(@NonNull String query) {
        userCloudDBDataSource.searchUsers(query);
        return usersSearchedMutableLiveData;
    }


    @Override
    public void onSuccessFromFirebase(User user) {
        if(user == null) {
            //login
            Log.d("Debug", "Login success");
            userCloudDBDataSource.getUser(userDataSource.getCurrentUser().getUid());
            Result.AuthSuccess resultSuccess = new Result.AuthSuccess("Success");
            userMutableLiveData.postValue(resultSuccess);
        } else {
            //register
            Log.d("Debug", "Register success");
            userCloudDBDataSource.addUser(user);
            Result.AuthSuccess resultSuccess = new Result.AuthSuccess("Success");
            userMutableLiveData.postValue(resultSuccess);
        }
    }

    @Override
    public void onSuccessFromOnlineDB(User user) {
        Log.d("Debug", "Success from online db");
        try {
            dataEncryptionUtil.writeSecreteDataOnFile(ENCRYPTED_DATA_FILE_NAME, gson.toJson(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //userLocalDataSource.addUser(user);
    }

    @Override
    public void onSuccessFromLocalDB(User user) {

    }

    @Override
    public void onUserSearchedSuccess(List<User> users) {
        Result.UserSuccess resultSuccess = new Result.UserSuccess(users);
        usersSearchedMutableLiveData.postValue(resultSuccess);
    }

    @Override
    public void onDeleteSuccess() {
        EventsRoomDatabase.nukeTables();
    }

    @Override
    public void onChangePasswordSuccess() {

    }
    @Override
    public void onResetPasswordSuccess() {
        Result.AuthSuccess resultSuccess = new Result.AuthSuccess("Email sent");
        userMutableLiveData.postValue(resultSuccess);
    }

    @Override
    public void onFailureFromRemote(Exception e) {
        Result.Error resultError = new Result.Error(e.getLocalizedMessage());
        userMutableLiveData.postValue(resultError);
    }
}
