package it.lanos.eventbuddy.util;

import android.app.Application;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import it.lanos.eventbuddy.data.EventRepository;
import it.lanos.eventbuddy.data.ISuggestionsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.LocationRepository;
import it.lanos.eventbuddy.data.SuggestionsRepository;
import it.lanos.eventbuddy.data.UserRepository;
import it.lanos.eventbuddy.data.services.AuthService;
import it.lanos.eventbuddy.data.services.CloudDBService;
import it.lanos.eventbuddy.data.services.MapboxService;
import it.lanos.eventbuddy.data.ILocationRepository;
import it.lanos.eventbuddy.data.source.firebase.auth.UserDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseEventsCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseUserCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.UserCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.realtimeDB.BaseLocationRealtimeDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.realtimeDB.LocationRealtimeDBDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.EventsLocalDataSource;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.mapbox.AutoCompleteMapboxDataSource;
import it.lanos.eventbuddy.data.source.mapbox.BaseAutocompleteMapboxDataSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;
    private ServiceLocator() {}
    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }
    public EventsRoomDatabase getDatabase(Application application) {
        return EventsRoomDatabase.getDatabase(application);
    }

    public IEventsRepository getEventsRepository(Application application) {
        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);
        BaseEventsLocalDataSource eventsLocalDataSource;
        eventsLocalDataSource = new EventsLocalDataSource(getDatabase(application), sharedPreferencesUtil);

        CloudDBService cloudDBService = new CloudDBService(FirebaseFirestore.getInstance());
        BaseEventsCloudDBDataSource cloudDBDataSource = new EventsCloudDBDataSource(cloudDBService);

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        return new EventRepository(eventsLocalDataSource, cloudDBDataSource, dataEncryptionUtil);
    }

    public IUserRepository getUserRepository(Application application) {
        AuthService authService = new AuthService(FirebaseAuth.getInstance());
        UserDataSource userDataSource = new UserDataSource(authService);

        CloudDBService cloudDBService = new CloudDBService(FirebaseFirestore.getInstance());
        BaseUserCloudDBDataSource cloudDBDataSource = new UserCloudDBDataSource(cloudDBService);


        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        return new UserRepository(userDataSource, cloudDBDataSource, dataEncryptionUtil);
    }

    public ISuggestionsRepository getSuggestionsRepository(Application application) {

        MapboxService mapboxService = getMapboxApiService();

        BaseAutocompleteMapboxDataSource mapboxDataSource = new AutoCompleteMapboxDataSource(mapboxService);

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        return new SuggestionsRepository(mapboxDataSource, dataEncryptionUtil);
    }

    public ILocationRepository getLocationRepository(Application application) {
        BaseLocationRealtimeDBDataSource locationRealtimeDBDataSource = new LocationRealtimeDBDataSource();

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        return new LocationRepository(locationRealtimeDBDataSource, dataEncryptionUtil);
    }

    private MapboxService getMapboxApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(MapboxService.class);
    }
}
