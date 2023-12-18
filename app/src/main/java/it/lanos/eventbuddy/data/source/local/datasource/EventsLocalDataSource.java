package it.lanos.eventbuddy.data.source.local.datasource;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.lanos.eventbuddy.data.source.entities.Event;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.data.source.entities.UserEventCrossRef;
import it.lanos.eventbuddy.data.source.local.EventsRoomDatabase;
import it.lanos.eventbuddy.data.source.local.dao.EventDao;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;
import it.lanos.eventbuddy.data.source.local.datasource.BaseEventsLocalDataSource;
import it.lanos.eventbuddy.util.DatastoreBuilder;

public class EventsLocalDataSource extends BaseEventsLocalDataSource {
    private final EventDao eventDao;
    private final UserDao userDao;
    private final DatastoreBuilder datastoreBuilder;
    //private final SharedPreferencesUtil sharedPreferencesUtil;

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
            /*List<Long> insertedEventsIds = userDao.insertUsers(eventWithUsers.getUsers());
            long insertedEventId = eventDao.insertEvent(eventWithUsers.getEvent());*/
            /*for (Long userId : insertedEventsIds) {
                eventDao.insertEventWithUsers(new UserEventCrossRef(userId, insertedEventId, false));
            }*/
            //datastoreBuilder.putStringValue(LAST_UPDATE, String.valueOf(System.currentTimeMillis()));
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
            /*List<UserEventCrossRef> userEventCrossRefs = new ArrayList<>();
            for (Map.Entry<User, Boolean> entry : users.entrySet()) {
                long insertedUserId = userDao.insertUser(entry.getKey());
                userEventCrossRefs.add(new UserEventCrossRef(insertedUserId, 0, entry.getValue()));
            }
            long insertedEventId = eventDao.insertEvent(event);
            for (UserEventCrossRef userEventCrossRef : userEventCrossRefs) {
                userEventCrossRef.setEventId(insertedEventId);
                eventDao.insertEventWithUsers(userEventCrossRef);
            }*/
            //datastoreBuilder.putStringValue(LAST_UPDATE, String.valueOf(System.currentTimeMillis()));
            eventsCallback.onSuccessFromLocal(eventDao.getEventsWithUsers());
        });
    }
}
