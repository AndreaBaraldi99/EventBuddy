package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.lanos.eventbuddy.data.services.CloudDBService;
import it.lanos.eventbuddy.data.source.entities.User;

public class CloudDBDataSource extends BaseCloudDBDataSource{
    CloudDBService service;
    public CloudDBDataSource(CloudDBService service) {
        this.service = service;
    }
    @Override
    public void getEvents(String uid) {
        service.getEvents(uid).addOnSuccessListener(queryDocumentSnapshots -> {
                    List<EventsWithUsersFromCloudResponse> eventsWithUsers = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventsCloudResponse responseEvent = document.toObject(EventsCloudResponse.class);
                        Map<User, Boolean> invited = new HashMap<>();
                        for (Map.Entry<String, Boolean> entry : responseEvent.getInvited().entrySet()) {
                            service.getUser(entry.getKey()).addOnSuccessListener(queryDocumentSnapshots1 -> {
                                for (QueryDocumentSnapshot document1 : queryDocumentSnapshots1) {
                                    User user = document1.toObject(User.class);
                                    invited.put(user, entry.getValue());
                                }
                            });
                        }
                        eventsWithUsers.add(new EventsWithUsersFromCloudResponse(responseEvent, invited));
                    }
                    eventsCallback.onSuccessFromRemote(eventsWithUsers);
                })
                .addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));
    }

    @Override
    public void addUser(User user) {
        service.addUser(user).addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));
    }

    @Override
    public void addEvent(EventsCloudResponse event) {
       service.addEvent(event).addOnSuccessListener(t -> {}).addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));
    }
    @Override
    public void joinEvent(String eventId, String uid){
       service.joinEvent(eventId, uid).addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));
    }
}
