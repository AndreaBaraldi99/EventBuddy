package it.lanos.eventbuddy.UI;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.ISuggestionsRepository;
import it.lanos.eventbuddy.data.SuggestionsRepository;
import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.mapbox.Feature;
import it.lanos.eventbuddy.data.source.models.mapbox.Suggestion;
import it.lanos.eventbuddy.util.ServiceLocator;

public class CreateEventActivity extends AppCompatActivity{
    private EventViewModel eventViewModel;
    private TextInputLayout eventNameTextInputLayout;
    private TextInputLayout dateTextInputLayout;
    private TextInputLayout timeTextInputLayout;
    private TextInputLayout locationTextInputLayout;

    private String address;

    private boolean flagHandle = true;

    private CreateEventViewModel createEventViewModel;

    private String description;

    private List<User> userList;

    private List<Suggestion> suggestionList;

    SuggestionsRepository iSuggestionsRepository;

    private Feature selectedFeature;

    private ArrayAdapter<Suggestion> addressAdapter;


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
        createEventToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(CreateEventActivity.this);
            }
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
                Iterator it = allSuggestion.iterator();
                while(it.hasNext()){
                    Suggestion current = (Suggestion) it.next();
                    if(current.full_address != null){
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
        //TODO: Sostituire metodo deprecato
        searchEditText.setHintTextColor(getResources().getColor(R.color.md_theme_light_onSurfaceVariant));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        suggestionList = new ArrayList<>();
        ListView suggestionListView = findViewById(R.id.suggestionListView);
        addressAdapter = new suggestionAdapter(this, R.layout.address_item, suggestionList);
        suggestionListView.setAdapter(addressAdapter);
        suggestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            }
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
        eventNameTextInputLayout.getEditText().getCurrentHintTextColor();
        dateTextInputLayout = findViewById(R.id.DateTextInputLayout);
        timeTextInputLayout = findViewById(R.id.TimeTextInputLayout);

        //locationTextInputLayout = findViewById(R.id.LocationTextInputLayout);

        addDescrButton.setOnClickListener(v -> openAddDescriptionDialog());
        addGuestButton.setOnClickListener(v -> openAddGuestDialog());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event_name = Objects.requireNonNull(eventNameTextInputLayout.getEditText()).getText().toString();
                String date_time = Objects.requireNonNull(dateTextInputLayout.getEditText()).getText().toString()+"/"+timeTextInputLayout.getEditText().getText().toString();
                Event event = new Event(event_name, date_time, address, description);
                EventWithUsers finalEvent = new EventWithUsers(event, userList);
                returnResultToCallingActivity(finalEvent);
            }
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