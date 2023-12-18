package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

import it.lanos.eventbuddy.data.services.CloudDBService;
import it.lanos.eventbuddy.data.source.entities.User;

public class UserCloudDBDataSource extends BaseUserCloudDBDataSource {

    private final CloudDBService service;
    public UserCloudDBDataSource(CloudDBService service) {
        this.service = service;

    }
    @Override
    public void addUser(User user) {
        service.addUser(user).addOnSuccessListener(documentReference -> {
            userCallback.onSuccessFromOnlineDB(user);
        }).addOnFailureListener(e -> userCallback.onFailureFromRemote(e));
    }

    @Override
    public void getUser(String uid){
        service.getUser(uid).addOnSuccessListener(documentSnapshots -> {
           userCallback.onSuccessFromOnlineDB(documentSnapshots.toObjects(User.class).get(0));
        }).addOnFailureListener(e -> userCallback.onFailureFromRemote(e));
    }

    @Override
    public void searchUsers(String query) {
        service.getUsersByName(query).addOnSuccessListener(queryDocumentSnapshots -> {
            userCallback.onUserSearchedSuccess(queryDocumentSnapshots.toObjects(User.class));
        }).addOnFailureListener(e -> userCallback.onFailureFromRemote(e));
    }


}
