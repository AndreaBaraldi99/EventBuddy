package it.lanos.eventbuddy.data.source;

import java.util.List;

import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.EventsWithUsersFromCloudResponse;

public interface EventsCallback {

    void onSuccessFromRemote(List<EventsWithUsersFromCloudResponse> eventsFromCloudResponse);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<EventWithUsers> eventsList);
    void onFailureFromLocal(Exception exception);
    void onJoinedEventFromRemote(String eventId);
    void onJoinStatusChangedFromLocal(EventWithUsers event);

}
