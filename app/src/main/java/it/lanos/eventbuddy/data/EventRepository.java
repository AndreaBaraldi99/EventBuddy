package it.lanos.eventbuddy.data;

import static it.lanos.eventbuddy.util.Constants.FRESH_TIMEOUT;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.entities.Event;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.Result;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseEventsCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsCloudResponse;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsWithUsersFromCloudResponse;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;


public class EventRepository implements IEventsRepository, EventsCallback{
    private static final String TAG = EventRepository.class.getSimpleName();
    private final MutableLiveData<Result> allEventsMutableLiveData;
    private final BaseEventsLocalDataSource eventsLocalDataSource;
    private final FirebaseUser user;
    private final BaseEventsCloudDBDataSource cloudDBDataSource;

    public EventRepository(BaseEventsLocalDataSource eventsLocalDataSource, BaseEventsCloudDBDataSource cloudDBDataSource, FirebaseUser user) {

        allEventsMutableLiveData = new MutableLiveData<>();
        this.eventsLocalDataSource = eventsLocalDataSource;
        this.cloudDBDataSource = cloudDBDataSource;
        this.eventsLocalDataSource.setEventsCallback(this);
        this.cloudDBDataSource.setEventsCallback(this);
        this.user = user;
    }

    @Override
    public MutableLiveData<Result> fetchEvents(long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            cloudDBDataSource.getEvents(user.getUid());
        } else {
            eventsLocalDataSource.getEvents();
        }
        return allEventsMutableLiveData;
    }

    @Override
    public void onSuccessFromRemote(List<EventsWithUsersFromCloudResponse> eventsCloudResponse) {
        for(EventsWithUsersFromCloudResponse event : eventsCloudResponse){
            eventsLocalDataSource.insertEvent(Event.fromCloudResponse(event.getEvent()), event.getUsers());
        }
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allEventsMutableLiveData.postValue(resultError);
    }

    @Override
    public void onSuccessFromLocal(List<EventWithUsers> eventList) {
        Result.Success result = new Result.Success(eventList);
        allEventsMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allEventsMutableLiveData.postValue(resultError);
    }

    @Override
    public void insertEvent(EventWithUsers event) {
        event.getEvent().setManager(user.getUid());
        cloudDBDataSource.addEvent(EventsCloudResponse.fromEventsWithUsers(event));
    }
    @Override
    public void joinEvent(String eventId) {
        cloudDBDataSource.joinEvent(eventId, user.getUid());
    }

    @Override
    public void leaveEvent(String eventId) {
        cloudDBDataSource.leaveEvent(eventId, user.getUid());
    }
}
