package it.lanos.eventbuddy.data.source.firebase.auth;

import static it.lanos.eventbuddy.util.Constants.PLACEHOLDER_IMAGE_URL;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.source.models.User;

public class UserDataSource extends BaseUserDataSource {
    private static final String TAG = UserDataSource.class.getSimpleName();
    private final FirebaseAuth mAuth;

    public UserDataSource() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    /***
     * Create a new user
     * @param email user email
     * @param password user password
     */
    @Override
    public void register(@NonNull String fullName, @NonNull String userName, @NonNull String email, @NonNull String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        authCallback.onSuccessFromFirebase(new User(currentUserId, userName, fullName, 0, PLACEHOLDER_IMAGE_URL));
                    } else {
                        authCallback.onFailureFromRemote(task.getException());
                    }
                });
    }


    /***
     * Log the user in
     * @param email user email
     * @param password user password
     */
    @Override
    public void signIn(@NonNull String email, @NonNull String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onSuccessFromFirebase(null);
                    } else {
                        authCallback.onFailureFromRemote(task.getException());
                    }
                });
    }

    /***
     * Log the user out
     */
    @Override
    public void signOut() {
        mAuth.signOut();
        authCallback.onSignOutSuccess();
    }

    /***
     * Delete the user account
     */
    public void deleteUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        signOut();
        assert currentUser != null;
        currentUser.delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        authCallback.onDeleteSuccess();
                    } else {
                        authCallback.onFailureFromRemote(task.getException());
                    }
                });
    }

    /***
     * Change the user password
     */
    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword){

        FirebaseUser currentUser = mAuth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(currentUser.getEmail(), oldPassword);

        currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                currentUser.updatePassword(newPassword);
                authCallback.onChangePasswordSuccess();
            } else {
                authCallback.onFailureFromRemote(task.getException());
            }
        });
    }

    /***
     * Send an email to the user to initiate a password reset
     * @param email user email
     */
    @Override
    public void resetPassword(@NonNull String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onResetPasswordSuccess();
                    } else {
                        authCallback.onFailureFromRemote(task.getException());
                    }
                });
    }
    @Override
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }




}
