package it.lanos.eventbuddy.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;


public class FriendsViewModel extends ViewModel {
    private final IUserRepository iUserRepository;
    //TODO: Gestire logica Result per Friends
    private MutableLiveData<Result> friendsListLiveData;

    private MutableLiveData<Result> friendsSearchedListLiveData;

    public FriendsViewModel(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    public MutableLiveData<Result> getFriends(long lastUpdate) {
        friendsListLiveData = iUserRepository.getFriends(lastUpdate);
        return friendsListLiveData;
    }

    public MutableLiveData<Result> attachSearchUsers(){
        return this.friendsSearchedListLiveData = iUserRepository.attachSearchUsers();
    }

    public void searchUsers(String query){
        iUserRepository.searchUsers(query);
    }

    public void addFriend(User friend){
        iUserRepository.addFriend(friend);
    }

    public void removeFriend(User friend){
        iUserRepository.removeFriend(friend);
    }

}
