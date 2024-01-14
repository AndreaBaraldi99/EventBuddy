package it.lanos.eventbuddy.data;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.lanos.eventbuddy.util.Constants.FRESH_TIMEOUT;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.data.source.UserCallback;
import it.lanos.eventbuddy.data.source.firebase.bucket.BaseImageRemoteDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.BaseUserLocalDataSource;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.firebase.auth.BaseUserDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseUserRemoteDataSource;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.models.UserFromRemote;
import it.lanos.eventbuddy.util.DataEncryptionUtil;

public class UserRepository implements IUserRepository, UserCallback {

    private final BaseUserDataSource userDataSource;
    private final BaseUserRemoteDataSource userCloudDBDataSource;
    private final BaseUserLocalDataSource userLocalDataSource;
    private final BaseImageRemoteDataSource imageRemoteDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> usersSearchedMutableLiveData;
    private final DataEncryptionUtil dataEncryptionUtil;
    private final MutableLiveData<Result> friendsSearchedMutableLiveData;
    private User user;

    public UserRepository(BaseUserDataSource userDataSource, BaseUserRemoteDataSource userCloudDBDataSource, BaseUserLocalDataSource baseUserLocalDataSource, BaseImageRemoteDataSource baseImageRemoteDataSource, DataEncryptionUtil dataEncryptionUtil){
        this.dataEncryptionUtil = dataEncryptionUtil;
        this.userDataSource = userDataSource;
        this.userDataSource.setAuthCallback(this);
        this.userCloudDBDataSource = userCloudDBDataSource;
        this.userLocalDataSource = baseUserLocalDataSource;
        this.userCloudDBDataSource.setUserCallback(this);
        this.userLocalDataSource.setUserCallback(this);
        this.imageRemoteDataSource = baseImageRemoteDataSource;
        this.imageRemoteDataSource.setUserCallback(this);
        readUser(dataEncryptionUtil);
        userMutableLiveData = new MutableLiveData<>();
        usersSearchedMutableLiveData = new MutableLiveData<>();
        friendsSearchedMutableLiveData = new MutableLiveData<>();
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
    public MutableLiveData<Result> deleteUser() {
        userDataSource.deleteUser();
        return userMutableLiveData;
    }

    @Override
    public void signOut() {
        userDataSource.signOut();
    }

    public void onSignOutSuccess() {
        EventsRoomDatabase.nukeTables();
    }

    @Override
    public MutableLiveData<Result> changePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        userDataSource.changePassword(oldPassword, newPassword);
        return userMutableLiveData;
    }


    @Override
    public void changeUsername(@NonNull String newUsername) {
        userCloudDBDataSource.changeUsername(new User(user.getUserId(), newUsername, user.getFullName(), user.getIsFriend(), user.getProfilePictureUrl()));
    }

    @Override
    public MutableLiveData<Result> searchUsers(@NonNull String query) {
        if(!query.equals("")){
            userCloudDBDataSource.searchUsers(query);
        }
        usersSearchedMutableLiveData.postValue(null);

        return usersSearchedMutableLiveData;
    }


    @Override
    public void addFriend(@NonNull User friend) {
        userCloudDBDataSource.addFriend(user.getUserId(), friend);
    }

    @Override
    public void removeFriend(@NonNull User friend) {
        userCloudDBDataSource.removeFriend(user.getUserId(), friend);
    }

    @Override
    public MutableLiveData<Result> getFriends(long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            userCloudDBDataSource.getFriends(user.getUserId());
        } else {
            userLocalDataSource.getFriends();
        }
        return friendsSearchedMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> uploadProfileImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageRemoteDataSource.uploadImage(this.user, baos.toByteArray());
        return userMutableLiveData;
    }

    @Override
    public void onImageUploaded(User user) {
        userCloudDBDataSource.uploadImage(user);
    }

    @Override
    public void onSuccessFromFirebase(User user) {
        if(user == null) {
            //login
            Log.d("Debug", "Login success");
            userCloudDBDataSource.getUser(userDataSource.getCurrentUser().getUid());
        } else {
            //register
            Log.d("Debug", "Register success");
            UserFromRemote userFromRemote = new UserFromRemote(user.getUserId(), user.getUsername(), user.getFullName(), new ArrayList<>(), user.getProfilePictureUrl());
            userCloudDBDataSource.addUser(userFromRemote);
        }
    }

    @Override
    public void onSuccessFromOnlineDB(User user) {
        Log.d("Debug", "Success from online db");
        try {
            dataEncryptionUtil.writeSecreteDataOnFile(ENCRYPTED_DATA_FILE_NAME, new Gson().toJson(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.user = user;
        userLocalDataSource.updateUser(user);
        Result.AuthSuccess resultSuccess = new Result.AuthSuccess("Success");
        userMutableLiveData.postValue(resultSuccess);
    }

    @Override
    public void onSuccessFromLocalDB(List<User> user) {
        Result.UserSuccess resultSuccess = new Result.UserSuccess(user);
        friendsSearchedMutableLiveData.postValue(resultSuccess);
    }

    @Override
    public void onUserSearchedSuccess(List<User> users) {
        Result.UserSuccess resultSuccess = new Result.UserSuccess(users);
        usersSearchedMutableLiveData.postValue(resultSuccess);
    }

    @Override
    public void onFailureUserSearch(Exception e) {
        Result.Error resultError = new Result.Error(e.getLocalizedMessage());
        usersSearchedMutableLiveData.postValue(resultError);
    }

    @Override
    public void onDeleteSuccess() {
        EventsRoomDatabase.nukeTables();
        Result.AuthSuccess resultSuccess = new Result.AuthSuccess("Account deleted");
        userMutableLiveData.postValue(resultSuccess);
    }

    @Override
    public void onChangePasswordSuccess() {
        Result.AuthSuccess resultSuccess = new Result.AuthSuccess("Password changed");
        userMutableLiveData.postValue(resultSuccess);
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

    @Override
    public void onFriendFromRemoteSuccess(List<User> users) {
        userLocalDataSource.addFriends(users);
    }

    @Override
    public void onUpdatedFriendFromLocal(User user) {
        Result allFriends = friendsSearchedMutableLiveData.getValue();
        if(allFriends != null && allFriends.isSuccess()){
            List<User> friends = ((Result.UserSuccess) allFriends).getData();
            if(user.getIsFriend() == 0){
                friends.remove(user);
            } else {
                friends.add(user);
            }
            friendsSearchedMutableLiveData.postValue(allFriends);
        }
    }

    @Override
    public void onFailureFriendSearched(Exception e) {
        Result.Error resultError = new Result.Error(e.getLocalizedMessage());
        friendsSearchedMutableLiveData.postValue(resultError);
    }

    @Override
    public void onImageUploadFailed(Exception e) {
        Result.Error resultError = new Result.Error(e.getLocalizedMessage());
        userMutableLiveData.postValue(resultError);
    }

    @Override
    public void onFriendUpdatedToRemote(User user) {
        userLocalDataSource.updateFriend(user);
    }

    @Override
    public void onFailureToUpdateFriend(Exception e) {
        Result.Error resultError = new Result.Error(e.getLocalizedMessage());
        friendsSearchedMutableLiveData.postValue(resultError);
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return userDataSource.getCurrentUser();
    }

    private void readUser(DataEncryptionUtil dataEncryptionUtil){
        try {
            this.user = new Gson().fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


