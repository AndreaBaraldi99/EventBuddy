package it.lanos.eventbuddy.UI;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;

public class EventDetailActivity extends AppCompatActivity {
    private EventWithUsers event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        View rootView = findViewById(android.R.id.content);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Applica gli insetti come margini alla vista radice.
            rootView.setPadding(
                    systemBarsInsets.left,
                    systemBarsInsets.top,
                    systemBarsInsets.right,
                    systemBarsInsets.bottom
            );

            // Restituisci CONSUMED se non vuoi che gli insetti della finestra continuino a scendere
            // verso le viste discendenti.
            return WindowInsetsCompat.CONSUMED;
        });


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

