package it.lanos.eventbuddy.data;

import android.app.Application;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.UserEventCrossRef;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.dao.EventDao;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;
import it.lanos.eventbuddy.util.ServiceLocator;

public class EventWithUsersRepository implements IEventsRepository{
    private static final String TAG = EventWithUsersRepository.class.getSimpleName();
    //private final ResponseCallback responseCallback;
    private final EventDao eventDao;
    private final UserDao userDao;
    public EventWithUsersRepository(Application application) {
        //this.responseCallback = responseCallback;
        //TODO: get the firebase dao
        EventsRoomDatabase eventsRoomDatabase = ServiceLocator.getInstance().getDatabase(application);
        this.eventDao = eventsRoomDatabase.eventDao();
        this.userDao = eventsRoomDatabase.userDao();
    }
    @Override
    public List<EventWithUsers> fetchEvents() {
        return eventDao.getEventsWithUsers();
    }
    @Override
    public void insertEvent(EventWithUsers eventWithUsers) {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            long eventId = eventDao.insertEvent(eventWithUsers.getEvent());
            eventWithUsers.getUsers().forEach(user -> {
                long userId = userDao.insertUser(user);
                eventDao.insertEventWithUsers(new UserEventCrossRef(userId, eventId, false));
            });
        });
    }
}
