package it.lanos.eventbuddy.UI;

import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.util.ServiceLocator;

public class EventFragment extends Fragment {

    private List<EventWithUsers> eventList;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    private EventViewModel eventViewModel;




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

        eventViewModel = new ViewModelProvider(
                this,
                new EventViewModelFactory(iEventsRepository)).get(EventViewModel.class);

        eventList = new ArrayList<>();

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
                Navigation.findNavController(v).navigate(R.id.action_eventFragment_to_createEventActivity2);
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

        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(eventList,
                requireActivity().getApplication(),
                new EventRecyclerViewAdapter.OnItemClickListener(){
                    @Override
                    public void onEventItemClick(EventWithUsers event){
                        EventFragmentDirections.GoToEventDetail action =
                                EventFragmentDirections.goToEventDetail();
                        action.setEventClick(event);

                        Navigation.findNavController(view).navigate((NavDirections) action);


                    }
                }
        );

        recyclerViewEvent.setLayoutManager(layoutManager);
        recyclerViewEvent.setAdapter(eventRecyclerViewAdapter);
        String lastUpdate = "0";

        eventViewModel.getEvents(Long.parseLong(lastUpdate)).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                int initialSize = this.eventList.size();
                this.eventList.clear();
                this.eventList.addAll(((Result.Success) result).getData());
                eventRecyclerViewAdapter.notifyItemRangeInserted(initialSize, this.eventList.size());
                //TODO: gestire eccezione
        }});
    }
}

