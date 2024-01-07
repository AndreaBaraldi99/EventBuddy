package it.lanos.eventbuddy.data.source.firebase.cloudDB;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.data.services.CloudDBService;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserFromRemote;

public class UserRemoteDataSource extends BaseUserRemoteDataSource {

    private final CloudDBService service;
    public UserRemoteDataSource(CloudDBService service) {
        this.service = service;

    }
    @Override
    public void addUser(UserFromRemote user) {
        service.addUser(user).addOnSuccessListener(documentReference -> {
            userCallback.onSuccessFromOnlineDB(new User(user.getUserId(), user.getUsername(), user.getFullName()));
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
        }).addOnFailureListener(e -> userCallback.onFailureUserSearch(e));
    }

    @Override
    public void changeUsername(User newUser) {
        service.changeUsername(newUser).addOnSuccessListener(aVoid -> {
            userCallback.onSuccessFromOnlineDB(newUser);
        }).addOnFailureListener(e -> userCallback.onFailureFromRemote(e));
    }

    @Override
    public void getFriends(String uid) {
        service.getUser(uid).addOnSuccessListener(documentSnapshots -> {
            List<Task<Void>> tasks = new ArrayList<>();
            List<String> friends = documentSnapshots.toObjects(UserFromRemote.class).get(0).getFriends();
            List<User> friendsList = new ArrayList<>();
            for(String friendId : friends){
                Task<Void> userTask = service.getUser(friendId).onSuccessTask(queryDocumentSnapshots1 -> {
                    friendsList.addAll(queryDocumentSnapshots1.toObjects(User.class));
                    return Tasks.forResult(null);
                });
                tasks.add(userTask);
            }
            Tasks.whenAll(tasks)
                    .addOnSuccessListener(result -> userCallback.onFriendFromRemoteSuccess(friendsList))
                    .addOnFailureListener(e -> userCallback.onFailureUserSearch(e));
        }).addOnFailureListener(e -> userCallback.onFailureUserSearch(e));
    }

    @Override
    public void addFriend(String uid, User friend) {
        service.addFriend(uid, friend.getUserId()).addOnSuccessListener(aVoid -> {
            friend.setIsFriend(1);
            userCallback.onFriendUpdatedToRemote(friend);
        }).addOnFailureListener(e -> userCallback.onFailureFriendSearched(e));
    }

    @Override
    public void removeFriend(String uid, User friend) {
        service.removeFriend(uid, friend.getUserId()).addOnSuccessListener(aVoid -> {
            friend.setIsFriend(0);
            userCallback.onFriendUpdatedToRemote(friend);
        }).addOnFailureListener(e -> userCallback.onFailureFriendSearched(e));
    }


}
