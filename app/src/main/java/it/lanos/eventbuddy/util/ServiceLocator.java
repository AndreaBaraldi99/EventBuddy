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
import it.lanos.eventbuddy.data.source.firebase.bucket.BaseImageRemoteDataSource;
import it.lanos.eventbuddy.data.source.firebase.bucket.ImageRemoteDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseEventsRemoteDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseUserRemoteDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsRemoteDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.UserRemoteDataSource;
import it.lanos.eventbuddy.data.source.firebase.realtimeDB.BaseLocationRemoteDataSource;
import it.lanos.eventbuddy.data.source.firebase.realtimeDB.LocationRemoteDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.BaseUserLocalDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.EventsLocalDataSource;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.datasource.UserLocalDataSource;
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
        BaseEventsRemoteDataSource cloudDBDataSource = new EventsRemoteDataSource(cloudDBService);

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        return new EventRepository(eventsLocalDataSource, cloudDBDataSource, dataEncryptionUtil);
    }

    public IUserRepository getUserRepository(Application application) {
        AuthService authService = new AuthService(FirebaseAuth.getInstance());
        UserDataSource userDataSource = new UserDataSource(authService);

        CloudDBService cloudDBService = new CloudDBService(FirebaseFirestore.getInstance());
        BaseUserRemoteDataSource cloudDBDataSource = new UserRemoteDataSource(cloudDBService);

        SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);

        BaseUserLocalDataSource userLocalDataSource = new UserLocalDataSource(getDatabase(application), sharedPreferencesUtil);

        BaseImageRemoteDataSource imageRemoteDataSource = new ImageRemoteDataSource();


        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        return new UserRepository(userDataSource, cloudDBDataSource, userLocalDataSource, imageRemoteDataSource, dataEncryptionUtil);
    }

    public ISuggestionsRepository getSuggestionsRepository(Application application) {

        MapboxService mapboxService = getMapboxApiService();

        BaseAutocompleteMapboxDataSource mapboxDataSource = new AutoCompleteMapboxDataSource(mapboxService);

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        return new SuggestionsRepository(mapboxDataSource, dataEncryptionUtil);
    }

    public ILocationRepository getLocationRepository(Application application) {
        BaseLocationRemoteDataSource locationRealtimeDBDataSource = new LocationRemoteDataSource();

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        return new LocationRepository(locationRealtimeDBDataSource, dataEncryptionUtil);
    }

    private MapboxService getMapboxApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(MapboxService.class);
    }
}
