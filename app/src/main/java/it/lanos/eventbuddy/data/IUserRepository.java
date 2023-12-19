package it.lanos.eventbuddy.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import it.lanos.eventbuddy.data.source.models.Result;

public interface IUserRepository {
    void register(@NonNull String fullName, @NonNull String userName, @NonNull String email, @NonNull String password);
    void signIn(@NonNull String email, @NonNull String password);
    void signOut();
    void deleteUser();
    void changePassword(@NonNull String oldPassword, @NonNull String newPassword);
    MutableLiveData<Result> searchUsers(@NonNull String query);
}
