package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CloudDBDataSource extends BaseCloudDBDataSource{
    private final CollectionReference usersRef;
    private final CollectionReference eventsRef;
    public CloudDBDataSource(FirebaseFirestore db) {
        this.usersRef = db.collection("users");
        this.eventsRef = db.collection("events");
    }
    @Override
    public void getEvents(String uid) {
        eventsRef.whereArrayContains("invited", uid).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<EventsWithUsersFromCloudResponse> eventsWithUsers = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        EventsCloudResponse responseEvent = document.toObject(EventsCloudResponse.class);
                        List<UsersCloudResponse> users = new ArrayList<>();
                        for (Map<String,Object> invited : responseEvent.getInvited().values()) {
                            DocumentReference reference = (DocumentReference) invited.get("ref");
                            reference.get().addOnSuccessListener(documentSnapshot -> {
                                UsersCloudResponse responseUser = documentSnapshot.toObject(UsersCloudResponse.class);
                                responseUser.setIsInvited((boolean) invited.get("isInvited"));
                                users.add(responseUser);
                            });
                        }
                        eventsWithUsers.add(new EventsWithUsersFromCloudResponse(responseEvent, users));
                    }
                    eventsCallback.onSuccessFromRemote(eventsWithUsers);
                })
                .addOnFailureListener(e -> eventsCallback.onFailureFromRemote(e));
    }
}
