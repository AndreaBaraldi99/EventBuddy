package it.lanos.eventbuddy.util;

import android.app.Application;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public IEventsRepository getEventsRepository(Application application) {
        BaseEventsLocalDataSource eventsLocalDataSource;
        eventsLocalDataSource = new EventsLocalDataSource(getDatabase(application), getDatastoreBuilder());
        BaseCloudDBDataSource cloudDBDataSource = new CloudDBDataSource(FirebaseFirestore.getInstance());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        return new EventWithUsersRepository(eventsLocalDataSource, cloudDBDataSource, user);
    }

    public DatastoreBuilder getDatastoreBuilder() {
        return new DatastoreBuilder();
    }
    public IAuthRepository getAuthRepository() {
        return null;
    }

}
