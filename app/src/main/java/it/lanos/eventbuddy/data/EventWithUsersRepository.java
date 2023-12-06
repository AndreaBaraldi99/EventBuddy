package it.lanos.eventbuddy.data;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.lanos.eventbuddy.data.source.BaseEventsLocalDataSource;
import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.Result;
import it.lanos.eventbuddy.data.source.entities.UserWithEvents;


public class EventWithUsersRepository implements IEventsRepository, EventsCallback{
    private static final String TAG = EventWithUsersRepository.class.getSimpleName();
    private final MutableLiveData<Result> allEventsMutableLiveData;
    //private final MutableLiveData<Result> favoriteNewsMutableLiveData;
    private final BaseEventsLocalDataSource eventsLocalDataSource;

    public EventWithUsersRepository(BaseEventsLocalDataSource eventsLocalDataSource) {

        allEventsMutableLiveData = new MutableLiveData<>();
        //favoriteNewsMutableLiveData = new MutableLiveData<>();
        //this.newsRemoteDataSource = newsRemoteDataSource;
        this.eventsLocalDataSource = eventsLocalDataSource;
        //this.newsRemoteDataSource.setNewsCallback(this);
        this.eventsLocalDataSource.setNewsCallback(this);
    }

    @Override
    public MutableLiveData<Result> fetchEvents() {
        //long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than FRESH_TIMEOUT value ago
       /* if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            newsRemoteDataSource.getNews(country);
        } else {
            newsLocalDataSource.getNews();
        }*/
        eventsLocalDataSource.getEvents();
        return allEventsMutableLiveData;
    }

    @Override
    public void onSuccessFromLocal(List<EventWithUsers> newsList) {
        Result.Success result = new Result.Success(newsList);
        allEventsMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allEventsMutableLiveData.postValue(resultError);
        //favoriteNewsMutableLiveData.postValue(resultError);
    }

    @Override
    public void insertEvent(EventWithUsers event) {
        eventsLocalDataSource.insertEvent(event);
    }
}
