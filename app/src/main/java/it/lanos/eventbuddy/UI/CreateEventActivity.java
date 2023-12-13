package it.lanos.eventbuddy.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.entities.Event;
import it.lanos.eventbuddy.data.source.entities.EventWithUsers;
import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.util.ServiceLocator;

public class CreateEventActivity extends AppCompatActivity{

    private EventViewModel eventViewModel;
    private TextInputLayout eventNameTextInputLayout;
    private TextInputLayout dateTextInputLayout;
    private TextInputLayout timeTextInputLayout;
    private TextInputLayout locationTextInputLayout;

    private String description;
    private final String TAG = CreateEventActivity.class.getSimpleName();


    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following
    // methods defined by the NoticeDialogFragment.NoticeDialogListener
    // interface.


    public void onDialogConfirmClick(String text, AddDescriptionFragment fragment){
       this.description = text;
       fragment.dismiss();
    }

    public void onDialogCancelClick(AddDescriptionFragment fragment){
        fragment.dismiss();
    }

    public void openAddDescriptionDialog() {
        DialogFragment newFragment = new AddDescriptionFragment();
        newFragment.show(getSupportFragmentManager(), "add description");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        IEventsRepository iEventsRepository =
                ServiceLocator.getInstance().getEventsRepository(getApplication());

        eventViewModel = new ViewModelProvider(
                this,
                new EventViewModelFactory(iEventsRepository)).get(EventViewModel.class);

        List<User> users = new ArrayList<>();

        Button addButton = findViewById(R.id.createEventButton);
        Button addDescrButton = findViewById(R.id.DescriptionIconButton);

        eventNameTextInputLayout = findViewById(R.id.EventNameTextInputLayout);
        dateTextInputLayout = findViewById(R.id.DateTextInputLayout);
        timeTextInputLayout = findViewById(R.id.TimeTextInputLayout);
        locationTextInputLayout = findViewById(R.id.LocationTextInputLayout);

        addDescrButton.setOnClickListener(v -> openAddDescriptionDialog());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event_name = eventNameTextInputLayout.getEditText().getText().toString();
                String date_time = dateTextInputLayout.getEditText().getText().toString() + timeTextInputLayout.getEditText().getText().toString();
                String location = locationTextInputLayout.getEditText().getText().toString();
                Log.d(TAG, description);
                Log.d(TAG, event_name);
                //List<User> partecipanti = new ArrayList<User>();
                //String description = "";
                //Event evento = new Event(0, 0, event_name, date_time, location, description);

                //EventWithUsers eventoFinale = new EventWithUsers(evento, partecipanti);
                //eventViewModel.addEvent(eventoFinale);

            }
        });

    }


}