package it.lanos.eventbuddy.data.source;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.Event;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;

public abstract class BaseEventsLocalDataSource {
    protected EventsCallback eventsCallback;

    public void setNewsCallback(EventsCallback eventsCallback) {
        this.eventsCallback = eventsCallback;
    }

    public abstract void getEvents();
    //public abstract void getFavoriteNews();
    //public abstract void updateNews(News news);
    //public abstract void deleteFavoriteNews();
    public abstract void insertEvent(EventWithUsers eventList);
}
