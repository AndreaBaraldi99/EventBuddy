package it.lanos.eventbuddy.data;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.lanos.eventbuddy.util.Constants.FRESH_TIMEOUT;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.List;

import it.lanos.eventbuddy.data.source.EventsCallback;
import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseEventsCloudDBDataSource;
import it.lanos.eventbuddy.data.source.models.EventsCloudResponse;
import it.lanos.eventbuddy.data.source.models.EventsWithUsersFromCloudResponse;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.DataEncryptionUtil;


public class EventRepository implements IEventsRepository, EventsCallback{
    private static final String TAG = EventRepository.class.getSimpleName();
    private final MutableLiveData<Result> allEventsMutableLiveData;
    private final BaseEventsLocalDataSource eventsLocalDataSource;
    //private final FirebaseUser user;
    private final BaseEventsCloudDBDataSource cloudDBDataSource;
    private final DataEncryptionUtil dataEncryptionUtil;
    private final Gson gson;

    public EventRepository(BaseEventsLocalDataSource eventsLocalDataSource, BaseEventsCloudDBDataSource cloudDBDataSource, FirebaseUser user, DataEncryptionUtil dataEncryptionUtil, Gson gson) {
        this.gson = gson;
        this.dataEncryptionUtil = dataEncryptionUtil;
        allEventsMutableLiveData = new MutableLiveData<>();
        this.eventsLocalDataSource = eventsLocalDataSource;
        this.cloudDBDataSource = cloudDBDataSource;
        this.eventsLocalDataSource.setEventsCallback(this);
        this.cloudDBDataSource.setEventsCallback(this);
        //this.user = user;
    }

    @Override
    public MutableLiveData<Result> fetchEvents(long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            try {
                User user = gson.fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
                cloudDBDataSource.getEvents(user.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        try {
            User user = gson.fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
            eventsLocalDataSource.joinEvent(eventId, user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void insertEvent(EventWithUsers event) {
        try {
            User user = gson.fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
            event.getEvent().setManager(user.getUserId());
            event.getUsers().add(user);
            cloudDBDataSource.addEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void joinEvent(String eventId) {
        try {
            User user = gson.fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
            cloudDBDataSource.joinEvent(eventId, user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leaveEvent(String eventId) {
        try {
            User user = gson.fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
            cloudDBDataSource.leaveEvent(eventId, user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
