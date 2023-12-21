package it.lanos.eventbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.UserRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.ServiceLocator;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(getApplication());
        userRepository.signIn("test@eventbuddy.it", "eventbuddy1");
        IEventsRepository eventsRepository = ServiceLocator.getInstance().getEventsRepository(getApplication());
        eventsRepository.fetchEvents(0).observe(this, result -> {
            if (result.isSuccess()) {
                List<EventWithUsers> events = ((Result.Success) result).getData();
                Log.d(TAG, "onCreate: " + events);
                Log.d(TAG, "EventsCrossRef size: " + events.get(0).getUserEventCrossRefs().size());
            } else {
                Log.d(TAG, "onCreate: " + ((Result.Error) result).getMessage());
            }
        });
        userRepository.searchUsers("ev").observe(this, result -> {
            if (result.isUserSuccess()) {
                List<User> users = ((Result.UserSuccess) result).getData();
                Log.d(TAG, "Users: " + users);
            } else {
                Log.d(TAG, "Error " + ((Result.Error) result).getMessage());
            }
        });*/
    }
}