package it.lanos.eventbuddy.data;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import it.lanos.eventbuddy.data.source.LocationCallback;
import it.lanos.eventbuddy.data.source.firebase.realtimeDB.BaseLocationRealtimeDBDataSource;
import it.lanos.eventbuddy.data.source.models.Location;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.DataEncryptionUtil;

public class LocationRepository implements ILocationRepository, LocationCallback {
    private static final String TAG = LocationRepository.class.getSimpleName();
    private final BaseLocationRealtimeDBDataSource locationRealtimeDBDataSource;
    private final MutableLiveData<Result> locationLiveData;
    private User user;
    public LocationRepository(BaseLocationRealtimeDBDataSource locationRealtimeDBDataSource, DataEncryptionUtil dataEncryptionUtil) {
        this.locationRealtimeDBDataSource = locationRealtimeDBDataSource;
        this.locationRealtimeDBDataSource.setLocationCallback(this);
        locationLiveData = new MutableLiveData<>();
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
    public MutableLiveData<Result> getLocation(String eventId) {
        locationRealtimeDBDataSource.observeLocation(eventId);
        return locationLiveData;
    }

    @Override
    public void setLocation(Location location) {
        location.userId = user.getUserId();
        locationRealtimeDBDataSource.setLocation(location);
    }

    @Override
    public void stopLocationUpdates(String eventId) {
        locationRealtimeDBDataSource.detachObserver(eventId);
    }

    @Override
    public void onFailureFromRemoteDatabase(Exception e) {
        Result.Error error = new Result.Error(e.getLocalizedMessage());
        locationLiveData.postValue(error);
    }

    @Override
    public void onReceivedLocationFromRemoteSuccess(Location location) {
        Result.LocationSuccess locationSuccess = new Result.LocationSuccess(location);
        locationLiveData.postValue(locationSuccess);
    }
}
