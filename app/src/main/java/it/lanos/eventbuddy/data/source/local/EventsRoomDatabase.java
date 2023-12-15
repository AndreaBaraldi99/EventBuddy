package it.lanos.eventbuddy.data.source.local;

import static it.lanos.eventbuddy.util.Constants.DATABASE_VERSION;
import static it.lanos.eventbuddy.util.Constants.NEWS_DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.lanos.eventbuddy.data.source.entities.Event;
import it.lanos.eventbuddy.data.source.local.dao.EventDao;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;
import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.data.source.entities.UserEventCrossRef;

@Database(entities = {Event.class, User.class, UserEventCrossRef.class}, version = DATABASE_VERSION)
public abstract class EventsRoomDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract UserDao userDao();
    private static volatile EventsRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static EventsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EventsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EventsRoomDatabase.class, NEWS_DATABASE_NAME).build();
                }
            }
        }

        return INSTANCE;
    }

    public static void nukeTables() {
        databaseWriteExecutor.execute(() -> {
            if(INSTANCE != null)
                INSTANCE.clearAllTables();
        });
    }


}
