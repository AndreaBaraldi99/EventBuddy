package it.lanos.eventbuddy.UI.authentication;


import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import it.lanos.eventbuddy.data.services.AuthService;
import it.lanos.eventbuddy.data.services.CloudDBService;
import it.lanos.eventbuddy.data.source.firebase.auth.BaseUserDataSource;
import it.lanos.eventbuddy.data.source.firebase.auth.UserDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.BaseUserCloudDBDataSource;
import it.lanos.eventbuddy.data.source.firebase.cloudDB.UserCloudDBDataSource;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.datasource.BaseUserLocalDataSource;
import it.lanos.eventbuddy.data.UserRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.local.datasource.UserLocalDataSource;

public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private AuthService authService;
    private CloudDBService cloudDBService;
    private EventsRoomDatabase eventsRoomDatabase;

    private ServiceLocator(Application application) {
        this.authService = new AuthService(FirebaseAuth.getInstance());
        this.cloudDBService = new CloudDBService(FirebaseFirestore.getInstance());
        this.eventsRoomDatabase = EventsRoomDatabase.getDatabase(application);
    }

    public static ServiceLocator getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator(application);
                }
            }
        }
        return INSTANCE;
    }


    public IUserRepository getUserRepository() {
        BaseUserDataSource userDataSource = new UserDataSource(authService);
        BaseUserCloudDBDataSource userCloudDBDataSource = new UserCloudDBDataSource(cloudDBService);
        BaseUserLocalDataSource userLocalDataSource = new UserLocalDataSource(eventsRoomDatabase);

        return new UserRepository(userDataSource, userCloudDBDataSource, userLocalDataSource);
    }
}

