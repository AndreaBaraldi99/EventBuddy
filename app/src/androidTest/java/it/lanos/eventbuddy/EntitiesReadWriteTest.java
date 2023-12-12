package it.lanos.eventbuddy;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.testing.TestLifecycleOwner;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.entities.Event;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.Result;
import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.util.ServiceLocator;

@RunWith(AndroidJUnit4.class)
public class EntitiesReadWriteTest{
    private static final String TAG = "EntitiesReadWriteTest";
    private static IEventsRepository eventsRepository;

    @Before
    public void initialize(){
        eventsRepository = ServiceLocator.getInstance().getEventsRepository(ApplicationProvider.getApplicationContext());
        /*Single<Preferences> updateResult =  dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            Integer currentInt = prefsIn.get(INTEGER_KEY);
            mutablePreferences.set(INTEGER_KEY, currentInt != null ? currentInt + 1 : 1);
            return Single.just(mutablePreferences);
        });*/
    }

    @Test
    public void writeTest(){
        List<User> users = new ArrayList<>();
        users.add(new User("1", "Mario", "Rossi"));
        users.add(new User("2", "Luigi", "Verdi"));
        users.add(new User("3", "Giovanni", "Bianchi"));
        Event event = new Event("EventId1", "Evento 1", "2020-01-01", "Via Milano 12", "Luogo evento 1");
        EventWithUsers eventWithUsers = new EventWithUsers(event, users);
        eventsRepository.insertEvent(eventWithUsers);
    }

    @Test
    public void readTest(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> eventsRepository.fetchEvents().observe(new TestLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                List<EventWithUsers> events = ((Result.Success) result).getData();
                assert(events.size() == 1);
                assert(events.get(0).getUsers().size() == 3);
            } else {
                Log.d(TAG, "Error fetching the result" );
            }

        }));
    }

}
