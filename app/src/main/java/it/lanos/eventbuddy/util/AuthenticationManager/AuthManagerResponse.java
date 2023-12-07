package it.lanos.eventbuddy.util.AuthenticationManager;

import com.google.android.gms.tasks.Task;

public class AuthManagerResponse {
    private Task<?> resultTask;
    private Exception exception;
    private String response;

    public AuthManagerResponse(Task<?> resultTask, String response) {
        this.resultTask = resultTask;
        this.response = response;
    }

    public AuthManagerResponse(Exception exception, String response) {
        this.exception = exception;
        this.response = response;
    }

    public Task<?> getResultTask() {
        return resultTask;
    }

    public Exception getException() {
        return exception;
    }

    public String getResponse() {
        return response;
    }
}
