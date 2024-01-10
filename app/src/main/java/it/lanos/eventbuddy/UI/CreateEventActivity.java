package it.lanos.eventbuddy.UI;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.SuggestionsRepository;
import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.mapbox.Feature;
import it.lanos.eventbuddy.data.source.models.mapbox.Suggestion;
import it.lanos.eventbuddy.util.ServiceLocator;

public class CreateEventActivity extends AppCompatActivity{
    private TextInputLayout eventNameTextInputLayout;
    private TextInputLayout dateTextInputLayout;
    private TextInputLayout timeTextInputLayout;

    private String address;

    private boolean flagHandle = true;

    private CreateEventViewModel createEventViewModel;

    private String description;

    private String date;

    private String time;

    private List<User> userList;

    private List<Suggestion> suggestionList;

    SuggestionsRepository iSuggestionsRepository;

    private Feature selectedFeature;

    private ArrayAdapter<Suggestion> addressAdapter;


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

    public void onGuestAddClick(User user){
        userList.add(user);
    }
    public void onGuestRemoveClick(User user){userList.remove(user);}

    public String getDescription() {
        return description;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guest_search_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName component = new ComponentName(this, CreateEventActivity.class);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(component);
        assert searchView != null;
        searchView.setSearchableInfo(searchableInfo);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        MaterialToolbar createEventToolbar = findViewById(R.id.create_event_toolbar);
        createEventToolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(CreateEventActivity.this));

        dateTextInputLayout = findViewById(R.id.DateTextInputLayout);
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(R.string.date_picker_title);
        builder.build();
        builder.setTheme(R.style.MyDatePicker);
        MaterialDatePicker<Long> picker = builder.build();
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
        Objects.requireNonNull(dateTextInputLayout.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                picker.show(getSupportFragmentManager(), "date_picker");
            }
        });
        picker.addOnPositiveButtonClickListener(selection -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = simpleDateFormat.format(selection);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(date, formatter);
                if (localDate.isBefore(LocalDate.now())) {
                    Toast.makeText(CreateEventActivity.this, R.string.date_picker_error, Toast.LENGTH_SHORT).show();
                } else {
                    dateTextInputLayout.getEditText().setText(date);
                }
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date = simpleDateFormat.format(selection);

                dateTextInputLayout.getEditText().setText(date);
            }
        });

        timeTextInputLayout = findViewById(R.id.TimeTextInputLayout);
        MaterialTimePicker.Builder builderTime = new MaterialTimePicker.Builder();
        builderTime.setTitleText(R.string.time_picker_title)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .build();
        builderTime.setTheme(R.style.MyTimePicker);
        MaterialTimePicker pickerTime = builderTime.build();
        Objects.requireNonNull(timeTextInputLayout.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                pickerTime.show(getSupportFragmentManager(), "time_picker");
            }
        });
        pickerTime.addOnPositiveButtonClickListener(selection -> {
            time = pickerTime.getHour() + ":" + pickerTime.getMinute();
            timeTextInputLayout.getEditText().setText(time);
        });

        iSuggestionsRepository = (SuggestionsRepository)
                ServiceLocator.getInstance().getSuggestionsRepository(getApplication());

        createEventViewModel = new ViewModelProvider(
                this,
                new CreateEventViewModelFactory(iSuggestionsRepository)).get(CreateEventViewModel.class);


        //GESTIONE INDIRIZZO
        //iSuggestionsRepository
        createEventViewModel.attachSuggestions().observe(this, result -> {
            if (result.isSuggestionSuccess()) {
                this.suggestionList.clear();
                List<Suggestion> allSuggestion = (((Result.SuggestionsSuccess) result).getData());
                for (Suggestion current : allSuggestion) {
                    if (current.full_address != null) {
                        this.suggestionList.add(current);
                    }
                }
                addressAdapter.notifyDataSetChanged();
                //TODO: gestire eccezione
            }
        });

        createEventViewModel.attachFeature().observe(this, result -> {
            if(result.isFeatureSuccess()){
                this.selectedFeature = ((Result.FeatureSuccess) result).getData().get(0);
                this.address = selectedFeature.properties.context.place.name
                        +", "
                        +selectedFeature.properties.address
                        +"/"
                        +selectedFeature.properties.coordinates.latitude
                        +"_"
                        +selectedFeature.properties.coordinates.longitude;

            }
        });
        SearchView mySearchView = findViewById(R.id.locationSearch);
        EditText searchEditText = mySearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(getResources().getColor(R.color.md_theme_light_onSurfaceVariant, null));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        suggestionList = new ArrayList<>();
        ListView suggestionListView = findViewById(R.id.suggestionListView);
        addressAdapter = new suggestionAdapter(this, R.layout.address_item, suggestionList);
        suggestionListView.setAdapter(addressAdapter);
        suggestionListView.setOnItemClickListener((parent, view, position, id) -> {
            flagHandle = false;
            String query = suggestionList.get(position).mapbox_id;
            createEventViewModel.getFeature(query);
            searchEditText.setText(suggestionList.get(position).context.place.name
                    +", "
                    +suggestionList.get(position).address);
            suggestionList.clear();
            addressAdapter.notifyDataSetChanged();
            mySearchView.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            flagHandle = true;
        });
        suggestionListView.bringToFront();


        userList = new ArrayList<>();

        ExtendedFloatingActionButton addButton = findViewById(R.id.extended_fab);
        Button addDescrButton = findViewById(R.id.DescriptionIconButton);
        Button addGuestButton = findViewById(R.id.GuestsIconButton);



        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(flagHandle)
                    handleSearch(newText);
                return false;
            }
        });


        eventNameTextInputLayout = findViewById(R.id.EventNameTextInputLayout);
        Objects.requireNonNull(eventNameTextInputLayout.getEditText()).getCurrentHintTextColor();


        addDescrButton.setOnClickListener(v -> openAddDescriptionDialog());
        addGuestButton.setOnClickListener(v -> openAddGuestDialog());
        addButton.setOnClickListener(v -> {
            String event_name = Objects.requireNonNull(eventNameTextInputLayout.getEditText()).getText().toString();
            String date_time = date + "/"+ time;
            if (event_name.isEmpty() || address.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(CreateEventActivity.this, R.string.fill_fields_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Event event = new Event(event_name, date_time, address, description);
            EventWithUsers finalEvent = new EventWithUsers(event, userList);
            returnResultToCallingActivity(finalEvent);
        });
    }
    private void returnResultToCallingActivity(EventWithUsers eventWithUsers) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_event", eventWithUsers);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void handleSearch(String query){
        if (query.length() < 3){
            suggestionList.clear();
            addressAdapter.notifyDataSetChanged();
        }
        else{
            createEventViewModel.getSuggestions(query);
        }
    }


}