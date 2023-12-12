package it.lanos.eventbuddy.data.source;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsCloudResponse;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsWithUsersFromCloudResponse;

public interface EventsCallback {

    void onSuccessFromRemote(List<EventsWithUsersFromCloudResponse> newsApiResponse);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<EventWithUsers> eventsList);
    void onFailureFromLocal(Exception exception);

    //void onNewsFavoriteStatusChanged(News news, List<News> favoriteNews);
    //void onNewsFavoriteStatusChanged(List<News> news);
    //void onDeleteFavoriteNewsSuccess(List<News> favoriteNews);
}
