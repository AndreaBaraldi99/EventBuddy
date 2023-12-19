package it.lanos.eventbuddy.data.source.local.datasource;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;

import java.util.ArrayList;
import java.util.Map;

import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.dao.EventDao;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;
import it.lanos.eventbuddy.util.DatastoreBuilder;

public class EventsLocalDataSource extends BaseEventsLocalDataSource {
    private final EventDao eventDao;
    private final UserDao userDao;
    private final DatastoreBuilder datastoreBuilder;

    public EventsLocalDataSource(EventsRoomDatabase newsRoomDatabase, DatastoreBuilder datastoreBuilder){
        this.eventDao = newsRoomDatabase.eventDao();
        this.userDao = newsRoomDatabase.userDao();
        this.datastoreBuilder = datastoreBuilder;
    }
    @Override
    public void getEvents() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> eventsCallback.onSuccessFromLocal(eventDao.getEventsWithUsers()));
    }
    @Override
    public void insertEvent(EventWithUsers eventWithUsers){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUsers(eventWithUsers.getUsers());
            eventDao.insertEvent(eventWithUsers.getEvent());
            for(User user : eventWithUsers.getUsers()){
                eventDao.insertEventWithUsers(new UserEventCrossRef(user.getUserId(), eventWithUsers.getEvent().getEventId(), false));
            }
            datastoreBuilder.putStringValue(LAST_UPDATE, String.valueOf(System.currentTimeMillis()));
            eventsCallback.onSuccessFromLocal(eventDao.getEventsWithUsers());
        });
    }

    @Override
    public void insertEvent(Event event, Map<User, Boolean> users){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUsers(new ArrayList<>(users.keySet()));
            eventDao.insertEvent(event);
            for (Map.Entry<User, Boolean> entry : users.entrySet()) {
                eventDao.insertEventWithUsers(new UserEventCrossRef(entry.getKey().getUserId(), event.getEventId(), entry.getValue()));
            }

            datastoreBuilder.putStringValue(LAST_UPDATE, String.valueOf(System.currentTimeMillis()));
            eventsCallback.onSuccessFromLocal(eventDao.getEventsWithUsers());
        });
    }
}
