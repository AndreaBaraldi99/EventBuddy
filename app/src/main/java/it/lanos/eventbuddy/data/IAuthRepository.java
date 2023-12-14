package it.lanos.eventbuddy.data;

import androidx.annotation.NonNull;

public interface IAuthRepository {
    void register(@NonNull String fullName, @NonNull String userName, @NonNull String email, @NonNull String password);
    void signIn(@NonNull String email, @NonNull String password);
    void signOut();
    void deleteUser();
    void changePassword(@NonNull String oldPassword, @NonNull String newPassword);
}
