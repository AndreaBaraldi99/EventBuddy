package it.lanos.eventbuddy.data.source.local.datasource;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;

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
import it.lanos.eventbuddy.util.SharedPreferencesUtil;

public class EventsLocalDataSource extends BaseEventsLocalDataSource {
    private final EventDao eventDao;
    private final UserDao userDao;
    private final SharedPreferencesUtil sharedPreferencesUtil;

    public EventsLocalDataSource(EventsRoomDatabase newsRoomDatabase, SharedPreferencesUtil sharedPreferencesUtil){
        this.eventDao = newsRoomDatabase.eventDao();
        this.userDao = newsRoomDatabase.userDao();
        this.sharedPreferencesUtil = sharedPreferencesUtil;
    }
    @Override
    public void getEvents() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> eventsCallback.onSuccessFromLocal(eventDao.getEventsWithUsers()));
    }
    @Override
    public void insertEvent(List<EventsWithUsersFromCloudResponse> eventWithUsers){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<EventWithUsers> eventWithUsersList = new ArrayList<>();
            for(EventsWithUsersFromCloudResponse event : eventWithUsers){
                Event ev = Event.fromCloudResponse(event.getEvent());
                Map<User, Boolean> users = event.getUsers();
                EventWithUsers newEvent = new EventWithUsers(ev, new ArrayList<>(users.keySet()));
                List<UserEventCrossRef> newUserEventCrossRefs = new ArrayList<>();
                userDao.insertUsers(new ArrayList<>(users.keySet()));
                eventDao.insertEvent(ev);
                for (Map.Entry<User, Boolean> entry : users.entrySet()) {
                    UserEventCrossRef userEventCrossRef = new UserEventCrossRef(entry.getKey().getUserId(), ev.getEventId(), entry.getValue());
                    eventDao.insertEventWithUsers(userEventCrossRef);
                    newUserEventCrossRefs.add(userEventCrossRef);
                }
                newEvent.setUserEventCrossRefs(newUserEventCrossRefs);
                eventWithUsersList.add(newEvent);
            }
            deleteOldEvents(eventWithUsersList);
            sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                    LAST_UPDATE, String.valueOf(System.currentTimeMillis()));

            eventsCallback.onSuccessFromLocal(eventWithUsersList);
        });
    }

    private void deleteOldEvents(List<EventWithUsers> newData){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<EventWithUsers> oldData = eventDao.getEventsWithUsers();
            for(EventWithUsers event : oldData){
                if(!newData.contains(event)){
                    eventDao.deleteEvent(event.getEvent());
                    eventDao.deleteUserEventCrossRef(event.getEvent().getEventId());
                    userDao.deleteUsersWithNoEvents(event.getEvent().getEventId());
                }
            }
        });
    }

    @Override
    public void insertEvent(EventWithUsers eventWithUsers){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUsers(eventWithUsers.getUsers());
            eventDao.insertEvent(eventWithUsers.getEvent());
            for(User user : eventWithUsers.getUsers()){
                eventDao.insertEventWithUsers(new UserEventCrossRef(user.getUserId(), eventWithUsers.getEvent().getEventId(), false));
            }
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
    @Override
    public void leaveEvent(String eventId, String uid){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertEventWithUsers(new UserEventCrossRef(uid, eventId, false));
            eventsCallback.onJoinStatusChangedFromLocal(eventDao.getEventWithUsers(eventId));
        });
    }
}
