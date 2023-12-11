package it.lanos.eventbuddy.data.source.firebase.cloudDB;

public interface CloudDBCallback {

    void onGetEventsSuccess();
    void onGetEventsFailure(String message);

}
