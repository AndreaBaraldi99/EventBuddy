package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.EventRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;
import it.lanos.eventbuddy.util.DataEncryptionUtil;
import it.lanos.eventbuddy.util.Parser;
import it.lanos.eventbuddy.util.ServiceLocator;

public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private EventWithUsers event;
    private User user;
    private int somethingChange = 0;
    Button join;
    Button doNotJoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_detail);

        MaterialToolbar createEventToolbar = findViewById(R.id.detail_event_top_appbar);
        createEventToolbar.setNavigationOnClickListener(v -> {
            returnResultToCallingActivity(somethingChange, event.getEvent().getEventId());
            finish();
        });

        EventRepository iEventsRepository = (EventRepository)
                ServiceLocator.getInstance().getEventsRepository(this.getApplication());

        readUser(new DataEncryptionUtil(this.getApplication()));



        Intent intent = getIntent();
        //event = EventDetailActivityArgs.fromBundle(getIntent().getExtras()).getEventClick();
        if (intent != null && intent.hasExtra("event")) {
            // Recupera l'oggetto EventWithUsers dalla Intent
            event = intent.getParcelableExtra("event");
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnResultToCallingActivity(somethingChange, event.getEvent().getEventId());
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);


        assert event != null;
        List<UserEventCrossRef> usersInfo = event.getUserEventCrossRefs();
        List<UserEventCrossRef> joinedUsers =  getJoinedUsers(usersInfo);



        TextView eventDate = findViewById(R.id.event_date);
        TextView eventHour = findViewById(R.id.event_time);
        TextView eventName = findViewById(R.id.event_name);
        TextView eventAddress = findViewById(R.id.event_address);
        TextView eventDescription = findViewById(R.id.event_description);
        TextView detailPartecipants = findViewById(R.id.event_participants_info);
        TextView numberPartecipants = findViewById(R.id.event_number_partecipants);
        ImageView firstJoined = findViewById(R.id.event_avatar2);
        ImageView secondJoined = findViewById(R.id.event_avatar1);
        View buttonInfoJoined = findViewById(R.id.event_number_participants_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.active_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        numberPartecipants.setText("+"+joinedUsers.size());
        if(joinedUsers.size() == 0)
            detailPartecipants.setText(getString(R.string.noone_partecipating));
        else {
            String luckyNickname = "";
            for (User u : event.getUsers()) {
                if (u.getUserId().equals(joinedUsers.get(0).getUserId())) {
                    luckyNickname = u.getUsername();
                    if(luckyNickname.equals(user.getUsername())){
                        luckyNickname = getString(R.string.you);
                    }
                    break;
                }
            }
            detailPartecipants.setText(luckyNickname + " & " + (joinedUsers.size() - 1) + " " + getString(R.string.people_partecipating));

            buttonInfoJoined.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putParcelableArrayList("iUsers", (ArrayList) event.getUsers());
                args.putParcelableArrayList("pUsers", (ArrayList) joinedUsers);

                ShowFriendFragment dialogFragment = new ShowFriendFragment();
                dialogFragment.setArguments(args);
                dialogFragment.show(getSupportFragmentManager(), "show_friends_dialog");
            });

            detailPartecipants.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("iUsers", (ArrayList) event.getUsers());
                    args.putParcelableArrayList("pUsers", (ArrayList) joinedUsers);

                    ShowFriendFragment dialogFragment = new ShowFriendFragment();
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getSupportFragmentManager(), "show_friends_dialog");
                }
            });

            if (joinedUsers.size() >= 1) {
                StorageReference storageReferenceFirst = FirebaseStorage.getInstance().getReference()
                        .child(PROFILE_PICTURES_BUCKET_REFERENCE).child(joinedUsers.get(0).getUserId());

                Glide.with(getApplicationContext())
                        .load(storageReferenceFirst)
                        .placeholder(R.drawable.logo)
                        .into(firstJoined);
            }
            if (joinedUsers.size() >= 2){
                StorageReference storageReferenceSecond = FirebaseStorage.getInstance().getReference()
                        .child(PROFILE_PICTURES_BUCKET_REFERENCE).child(joinedUsers.get(1).getUserId());

                Glide.with(getApplicationContext())
                        .load(storageReferenceSecond)
                        .placeholder(R.drawable.logo)
                        .into(secondJoined);
            }
        }


        join = findViewById(R.id.button_join);
        doNotJoin = findViewById(R.id.button_do_not_join);

        handleButtonsConfiguration(usersInfo);
        join.setOnClickListener(v -> {
            join.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.md_theme_light_surfaceTint));
            doNotJoin.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.divisor));
            somethingChange = 1;
        });

        doNotJoin.setOnClickListener(v -> {
            doNotJoin.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.md_theme_dark_error));
            join.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.divisor));
            somethingChange = 2;
        });

        ShapeableImageView eventImage = findViewById(R.id.event_manager_avatar);
        String manager = event.getEvent().getManager();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(PROFILE_PICTURES_BUCKET_REFERENCE).child(manager);

        Glide.with(this)
                .load(storageReference)
                .placeholder(R.drawable.logo)
                .into(eventImage);


        String date_time = event.getEvent().getDate();
        String formatted_date = Parser.formatDate(date_time, this);
        String formatted_time = Parser.formatTime(date_time);
        String location = event.getEvent().getLocation();
        String showLocation = Parser.formatLocation(location);
        double[] cord = Parser.getCord(location);

        eventDate.setText(formatted_date);
        eventHour.setText(formatted_time); //TODO: Dividere la data dall'ora
        eventName.setText(event.getEvent().getName());
        eventAddress.setText(showLocation);
        eventDescription.setText(event.getEvent().getDescription());

    }

    private List<UserEventCrossRef> getJoinedUsers(List<UserEventCrossRef> usersInfo) {
        List<UserEventCrossRef> joinedUsers = new ArrayList<>();
        for (UserEventCrossRef current : usersInfo) {
            if (current.getJoined())
                joinedUsers.add(current);
        }
        return joinedUsers;
    }

    private void handleButtonsConfiguration(List<UserEventCrossRef> usersInfo) {
        for (UserEventCrossRef current : usersInfo) {
            if (current.getUserId().equals(this.user.getUserId()) && current.getJoined()) {
                join.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.md_theme_light_surfaceTint));
                doNotJoin.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.divisor));
                break;
            } else if (current.getUserId().equals(this.user.getUserId()) && !current.getJoined()) {
                doNotJoin.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.md_theme_dark_error));
                join.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.divisor));
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
    private void returnResultToCallingActivity(int change, String eventId) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("change", change);
        resultIntent.putExtra("id", eventId);
        setResult(Activity.RESULT_OK, resultIntent);
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        String location = event.getEvent().getLocation();
        String showLocation = Parser.formatLocation(location);
        double[] cord = Parser.getCord(location);
        LatLng place = new LatLng(cord[0], cord[1]);
        googleMap.addMarker(new MarkerOptions()
                .position(place)
                .title(event.getEvent().getName()));
        // [START_EXCLUDE silent]
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(place));
    }
}

