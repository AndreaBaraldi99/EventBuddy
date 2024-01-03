package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.EventRepository;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;
import it.lanos.eventbuddy.util.DataEncryptionUtil;
import it.lanos.eventbuddy.util.Parser;
import it.lanos.eventbuddy.util.ServiceLocator;

public class EventDetailActivity extends AppCompatActivity {
    private EventWithUsers event;
    private User user;
    private boolean somethingChange = false;
    Button join;
    Button doNotJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        MaterialToolbar createEventToolbar = findViewById(R.id.detail_event_top_appbar);
        createEventToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(EventDetailActivity.this);
            }
        });

        EventRepository iEventsRepository = (EventRepository)
                ServiceLocator.getInstance().getEventsRepository(this.getApplication());



        Intent intent = getIntent();
        //event = EventDetailActivityArgs.fromBundle(getIntent().getExtras()).getEventClick();
        if (intent != null && intent.hasExtra("event")) {
            // Recupera l'oggetto EventWithUsers dalla Intent
            event = intent.getParcelableExtra("event");
        }
        List<UserEventCrossRef> usersInfo = event.getUserEventCrossRefs();
        List<UserEventCrossRef> joinedUsers = getJoinedUsers(usersInfo);


        TextView eventDate = findViewById(R.id.event_date);
        TextView eventHour = findViewById(R.id.event_time);
        TextView eventName = findViewById(R.id.event_name);
        TextView eventAddress = findViewById(R.id.event_address);
        TextView eventDescription = findViewById(R.id.event_description);
        TextView detailPartecipants = findViewById(R.id.event_participants_info);
        TextView numberPartecipants = findViewById(R.id.event_number_partecipants);

        numberPartecipants.setText("+"+joinedUsers.size());
        if(joinedUsers.size() == 0)
            detailPartecipants.setText(getString(R.string.noone_partecipating));

        join = findViewById(R.id.button_join);
        doNotJoin = findViewById(R.id.button_do_not_join);

        handleButtonsConfiguration(usersInfo);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join.setBackgroundColor(getResources().getColor(R.color.md_theme_light_surfaceTint));
                doNotJoin.setBackgroundColor(getResources().getColor(R.color.divisor));
                iEventsRepository.joinEvent(event.getEvent().getEventId());
                somethingChange = true;
            }
        });

        doNotJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNotJoin.setBackgroundColor(getResources().getColor(R.color.md_theme_dark_error));
                join.setBackgroundColor(getResources().getColor(R.color.divisor));
                iEventsRepository.leaveEvent(event.getEvent().getEventId());
                somethingChange = true;
            }
        });


        String date_time = event.getEvent().getDate();
        String formatted_date = Parser.formatDate(date_time);
        String formatted_time = Parser.formatTime(date_time);





        eventDate.setText(formatted_date);
        eventHour.setText(formatted_time); //TODO: Dividere la data dall'ora
        eventName.setText(event.getEvent().getName());
        eventAddress.setText(event.getEvent().getLocation());
        eventDescription.setText(event.getEvent().getDescription());

    }

    private List<UserEventCrossRef> getJoinedUsers(List<UserEventCrossRef> usersInfo) {
        List<UserEventCrossRef> joinedUsers = new ArrayList<>();
        Iterator it = usersInfo.iterator();
        while(it.hasNext()){
            UserEventCrossRef current = (UserEventCrossRef) it.next();
            if(current.getJoined())
                joinedUsers.add(current);
        }
        return joinedUsers;
    }

    private void handleButtonsConfiguration(List<UserEventCrossRef> usersInfo) {
        readUser(new DataEncryptionUtil(this.getApplication()));
        Iterator it = usersInfo.iterator();
        while(it.hasNext()){
            UserEventCrossRef current = (UserEventCrossRef) it.next();
            if(current.getUserId().equals(this.user.getUserId()) && current.getJoined()) {
                join.setBackgroundColor(getResources().getColor(R.color.md_theme_light_surfaceTint));
                doNotJoin.setBackgroundColor(getResources().getColor(R.color.divisor));
                break;
            }
            else if(current.getUserId().equals(this.user.getUserId()) && !current.getJoined()) {
                doNotJoin.setBackgroundColor(getResources().getColor(R.color.md_theme_dark_error));
                join.setBackgroundColor(getResources().getColor(R.color.divisor));
                break;
            }
        }
    }

    private void readUser(DataEncryptionUtil dataEncryptionUtil){
        try {
            this.user = new Gson().fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void returnResultToCallingActivity(boolean change) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("change", change);
        setResult(Activity.RESULT_OK, resultIntent);
    }

    @Override
    public void onBackPressed() {
        returnResultToCallingActivity(somethingChange);
        super.onBackPressed();
    }

}

