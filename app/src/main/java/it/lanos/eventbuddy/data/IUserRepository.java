package it.lanos.eventbuddy.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.source.models.Result;

public interface IUserRepository {
    MutableLiveData<Result> register(@NonNull String fullName, @NonNull String userName, @NonNull String email, @NonNull String password);
    MutableLiveData<Result> signIn(@NonNull String email, @NonNull String password);
    MutableLiveData<Result> resetPassword(@NonNull String email);
    FirebaseUser getCurrentUser();
    void signOut();
    MutableLiveData<Result> deleteUser();
    void changePassword(@NonNull String oldPassword, @NonNull String newPassword);
    void changeUsername(@NonNull String newUsername);
    MutableLiveData<Result> searchUsers(@NonNull String query);

}
