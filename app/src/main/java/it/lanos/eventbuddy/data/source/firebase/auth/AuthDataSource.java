package it.lanos.eventbuddy.data.source.firebase.auth;

import com.google.firebase.auth.FirebaseAuth;

public class AuthDataSource extends BaseAuthDataSource {
    private FirebaseAuth mAuth;
    public AuthDataSource() {
        this.mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onLoginSuccess(mAuth.getCurrentUser());
                    } else {
                        authCallback.onLoginFailure(task.getException());
                    }
                });
    }

    @Override
    public void register(String email, String password) {

    }
}
