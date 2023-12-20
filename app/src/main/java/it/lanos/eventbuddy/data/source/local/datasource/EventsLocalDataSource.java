package it.lanos.eventbuddy.data.source.local.datasource;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.EventsWithUsersFromCloudResponse;
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
    public void insertEvent(List<EventsWithUsersFromCloudResponse> eventWithUsers){
        EventsRoomDatabase.nukeTables();
        for(EventsWithUsersFromCloudResponse event : eventWithUsers){
            Event ev = Event.fromCloudResponse(event.getEvent());
            Map<User, Boolean> users = event.getUsers();
            EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUsers(new ArrayList<>(users.keySet()));
            eventDao.insertEvent(ev);
            for (Map.Entry<User, Boolean> entry : users.entrySet()) {
                eventDao.insertEventWithUsers(new UserEventCrossRef(entry.getKey().getUserId(), ev.getEventId(), entry.getValue()));
            }

            //datastoreBuilder.putStringValue(LAST_UPDATE, String.valueOf(System.currentTimeMillis()));
            eventsCallback.onSuccessFromLocal(eventDao.getEventsWithUsers());
            });
        };
    }

    @Override
    public void insertEvent(Event event, Map<User, Boolean> users){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUsers(new ArrayList<>(users.keySet()));
            eventDao.insertEvent(event);
            for (Map.Entry<User, Boolean> entry : users.entrySet()) {
                eventDao.insertEventWithUsers(new UserEventCrossRef(entry.getKey().getUserId(), event.getEventId(), entry.getValue()));
            }

            //datastoreBuilder.putStringValue(LAST_UPDATE, String.valueOf(System.currentTimeMillis()));
            eventsCallback.onSuccessFromLocal(eventDao.getEventsWithUsers());
        });
    }

    @Override
    public void joinEvent(String eventId, String uid){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertEventWithUsers(new UserEventCrossRef(uid, eventId, true));
            eventsCallback.onJoinStatusChangedFromLocal(eventDao.getEventWithUsers(eventId));
        });
    }
}
