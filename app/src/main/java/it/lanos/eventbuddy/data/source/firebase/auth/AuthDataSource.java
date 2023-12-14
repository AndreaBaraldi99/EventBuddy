package it.lanos.eventbuddy.data.source.firebase.auth;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.CloudDBDataSource;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;

public class AuthDataSource extends BaseAuthDataSource {
    private static final String TAG = AuthDataSource.class.getSimpleName();
    private FirebaseAuth mAuth;
    private CloudDBDataSource cloudDBDataSource;


    public AuthDataSource() {
        this.mAuth = FirebaseAuth.getInstance();
        this.cloudDBDataSource = new CloudDBDataSource(FirebaseFirestore.getInstance());
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
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        createUserInDbs(currentUser, userName, fullName);
                        authCallback.onRegisterSuccess(currentUser);
                    } else {
                        authCallback.onRegisterFailure(task.getException());
                    }
                });
    }

    /***
     * Create an instance of User and save it in both the local and cloud databases
     * @param firebaseUser instance of FireBaseUser
     * @param username username
     * @param fullName name and surname
     */

    // TODO: 15/12/2023  add user in the local database
    public void createUserInDbs(FirebaseUser firebaseUser, String username, String fullName) {
        User user =  new User(firebaseUser.getUid(), username, fullName);
        cloudDBDataSource.addUser(user);

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
                        authCallback.onLoginSuccess(mAuth.getCurrentUser());
                    } else {
                        authCallback.onLoginFailure(task.getException());
                    }
                });
    }

    /***
     * Log the user out
     */
    @Override
    public void signOut() {
        mAuth.signOut();
    }

    /***
     * Delete the user account
     */
    public void deleteUser() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        authCallback.onDeleteSuccess();
                    } else {
                        authCallback.onDeleteFailure(task.getException());
                    }
                });
    }

    /***
     * Change the user password
     */
    @Override
    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword){
        FirebaseUser user = mAuth.getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onChangePasswordSuccess();
                    } else {
                        authCallback.onChangePasswordFailure(task.getException());
                    }
                });
    }
}
