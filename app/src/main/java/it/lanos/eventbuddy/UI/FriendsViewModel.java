package it.lanos.eventbuddy.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;


public class FriendsViewModel extends ViewModel {
    private final IUserRepository iUserRepository;
    private MutableLiveData<Result> usersListLiveData;
    private MutableLiveData<Result> usersSearchedListLiveData;

    public FriendsViewModel(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    public MutableLiveData<Result> getFriends(long lastUpdate) {
        this.usersListLiveData = iUserRepository.getFriends(lastUpdate);
        return usersListLiveData;
    }


    public MutableLiveData<Result> searchUsers(String query){
        this.usersSearchedListLiveData = iUserRepository.searchUsers(query);
        return usersSearchedListLiveData;
    }

    public void addFriend(User friend){
        iUserRepository.addFriend(friend);
    }

    public void removeFriend(User friend){
        iUserRepository.removeFriend(friend);
    }

    public FirebaseUser getCurrentUser() {
        return iUserRepository.getCurrentUser();
    }

}
