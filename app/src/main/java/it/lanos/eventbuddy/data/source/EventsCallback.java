package it.lanos.eventbuddy.data.source;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsWithUsersFromCloudResponse;

public interface EventsCallback {

    void onSuccessFromRemote(List<EventsWithUsersFromCloudResponse> eventsFromCloudResponse);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<EventWithUsers> eventsList);
    void onFailureFromLocal(Exception exception);

}
