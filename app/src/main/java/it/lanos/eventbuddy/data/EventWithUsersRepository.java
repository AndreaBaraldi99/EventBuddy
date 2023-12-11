package it.lanos.eventbuddy.data;

import android.os.Handler;
import android.os.Looper;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;
import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.Result;


public class EventWithUsersRepository implements IEventsRepository, EventsCallback{
    private static final String TAG = EventWithUsersRepository.class.getSimpleName();
    private final MutableLiveData<Result> allEventsMutableLiveData;
    //private final MutableLiveData<Result> favoriteNewsMutableLiveData;
    private final BaseEventsLocalDataSource eventsLocalDataSource;
    private final RxDataStore<Preferences> dataStore;
    private String userId;

    public EventWithUsersRepository(BaseEventsLocalDataSource eventsLocalDataSource, RxDataStore<Preferences> dataStore) {

        allEventsMutableLiveData = new MutableLiveData<>();
        this.dataStore = dataStore;
        //favoriteNewsMutableLiveData = new MutableLiveData<>();
        //this.newsRemoteDataSource = newsRemoteDataSource;
        this.eventsLocalDataSource = eventsLocalDataSource;
        //this.newsRemoteDataSource.setNewsCallback(this);
        this.eventsLocalDataSource.setNewsCallback(this);
        setUserId();
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
        event.getEvent().setManager(userId);
        eventsLocalDataSource.insertEvent(event);
    }
    private void setUserId(){
        Preferences.Key<String> userIdKey = PreferencesKeys.stringKey("userId");
        Flowable<String> userId = dataStore.data().map(preferences -> preferences.get(userIdKey));
        Disposable subscribe = userId.subscribe(value -> this.userId = value);
        if(subscribe.isDisposed()){
            subscribe.dispose();
        }
    }
}
