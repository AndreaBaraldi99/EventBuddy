package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;
import it.lanos.eventbuddy.util.DataEncryptionUtil;
import it.lanos.eventbuddy.util.DateTimeComparator;
import it.lanos.eventbuddy.util.Parser;
import it.lanos.eventbuddy.util.ServiceLocator;


public class ActiveFragment extends Fragment {

    private EventViewModel eventViewModel;

    private List<EventWithUsers> eventList;

    private EventWithUsers selected;

    private User user;

    public ActiveFragment() {
        // Required empty public constructor
    }


    public static ActiveFragment newInstance(String param1, String param2) {
        ActiveFragment fragment = new ActiveFragment();
        return fragment;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active, container, false);

        // Applica lo stile al fragment
        //TODO: Capire come rendere il fragment fullscreen cosi da eliminare lo spazio bianco in cima al telefono
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            requireActivity().getWindow().setNavigationBarColor(Color.TRANSPARENT);
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }*/
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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


            String lastUpdate = "0";

            eventViewModel.getEvents(Long.parseLong(lastUpdate)).observe(getViewLifecycleOwner(), result -> {
                if (result.isSuccess()) {
                    this.eventList.clear();
                    this.eventList.addAll(((Result.Success) result).getData());
                    Collections.sort(eventList, new DateTimeComparator());
                    this.selected = pickRightEvent(eventList);
                    //TODO: gestire eccezione
                }});

            MaterialToolbar toolbar = view.findViewById(R.id.appbar_active_frag);
            TextView address = view.findViewById(R.id.active_event_address);
            TextView time = view.findViewById(R.id.active_event_time);
            MaterialButton googleMapsButton = view.findViewById(R.id.googleMapsButton);
            MapView map = view.findViewById(R.id.active_mapView);
            ImageView mapIcon = view.findViewById(R.id.event_map_icon);
            ImageView sfondo = view.findViewById(R.id.sfondo);
            TextView noEvent = view.findViewById(R.id.noActiveEventFound);
            if(selected != null) {
                toolbar.setTitle(selected.getEvent().getName());
                String formattedAddress = Parser.formatLocation(selected.getEvent().getLocation());
                address.setText(formattedAddress);
                String formattedTime = Parser.formatTime(selected.getEvent().getDate());
                time.setText(formattedTime);

                googleMapsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Indirizzo da cercare su Google Maps
                        String addressToSearch = selected.getEvent().getLocation().split("/")[0];

                        // Costruisci l'URI per l'intent di Google Maps
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(addressToSearch));

                        // Crea un intent con l'azione ACTION_VIEW
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                        startActivity(mapIntent);

                    }
                });


                String location = selected.getEvent().getLocation();

                double[] cord = Parser.getCord(location);



                map.getMapboxMap().loadStyleUri(
                        Style.STANDARD,
                        new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                CameraOptions camera = new CameraOptions.Builder().center(Point.fromLngLat(cord[1], cord[0]))
                                        .zoom(15.5).build();
                                map.getMapboxMap().setCamera(camera);
                            }
                        }
                );
            }
            else{
                map.setVisibility(View.GONE);
                googleMapsButton.setVisibility(View.GONE);
                mapIcon.setVisibility(View.GONE);
                sfondo.setVisibility(View.GONE);
                noEvent.setVisibility(View.VISIBLE);
            }





            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private EventWithUsers pickRightEvent(List<EventWithUsers> eventList) {
        readUser(new DataEncryptionUtil(requireActivity().getApplication()));
        Iterator itE = eventList.iterator();
        while(itE.hasNext()) {
            EventWithUsers current = (EventWithUsers) itE.next();
            List<UserEventCrossRef> crossList = current.getUserEventCrossRefs();
            Iterator itC = crossList.iterator();
            while(itC.hasNext()){
                UserEventCrossRef currentCross = (UserEventCrossRef) itC.next();
                if (currentCross.getUserId().equals(this.user.getUserId()) && currentCross.getJoined()) {
                    return current;
                }

            }
        }
        return null;
    }

    private void readUser(DataEncryptionUtil dataEncryptionUtil){
        try {
            this.user = new Gson().fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}