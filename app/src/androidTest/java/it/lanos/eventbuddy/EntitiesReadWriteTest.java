package it.lanos.eventbuddy;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import it.lanos.eventbuddy.data.source.local.AppDatabase;
import it.lanos.eventbuddy.data.source.local.dao.UserDao;
import it.lanos.eventbuddy.data.source.local.dao.EventDao;
import it.lanos.eventbuddy.data.source.local.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.local.entities.LocalEvent;
import it.lanos.eventbuddy.data.source.local.entities.LocalUser;
import it.lanos.eventbuddy.data.source.local.entities.UserEventCrossRef;
import it.lanos.eventbuddy.data.source.local.entities.UserWithEvents;

@RunWith(AndroidJUnit4.class)
public class EntitiesReadWriteTest {
    private UserDao userDao;
    private EventDao eventDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
        eventDao = db.eventDao();
    }

    @After
    public void closeDb(){
        db.close();
    }

    @Test
    public void writeUserAndReadInList() {
        LocalEvent event = new LocalEvent(1, 11, "Test name", new Date(2023, 12, 12), "Via Milano 23", "Test description");

        LocalUser user = new LocalUser(11, "TestUsername", "Test Full Name");
        LocalUser user2 = new LocalUser(12, "TestUsername2", "Test Full Name2");

        long userId1 = userDao.insertUser(user);
        long userId = userDao.insertUser(user2);
        long eventId = eventDao.insertEvent(event);

        userDao.insertUserWithEvents(new UserEventCrossRef(userId, eventId, true));
        userDao.insertUserWithEvents(new UserEventCrossRef(userId1, eventId, true));

        //Tests that users are correctly referenced to the event
        List<UserWithEvents> usersWithEvent = userDao.getUsersWithEvents();
        assert(usersWithEvent.get(0).getEvents().get(0).getEventId() == 1);
        assert(usersWithEvent.get(1).getEvents().get(0).getEventId() == 1);

        //Tests that events are correctly referenced to the users
        List<EventWithUsers> eventsWithUsers = eventDao.getEventsWithUsers();
        assert(eventsWithUsers.get(0).getEvent().getEventId() == 1);
        assert(eventsWithUsers.get(0).getUsers().get(0).getUserId() == 12);
        assert(eventsWithUsers.get(0).getUsers().get(1).getUserId() == 11);
    }
}
