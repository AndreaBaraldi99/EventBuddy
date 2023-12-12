package it.lanos.eventbuddy.util;

import android.app.Application;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;

import com.google.firebase.firestore.FirebaseFirestore;

import it.lanos.eventbuddy.data.EventWithUsersRepository;
import it.lanos.eventbuddy.data.IAuthRepository;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.CloudDBDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.EventsLocalDataSource;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;

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

    public RxDataStore<Preferences> getDataStore(Application application) {
        return DatastoreBuilder.getDataStore(application);
    }

    public IEventsRepository getEventsRepository(Application application) {
        BaseEventsLocalDataSource eventsLocalDataSource;
        eventsLocalDataSource = new EventsLocalDataSource(getDatabase(application));
        RxDataStore<Preferences> dataStore = getDataStore(application);
        BaseCloudDBDataSource cloudDBDataSource = new CloudDBDataSource(FirebaseFirestore.getInstance());

        return new EventWithUsersRepository(eventsLocalDataSource, cloudDBDataSource, dataStore);
    }
    public IAuthRepository getAuthRepository() {
        return null;
    }

    public FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }
}
