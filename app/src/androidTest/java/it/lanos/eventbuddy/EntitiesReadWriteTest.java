package it.lanos.eventbuddy;

import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;

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
