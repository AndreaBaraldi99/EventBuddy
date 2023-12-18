package it.lanos.eventbuddy;

import static org.junit.Assert.assertNotNull;

import android.app.Application;
import android.app.LauncherActivity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.testing.TestLifecycleOwner;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.entities.Event;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.Result;
import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.util.ServiceLocator;

@RunWith(AndroidJUnit4.class)
public class EntitiesReadWriteTest{
    private static final String TAG = "EntitiesReadWriteTest";
    private IEventsRepository eventsRepository;
    private IUserRepository userRepository;

    @Before
    public void initialize(){
       /* Application appContext = (Application) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        eventsRepository = ServiceLocator.getInstance().getEventsRepository(appContext);
        userRepository = ServiceLocator.getInstance().getUserRepository(appContext);
        FirebaseApp.initializeApp(appContext);
        userRepository.register("Pippo paperino", "pippo", "pippo@paperino.it", "pippo123");*/

    }
   /* @Test
    public void testInsertEvent(){
        Event event = new Event("Test Name", "Test Date", "Test Location", "Test Description");
        User user = new User("Test Name", "Test Username", "Test fullname");
        User user2 = new User("Test Name2", "Test Username2", "Test fullname2");
        EventWithUsers eventWithUsers = new EventWithUsers(event, new ArrayList<User>());
        eventsRepository.insertEvent(eventWithUsers);
    }

    @Test
    public void testFetchEvents() {
        eventsRepository.fetchEvents(0).observe(new TestLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                List<EventWithUsers> events = ((Result.Success) result).getData();
                Log.d(TAG, "testFetchEvents: " + events.size());
            } else {
                Log.d(TAG, "testFetchEvents: " + ((Result.Error) result).getMessage());
            }
        });
    }*/

}
