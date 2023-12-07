package it.lanos.eventbuddy.util.AuthenticationManager;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.util.AuthenticationManager.AuthManagerException.NullEmailException;
import it.lanos.eventbuddy.util.AuthenticationManager.AuthManagerException.NullUserException;

public class AuthManager {
    private final FirebaseAuth AUTH_INSTANCE;
    private final Context CONTEXT;

    public AuthManager(FirebaseAuth mAuth, Context context) {
        this.AUTH_INSTANCE = mAuth;
        this.CONTEXT = context;
    }

    public void createAccount(@NonNull String email, @NonNull String password, final AuthManagerCallback callback) {
        AUTH_INSTANCE.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    AuthManagerResponse response;
                    if (task.isSuccessful()) {
                        //Creation of user was successful
                        response = new AuthManagerResponse(task, CONTEXT.getString(R.string.successful_registration));
                        callback.onSuccess(response);
                    } else {
                        //Handle exceptions
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthWeakPasswordException) {
                            //Password is not strong enough
                            response = new AuthManagerResponse(exception, CONTEXT.getString(R.string.weak_password));
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            //Email is malformed
                            response = new AuthManagerResponse(exception, CONTEXT.getString(R.string.malformed_email));
                        } else if (exception instanceof FirebaseAuthUserCollisionException) {
                            //A user with this email already exists
                            response = new AuthManagerResponse(exception, CONTEXT.getString(R.string.already_existing_email));
                        } else {
                            //Generic error
                            response = new AuthManagerResponse(exception, CONTEXT.getString(R.string.registration_failed));
                        }
                        callback.onFailure(response);
                    }

                });
    }

    public void loginAccount(@NonNull String email, @NonNull String password, final AuthManagerCallback callback) {
        AUTH_INSTANCE.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    AuthManagerResponse response;
                    if (task.isSuccessful()) {
                        //Login was successful
                        response = new AuthManagerResponse(task, CONTEXT.getString(R.string.successful_login));
                        callback.onSuccess(response);
                    } else {
                        //Handle exceptions
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthInvalidUserException) {
                            //User with this email doesn't exists or has been disabled
                            response = new AuthManagerResponse(exception, CONTEXT.getString(R.string.user_doesnt_exists));
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            //Wrong password
                            response = new AuthManagerResponse(exception, CONTEXT.getString(R.string.wrong_password));
                        } else {
                            //Generic error
                            response = new AuthManagerResponse(exception, CONTEXT.getString(R.string.login_failed));
                        }
                        callback.onFailure(response);
                    }
                });
    }

    public void deleteAccount(final AuthManagerCallback callback) {
        FirebaseUser currentUser = AUTH_INSTANCE.getCurrentUser();

        if (currentUser != null) {
            //Logout
            AUTH_INSTANCE.signOut();

            currentUser.delete()
                    .addOnCompleteListener(task -> {
                        AuthManagerResponse response;
                        if (task.isSuccessful()) {
                            //Account successfully deleted
                            response = new AuthManagerResponse(task, CONTEXT.getString(R.string.account_deleted));
                            callback.onSuccess(response);
                        } else {
                            //Handle exceptions
                            Exception exception = task.getException();
                            response = new AuthManagerResponse(exception, CONTEXT.getString(R.string.delete_account_failed));
                            callback.onFailure(response);
                        }
                    });
        } else {
            AuthManagerResponse response = new AuthManagerResponse(new NullUserException(), CONTEXT.getString(R.string.null_user));
            callback.onFailure(response);
        }
    }

    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword, AuthManagerCallback callback) {
        FirebaseUser currentUser = AUTH_INSTANCE.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();

            if (email != null) {
                AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

                //Reauthenticate the user
                currentUser.reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            AuthManagerResponse reauthenticateResponse;
                            if (task.isSuccessful()) {
                                //Update the user password with the new password
                                currentUser.updatePassword(newPassword)
                                        .addOnCompleteListener(task1 -> {
                                            AuthManagerResponse updatePasswordResponse;

                                            if (task1.isSuccessful()) {
                                                //Password successfully updated
                                                updatePasswordResponse = new AuthManagerResponse(task1, CONTEXT.getString(R.string.password_successfully_updated));
                                                callback.onSuccess(updatePasswordResponse);
                                            } else {
                                                //Handle the exceptions
                                                Exception exception = task1.getException();

                                                if (exception instanceof FirebaseAuthWeakPasswordException) {
                                                    //Password is not strong enough
                                                    updatePasswordResponse = new AuthManagerResponse(exception, CONTEXT.getString(R.string.weak_password));
                                                } else if (exception instanceof FirebaseAuthInvalidUserException) {
                                                    //User doesn't exists or has been disabled
                                                    updatePasswordResponse = new AuthManagerResponse(exception, CONTEXT.getString(R.string.user_doesnt_exists));
                                                } else if (exception instanceof FirebaseAuthRecentLoginRequiredException) {
                                                    //User's last sign-in time does not meet the security threshold
                                                    updatePasswordResponse = new AuthManagerResponse(exception, CONTEXT.getString(R.string.security_threshold));
                                                } else {
                                                    //Generic error
                                                    updatePasswordResponse = new AuthManagerResponse(exception, CONTEXT.getString(R.string.update_password_failed));
                                                }
                                                callback.onFailure(updatePasswordResponse);
                                            }
                                        });
                            } else {
                                //Handle the exceptions
                                Exception exception = task.getException();

                                if (exception instanceof FirebaseAuthInvalidUserException) {
                                    //User doesn't exists or has been disabled
                                    reauthenticateResponse = new AuthManagerResponse(exception, CONTEXT.getString(R.string.user_doesnt_exists));
                                } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                    //Credential malformed or expired
                                    reauthenticateResponse = new AuthManagerResponse(exception, CONTEXT.getString(R.string.malformed_credential));
                                } else {
                                    //Generic error
                                    reauthenticateResponse = new AuthManagerResponse(exception, CONTEXT.getString(R.string.update_password_failed));
                                }
                                callback.onFailure(reauthenticateResponse);
                            }
                        });
            } else {
                AuthManagerResponse response = new AuthManagerResponse(new NullEmailException(), CONTEXT.getString(R.string.null_email));
                callback.onFailure(response);
            }
        } else {
            AuthManagerResponse response = new AuthManagerResponse(new NullUserException(), CONTEXT.getString(R.string.null_user));
            callback.onFailure(response);
        }
    }
}
