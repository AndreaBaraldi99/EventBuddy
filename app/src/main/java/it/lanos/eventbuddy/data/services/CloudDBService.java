package it.lanos.eventbuddy.data.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.EventsCloudResponse;
import it.lanos.eventbuddy.data.source.models.UserFromRemote;

public class CloudDBService {
    private final CollectionReference usersRef;
    private final CollectionReference eventsRef;
    public CloudDBService(FirebaseFirestore db){
        this.usersRef = db.collection("users");
        this.eventsRef = db.collection("events");
    }
    public Task<QuerySnapshot> getUser(String uid){
        return usersRef.whereEqualTo("userId", uid).get();
    }
    public Task<QuerySnapshot> getUsersByName(String username){
        return usersRef.where(Filter.greaterThanOrEqualTo("username", username)).where(Filter.lessThanOrEqualTo("username", username + "\uf8ff")).get();
    }
    public Task<QuerySnapshot> getEvents(String uid){
        return eventsRef.orderBy("invited." + uid).get();
    }
    public Task<Void> addUser(UserFromRemote user){
        return usersRef.document(user.getUserId()).set(user);
    }
    public Task<Void> addEvent(EventsCloudResponse event){
        return eventsRef.document(event.getUid()).set(event);
    }
    public Task<Void> joinEvent(String eventId, String uid){
        DocumentReference a = eventsRef.document(eventId);
        return eventsRef.document(eventId).update("invited." + uid, true);
    }
    public Task<Void> leaveEvent(String eventId, String uid){
        return eventsRef.document(eventId).update("invited." + uid, false);
    }
    public Task<Void> changeUsername(User user){
        return usersRef.document(user.getUserId()).update("username", user.getUsername());
    }
    public Task<Void> addFriend(String uid, String friendId){
        return usersRef.document(uid).update("friends", FieldValue.arrayUnion(friendId));
    }
    public Task<Void> removeFriend(String uid, String friendId){
        return usersRef.document(uid).update("friends", FieldValue.arrayRemove(friendId));
    }
}
