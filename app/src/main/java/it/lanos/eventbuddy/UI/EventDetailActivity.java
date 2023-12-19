package it.lanos.eventbuddy.UI;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;

public class EventDetailActivity extends AppCompatActivity {
    private EventWithUsers event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        event = EventDetailActivityArgs.fromBundle(getIntent().getExtras()).getEventClick();
        TextView eventDate = findViewById(R.id.event_date);
        TextView eventHour = findViewById(R.id.event_time);
        TextView eventName = findViewById(R.id.event_name);
        TextView eventAddress = findViewById(R.id.event_address);
        TextView eventDescription = findViewById(R.id.event_description);
        Button join = findViewById(R.id.button_join);
        Button doNotJoin = findViewById(R.id.button_do_not_join);


        eventDate.setText(event.getEvent().getDate());
        //eventHour.setText(event.getEvent().getDate()); //TODO: Dividere la data dall'ora
        eventName.setText(event.getEvent().getName());
        eventAddress.setText(event.getEvent().getLocation());
        eventDescription.setText(event.getEvent().getDescription());

    }



}

