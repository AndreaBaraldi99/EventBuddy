package it.lanos.eventbuddy.util.AuthenticationManager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class AuthManagerResponse {
    private Task<AuthResult> authResultTask;
    private Task<Void> voidResultTask;
    private Exception exception;
    private boolean successful;
    private String response;

    public AuthManagerResponse(Task<AuthResult> authResultTask, boolean successful, String response) {
        this.authResultTask = authResultTask;
        this.successful = successful;
        this.response = response;
    }
    public AuthManagerResponse(Exception exception, boolean successful, String response) {
        this.exception = exception;
        this.successful = successful;
        this.response = response;
    }

    public Task<AuthResult> getAuthResultTask() {
        return authResultTask;
    }

    public Task<Void> getVoidResultTask() {
        return voidResultTask;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getResponse() {
        return response;
    }
}
