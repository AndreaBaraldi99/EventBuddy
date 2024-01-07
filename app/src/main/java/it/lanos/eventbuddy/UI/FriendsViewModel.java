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

    public FriendsViewModel(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    public MutableLiveData<Result> getFriends() {
        friendsListLiveData = iUserRepository.getFriends(0);
        return friendsListLiveData;
    }

    public void addFriend(User friend){
        iUserRepository.addFriend(friend);
    }

    public void removeFriend(User friend){
        iUserRepository.removeFriend(friend);
    }

}
