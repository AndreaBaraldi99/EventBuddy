package it.lanos.eventbuddy.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.util.ServiceLocator;

public class CreateEventActivity extends AppCompatActivity{

    private EventViewModel eventViewModel;
    private TextInputLayout eventNameTextInputLayout;
    private TextInputLayout dateTextInputLayout;
    private TextInputLayout timeTextInputLayout;
    private TextInputLayout locationTextInputLayout;

    private String description;


    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following
    // methods defined by the NoticeDialogFragment.NoticeDialogListener
    // interface.


    public void onDescriptionDialogConfirmClick(String text, AddDescriptionFragment fragment){
       this.description = text;
       fragment.dismiss();
    }
    public void onGuestDialogConfirmClick() {

    }

    public void onDialogCancelClick(DialogFragment fragment){
        fragment.dismiss();
    }

    public void openAddDescriptionDialog(

    ) {
        DialogFragment newFragment = new AddDescriptionFragment();
        newFragment.show(getSupportFragmentManager(), "add description");
    }

    public void openAddGuestDialog(

    ) {
        DialogFragment newFragment = new AddGuestsFragment();
        newFragment.show(getSupportFragmentManager(), "add guests");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        ExtendedFloatingActionButton addButton = findViewById(R.id.extended_fab);
        Button addDescrButton = findViewById(R.id.DescriptionIconButton);
        Button addGuestButton = findViewById(R.id.GuestsIconButton);

        eventNameTextInputLayout = findViewById(R.id.EventNameTextInputLayout);
        dateTextInputLayout = findViewById(R.id.DateTextInputLayout);
        timeTextInputLayout = findViewById(R.id.TimeTextInputLayout);
        locationTextInputLayout = findViewById(R.id.LocationTextInputLayout);

        addDescrButton.setOnClickListener(v -> openAddDescriptionDialog());
        addGuestButton.setOnClickListener(v -> openAddGuestDialog());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: IL PROBLEMA è QUI
                String event_name = eventNameTextInputLayout.getEditText().getText().toString();
                String date_time = dateTextInputLayout.getEditText().getText().toString() + timeTextInputLayout.getEditText().getText().toString();
                String location = locationTextInputLayout.getEditText().getText().toString();
                List<User> guests = new ArrayList<User>();
                guests.add((new User("134", "lu", "lucrezia")));
                Event event = new Event(event_name, date_time, location, description);

                EventWithUsers finalEvent = new EventWithUsers(event, guests);
                eventViewModel.addEvent(finalEvent);

            }
        });

    }


}