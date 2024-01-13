package it.lanos.eventbuddy.data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;

public interface IUserRepository {
    MutableLiveData<Result> register(@NonNull String fullName, @NonNull String userName, @NonNull String email, @NonNull String password);
    MutableLiveData<Result> signIn(@NonNull String email, @NonNull String password);
    MutableLiveData<Result> resetPassword(@NonNull String email);
    FirebaseUser getCurrentUser();
    void signOut();
    MutableLiveData<Result> deleteUser();
    void changeUsername(@NonNull String newUsername);
    MutableLiveData<Result> changePassword(@NonNull String oldPassword, @NonNull String newPassword);
    MutableLiveData<Result> searchUsers(@NonNull String query);
    void addFriend(@NonNull User friend);
    void removeFriend(@NonNull User friend);
    MutableLiveData<Result> getFriends(long lastUpdate);
    MutableLiveData<Result> uploadProfileImage(Bitmap bitmap);
    MutableLiveData<Result> downloadProfileImage(String userId);

}
