package it.lanos.eventbuddy.UI.authentication;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import it.lanos.eventbuddy.data.EventRepository;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.UserRepository;
import it.lanos.eventbuddy.data.services.AuthService;
import it.lanos.eventbuddy.data.services.CloudDBService;
import it.lanos.eventbuddy.data.source.firebase.auth.UserDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseEventsCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseUserCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.EventsCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.UserCloudDBDataSource;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.BaseUserLocalDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.EventsLocalDataSource;
import it.lanos.eventbuddy.data.source.local.datasource.UserLocalDataSource;
import it.lanos.eventbuddy.util.DataEncryptionUtil;
import it.lanos.eventbuddy.util.DatastoreBuilder;

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
        eventsLocalDataSource = new EventsLocalDataSource(getDatabase(application), getDatastoreBuilder(application));
        CloudDBService cloudDBService = new CloudDBService(FirebaseFirestore.getInstance());
        BaseEventsCloudDBDataSource cloudDBDataSource = new EventsCloudDBDataSource(cloudDBService);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);
        Gson gson = new Gson();

        return new EventRepository(eventsLocalDataSource, cloudDBDataSource, user, dataEncryptionUtil, gson);
    }

    public IUserRepository getUserRepository(Application application) {
        AuthService authService = new AuthService(FirebaseAuth.getInstance());
        UserDataSource userDataSource = new UserDataSource(authService);

        CloudDBService cloudDBService = new CloudDBService(FirebaseFirestore.getInstance());
        BaseUserCloudDBDataSource cloudDBDataSource = new UserCloudDBDataSource(cloudDBService);

        BaseUserLocalDataSource userLocalDataSource = new UserLocalDataSource(getDatabase(application));

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);
        Gson gson = new Gson();

        return new UserRepository(userDataSource, cloudDBDataSource, userLocalDataSource, dataEncryptionUtil, gson);
    }

    public DatastoreBuilder getDatastoreBuilder(Application application) {
        return new DatastoreBuilder();
    }

}