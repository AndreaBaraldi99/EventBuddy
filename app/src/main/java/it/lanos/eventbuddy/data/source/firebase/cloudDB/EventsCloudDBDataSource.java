package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.lanos.eventbuddy.data.services.CloudDBService;
import it.lanos.eventbuddy.data.source.entities.User;

public class EventsCloudDBDataSource extends BaseEventsCloudDBDataSource {
    private final CloudDBService service;
    public EventsCloudDBDataSource(CloudDBService service) {
        this.service = service;
    }
    @Override
    public void getEvents(String uid) {
        service.getEvents(uid).addOnSuccessListener(queryDocumentSnapshots -> {
            List<Task<Void>> tasks = new ArrayList<>();

            List<EventsWithUsersFromCloudResponse> eventsWithUsers = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                EventsCloudResponse responseEvent = document.toObject(EventsCloudResponse.class);
                Map<User, Boolean> invited = new HashMap<>();

                for (Map.Entry<String, Boolean> entry : responseEvent.getInvited().entrySet()) {
                    Task<Void> userTask = service.getUser(entry.getKey()).onSuccessTask(queryDocumentSnapshots1 -> {
                        for (QueryDocumentSnapshot document1 : queryDocumentSnapshots1) {
                            User user = document1.toObject(User.class);
                            invited.put(user, entry.getValue());
                        }
                        return Tasks.forResult(null);
                    });

                    tasks.add(userTask);
                }

                eventsWithUsers.add(new EventsWithUsersFromCloudResponse(responseEvent, invited));
            }

            // Attendere fino a quando tutte le chiamate asincrone sono complete
            Tasks.whenAll(tasks)
                    .addOnSuccessListener(result -> eventsCallback.onSuccessFromRemote(eventsWithUsers))
                    .addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));

        }).addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));
    }
    @Override
    public void addEvent(EventsCloudResponse event) {
       service.addEvent(event).addOnSuccessListener(t -> {}).addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));
    }
    @Override
    public void joinEvent(String eventId, String uid){
       service.joinEvent(eventId, uid).addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));
    }
    @Override
    public void leaveEvent(String eventId, String uid){
        service.leaveEvent(eventId, uid).addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));
    }
}
