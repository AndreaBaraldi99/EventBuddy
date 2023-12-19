package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import it.lanos.eventbuddy.data.services.CloudDBService;
import it.lanos.eventbuddy.data.source.models.User;

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
