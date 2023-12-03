package it.lanos.eventbuddy.util.AuthenticationManager;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import it.lanos.eventbuddy.R;

public class AuthManager {
    private FirebaseAuth mAuth;
    private Context context;

    public AuthManager(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public void createAccount(@NonNull String email, @NonNull String password, final AuthManagerCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    AuthManagerResponse response;
                    if (task.isSuccessful()) {
                        //Creation of user was successful
                        response = new AuthManagerResponse(task, true, context.getString(R.string.successful_registration));
                    } else {
                        //Handle the exceptions
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthWeakPasswordException) {
                            //Password is not strong enough
                            response = new AuthManagerResponse(exception, false, context.getString(R.string.weak_password));
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            //Email is malformed
                            response = new AuthManagerResponse(exception, false, context.getString(R.string.malformed_email));
                        } else if (exception instanceof FirebaseAuthUserCollisionException) {
                            //A user with this email already exists
                            response = new AuthManagerResponse(exception, false, context.getString(R.string.already_existing_email));
                        } else {
                            //Generic error
                            response = new AuthManagerResponse(exception, false, context.getString(R.string.registration_failed));
                            callback.onFailure(response);
                        }
                    }
                    //Call the callback method with response
                    callback.onComplete(response);
                });
    }
}
