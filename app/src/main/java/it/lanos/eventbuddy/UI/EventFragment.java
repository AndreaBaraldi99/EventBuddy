package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.Worker.UpdateEventsWorker;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.util.DateTimeComparator;
import it.lanos.eventbuddy.util.ServiceLocator;
import it.lanos.eventbuddy.util.SharedPreferencesUtil;

public class EventFragment extends Fragment {

    private List<EventWithUsers> eventList;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    private EventViewModel eventViewModel;

    private WorkManager workManager;
    private SharedPreferencesUtil sharedPreferencesUtil;

    private String lastUpdate;


    private final ActivityResultLauncher<Intent> createEventLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // L'Activity CREATE_EVENT ha restituito con successo il risultato

                    Intent data = result.getData();
                    if (data != null) {
                        // Estrarre l'oggetto EventWithUsers dal risultato
                        EventWithUsers eventWithUsers = data.getParcelableExtra("new_event");

                        // Ora puoi utilizzare l'oggetto eventWithUsers come necessario
                        // ad esempio, aggiornare l'UI con il nuovo evento
                        eventViewModel.addEvent(eventWithUsers);
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> detailEventLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // L'Activity CREATE_EVENT ha restituito con successo il risultato

                    Intent data = result.getData();
                    if (data != null) {
                        // Estrarre l'oggetto EventWithUsers dal risultato
                        int change = data.getIntExtra("change", 0);
                        String id = data.getStringExtra("id");
                        if(change == 1) {
                            eventViewModel.joinEvent(id);
                        }
                        else if(change == 2){
                            eventViewModel.leaveEvent(id);
                        }
                    }
                }
            }
    );




    public EventFragment() {
        // Required empty public constructor
    }


    public static EventFragment newInstance() {
        EventFragment fragment = new EventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IEventsRepository iEventsRepository =
                ServiceLocator.getInstance().getEventsRepository(requireActivity().getApplication());

        /*eventViewModel = new ViewModelProvider(
                this,
                new EventViewModelFactory(iEventsRepository)).get(EventViewModel.class);*/
        eventViewModel = new ViewModelProvider(
                requireActivity(),
                new EventViewModelFactory(iEventsRepository)).get(EventViewModel.class);

        eventList = new ArrayList<>();

        sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());

        workManager = WorkManager.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigation.findNavController(v).navigate(R.id.action_eventFragment_to_createEventActivity2);
                startCreateEventActivity();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.topMargin = insets.top;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

        RecyclerView recyclerViewEvent = view.findViewById(R.id.recyclerViewEventList);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(eventViewModel, eventList,
                requireActivity().getApplication(),
                new EventRecyclerViewAdapter.OnItemClickListener(){
                    @Override
                    public void onEventItemClick(EventWithUsers event){
                        /*EventFragmentDirections.GoToEventDetail action =
                                EventFragmentDirections.goToEventDetail();
                        action.setEventClick(event);

                        Navigation.findNavController(view).navigate(action);*/
                        startDetailEventActivity(event);

                    }
                }
        );

        recyclerViewEvent.setLayoutManager(layoutManager);
        recyclerViewEvent.setAdapter(eventRecyclerViewAdapter);
        lastUpdate = "0";
        if (sharedPreferencesUtil.readStringData(
                SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE) != null) {
            lastUpdate = sharedPreferencesUtil.readStringData(
                    SHARED_PREFERENCES_FILE_NAME, LAST_UPDATE);
        }


        eventViewModel.getEvents(Long.parseLong(lastUpdate)).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                this.eventList.clear();
                this.eventList.addAll(((Result.Success) result).getData());
                Collections.sort(eventList, new DateTimeComparator());
                eventRecyclerViewAdapter.notifyDataSetChanged();
                //TODO: gestire eccezione
        }});



        //TODO: Eliminare in caso non funzioni
        PeriodicWorkRequest updateRequest =
                new PeriodicWorkRequest.Builder(UpdateEventsWorker.class, 15, TimeUnit.SECONDS).build();

        workManager.enqueue(updateRequest);


        //Questo è da rivedere
        workManager.getWorkInfoByIdLiveData(updateRequest.getId()).observe(getViewLifecycleOwner(), info ->{
            if(info.getState() == WorkInfo.State.SUCCEEDED) {
                Data output = info.getOutputData();
                lastUpdate = output.getString("update");
                eventViewModel.getEvents(Long.parseLong(lastUpdate));
            }
        });


    }


    private void startCreateEventActivity() {
        Intent intent = new Intent(requireContext(), CreateEventActivity.class);
        createEventLauncher.launch(intent);
    }

    private void startDetailEventActivity(EventWithUsers event) {
        Intent intent = new Intent(requireContext(), EventDetailActivity.class);
        intent.putExtra("event", event);
        detailEventLauncher.launch(intent);
    }


}

