package it.lanos.eventbuddy.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.Result;

public class FriendsViewModel extends ViewModel {
    private final IUserRepository iUserRepository;
    //TODO: Gestire logica Result per Friends
    //private MutableLiveData<> friendsListLiveData;

    public FriendsViewModel(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    public void fetchFriends(long lastUpdate) {
        //TODO:Unire logica per prendere lista amici
    }

    /*public MutableLiveData<Result> getFriends(long lastUpdate) {
        if (friendsListLiveData == null) {
            fetchFriends(lastUpdate);
        }
        return friendsListLiveData;
    }*/ //TODO: Togliere commento una volta implementata logica Result
}
