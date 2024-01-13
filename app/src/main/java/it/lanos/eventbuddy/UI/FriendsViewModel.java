package it.lanos.eventbuddy.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;


public class FriendsViewModel extends ViewModel {
    private final IUserRepository iUserRepository;

    public FriendsViewModel(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    public MutableLiveData<Result> getFriends(long lastUpdate) {
        //TODO: Gestire logica Result per Friends
        return iUserRepository.getFriends(lastUpdate);
    }


    public MutableLiveData<Result> searchUsers(String query){
        return iUserRepository.searchUsers(query);
    }

    public void addFriend(User friend){
        iUserRepository.addFriend(friend);
    }

    public void removeFriend(User friend){
        iUserRepository.removeFriend(friend);
    }

}
