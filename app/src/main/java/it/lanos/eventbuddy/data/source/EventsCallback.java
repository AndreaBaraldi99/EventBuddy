package it.lanos.eventbuddy.data.source;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.EventWithUsers;

public interface EventsCallback {

    //void onSuccessFromRemote(EventsApiResponse newsApiResponse, long lastUpdate);
    //void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<EventWithUsers> eventsList);
    void onFailureFromLocal(Exception exception);
    //void onNewsFavoriteStatusChanged(News news, List<News> favoriteNews);
    //void onNewsFavoriteStatusChanged(List<News> news);
    //void onDeleteFavoriteNewsSuccess(List<News> favoriteNews);
}
