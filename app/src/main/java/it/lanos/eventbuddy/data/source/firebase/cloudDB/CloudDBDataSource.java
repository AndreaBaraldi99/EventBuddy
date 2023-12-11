package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import it.lanos.eventbuddy.data.source.entities.Event;

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
                    dbCallback.onGetEventsSuccess();
                })
                .addOnFailureListener(e -> {
                    dbCallback.onGetEventsFailure(e.getLocalizedMessage());
                });
    }


}
