package it.lanos.eventbuddy.data.services;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthCredential;

public class AuthService {
    private FirebaseAuth mAuth;
    public AuthService(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public Task<AuthResult> register(@NonNull String email, @NonNull String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signIn(@NonNull String email, @NonNull String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signOut() {
        mAuth.signOut();

    }

    public Task<Void>  deleteUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser.delete();
    }


    public Task<Void> changePassword(@NonNull String newPassword) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser.updatePassword(newPassword);
    }

    public Task<Void> resetPassword(@NonNull String email) {
        return mAuth.sendPasswordResetEmail(email);
    }
}
