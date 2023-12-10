package it.lanos.eventbuddy.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.entities.User;
import it.lanos.eventbuddy.util.ServiceLocator;

public class CreateEventActivity extends AppCompatActivity implements AddDescriptionFragment.NoticeDialogListener {

    private EventViewModel eventViewModel;
    private TextInputLayout eventNameTextInputLayout;
    private TextInputLayout dateTextInputLayout;
    private TextInputLayout timeTextInputLayout;
    private TextInputLayout locationTextInputLayout;

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following
    // methods defined by the NoticeDialogFragment.NoticeDialogListener
    // interface.
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //dialog.dismiss();
        //TODO: mandare i dati della textview
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //dialog.dismiss();
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

    }


}