package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.LAST_UPDATE;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.Worker.DummyWorker;
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
    private UUID lastWorkId;
    private LiveData<WorkInfo> currentWorkInfoLiveData;
    private Observer<WorkInfo> workInfoObserver;
    Observer<WorkInfo> periodicWorkInfoObserver;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IEventsRepository iEventsRepository =
                ServiceLocator.getInstance().getEventsRepository(requireActivity().getApplication());

        eventViewModel = new ViewModelProvider(
                requireActivity(),
                new EventViewModelFactory(iEventsRepository)).get(EventViewModel.class);

        eventList = new ArrayList<>();

        sharedPreferencesUtil = new SharedPreferencesUtil(requireActivity().getApplication());

        workManager = WorkManager.getInstance(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            //Navigation.findNavController(v).navigate(R.id.action_eventFragment_to_createEventActivity2);
            startCreateEventActivity();
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
                this::startDetailEventActivity, requireContext()
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
                eventList.sort(new DateTimeComparator());
                eventRecyclerViewAdapter.notifyDataSetChanged();
                //TODO: gestire eccezione
        }});

        periodicWorkInfoObserver = workInfo -> {
            Log.d("UPDATEDUMMY", "entrato nel periodicWorkInfoObserver");
            if (workInfo != null) {
                Log.d("UPDATEDUMMY", "entrato nel workInfo");

                int originalSize = eventList.size();
                eventViewModel.getEvents(Long.parseLong(lastUpdate)).observe(getViewLifecycleOwner(), result -> {
                    if (result.isSuccess() && eventList.size() == originalSize) {
                        sendNotification("New events have been added!");
                        Log.d("UPDATEDUMMY", "notifica mandata");
                    }
                });
            }
        };

       // Schedule PeriodicWorkRequest
        schedulePeriodicWork();

        // Observer for OneTimeRequest
        workInfoObserver = workInfo -> {
            if (workInfo != null && workInfo.getState().isFinished()) {
                Data outputData = workInfo.getOutputData();
                String lastUpdate = outputData.getString("lastUpdate");

                assert lastUpdate != null;
                eventViewModel.getEvents(Long.parseLong(lastUpdate));

                scheduleNextWork();
            }
        };

        // Plans first work
        if (lastWorkId == null) {
            scheduleNextWork();
        }
    }

    private void schedulePeriodicWork() {
        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(DummyWorker.class, 15, TimeUnit.MINUTES)
                        .build();

        workManager.enqueueUniquePeriodicWork("periodicWorkRequest", ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);

        LiveData<WorkInfo> periodicWorkInfoLiveData = workManager.getWorkInfoByIdLiveData(periodicWorkRequest.getId());
        periodicWorkInfoLiveData.observe(getViewLifecycleOwner(), periodicWorkInfoObserver);
    }

    private void scheduleNextWork() {
        OneTimeWorkRequest newWorkRequest =
                new OneTimeWorkRequest.Builder(UpdateEventsWorker.class)
                        .setInitialDelay(30, TimeUnit.SECONDS)
                        .build();

        lastWorkId = newWorkRequest.getId();
        workManager.enqueue(newWorkRequest);

        observeWorkInfo(lastWorkId);
    }
    private void observeWorkInfo(UUID workId) {
        removeOldObserver();

        currentWorkInfoLiveData = workManager.getWorkInfoByIdLiveData(workId);
        currentWorkInfoLiveData.observe(getViewLifecycleOwner(), workInfoObserver);
    }

    private void removeOldObserver() {
        if (currentWorkInfoLiveData != null) {
            currentWorkInfoLiveData.removeObserver(workInfoObserver);
        }
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

    private void sendNotification(String message) {
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("EventUpdates", "Event Updates", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications for event updates");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "EventUpdates")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Event Update")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
    }

}
