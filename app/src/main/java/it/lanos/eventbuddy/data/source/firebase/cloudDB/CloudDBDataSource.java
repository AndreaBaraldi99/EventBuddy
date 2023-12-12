package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import it.lanos.eventbuddy.data.source.entities.User;

public class CloudDBDataSource extends BaseCloudDBDataSource{
    private final CollectionReference usersRef;
    private final CollectionReference eventsRef;
    public CloudDBDataSource(FirebaseFirestore db) {
        this.usersRef = db.collection("users");
        this.eventsRef = db.collection("events");
    }
    @Override
    public void getEvents(String uid) {
        eventsRef.orderBy("invited." + uid).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<EventsWithUsersFromCloudResponse> eventsWithUsers = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventsCloudResponse responseEvent = document.toObject(EventsCloudResponse.class);
                        Map<User, Boolean> invited = new HashMap<>();
                        for (Map.Entry<String, Boolean> entry : responseEvent.getInvited().entrySet()) {
                            usersRef.whereEqualTo("uid", entry.getValue()).get().addOnSuccessListener(queryDocumentSnapshots1 -> {
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
}
