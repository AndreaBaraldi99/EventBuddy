package it.lanos.eventbuddy.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.lanos.eventbuddy.data.source.local.dao.EventDao;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;
import it.lanos.eventbuddy.data.source.local.entities.LocalEvent;
import it.lanos.eventbuddy.data.source.local.entities.LocalUser;
import it.lanos.eventbuddy.data.source.local.entities.UserEventCrossRef;
import it.lanos.eventbuddy.util.DateConverter;

@Database(entities = {LocalEvent.class, LocalUser.class, UserEventCrossRef.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract UserDao userDao();
}
