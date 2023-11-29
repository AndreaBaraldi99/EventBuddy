package it.lanos.eventbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*EventsUsersDB db = Room.databaseBuilder(getApplicationContext(),
                EventsUsersDB.class, "eventbuddyDB").build();*/
    }
}