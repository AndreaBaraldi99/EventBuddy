package it.lanos.eventbuddy.data;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.lanos.eventbuddy.util.Constants.FRESH_TIMEOUT;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.List;

import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseEventsRemoteDataSource;
import it.lanos.eventbuddy.data.source.models.EventsWithUsersFromCloudResponse;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.DataEncryptionUtil;


public class EventRepository implements IEventsRepository, EventsCallback{
    private static final String TAG = EventRepository.class.getSimpleName();
    private final MutableLiveData<Result> allEventsMutableLiveData;
    private final BaseEventsLocalDataSource eventsLocalDataSource;
    private User user;
    private final BaseEventsRemoteDataSource cloudDBDataSource;

    public EventRepository(BaseEventsLocalDataSource eventsLocalDataSource, BaseEventsRemoteDataSource cloudDBDataSource, DataEncryptionUtil dataEncryptionUtil) {
        allEventsMutableLiveData = new MutableLiveData<>();
        this.eventsLocalDataSource = eventsLocalDataSource;
        this.cloudDBDataSource = cloudDBDataSource;
        this.eventsLocalDataSource.setEventsCallback(this);
        this.cloudDBDataSource.setEventsCallback(this);
        readUser(dataEncryptionUtil);
    }

    private void readUser(DataEncryptionUtil dataEncryptionUtil){
        try {
            this.user = new Gson().fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MutableLiveData<Result> fetchEvents(long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            cloudDBDataSource.getEvents(user.getUserId());
        } else {
            eventsLocalDataSource.getEvents();
        }
        return allEventsMutableLiveData;
    }

    @Override
    public void onSuccessFromRemote(List<EventsWithUsersFromCloudResponse> eventsCloudResponse) {
        eventsLocalDataSource.insertEvent(eventsCloudResponse);
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
    public void onJoinedEventFromRemote(String eventId) {
        eventsLocalDataSource.joinEvent(eventId, user.getUserId());
    }
    @Override
    public void onLeftEventFromRemote(String eventId) {
        eventsLocalDataSource.leaveEvent(eventId, user.getUserId());
    }

    @Override
    public void onJoinStatusChangedFromLocal(EventWithUsers event) {
        Result eventResult = allEventsMutableLiveData.getValue();
        if(eventResult != null && eventResult.isSuccess()){
            List<EventWithUsers> eventList = ((Result.Success)eventResult).getData();
            if(eventList.contains(event)){
                eventList.set(eventList.indexOf(event), event);
                allEventsMutableLiveData.postValue(eventResult);
            }else{
                eventList.add(event);
                allEventsMutableLiveData.postValue(eventResult);
            }
        }
    }

    @Override
    public void onInsertedEvent(EventWithUsers event) {
        eventsLocalDataSource.insertEvent(event);
    }

    @Override
    public void insertEvent(EventWithUsers event) {
        event.getEvent().setManager(user.getUserId());
        event.getUsers().add(user);
        cloudDBDataSource.addEvent(event);
    }
    @Override
    public void joinEvent(String eventId) {
        cloudDBDataSource.joinEvent(eventId, user.getUserId());
    }

    @Override
    public void leaveEvent(String eventId) {
        cloudDBDataSource.leaveEvent(eventId, user.getUserId());
    }
}
