package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

    public void getUser(String uid){
        service.getUser(uid).addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                User user = document.toObject(User.class);
                userCallback.onSuccessFromOnlineDB(user);
            }
        }).addOnFailureListener(e -> userCallback.onFailureFromRemote(e));
    }


}
