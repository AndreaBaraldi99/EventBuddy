package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.ILocationRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Location;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;
import it.lanos.eventbuddy.data.source.models.mapbox.Geometry;
import it.lanos.eventbuddy.util.DataEncryptionUtil;
import it.lanos.eventbuddy.util.DateTimeComparator;
import it.lanos.eventbuddy.util.Parser;
import it.lanos.eventbuddy.util.ServiceLocator;
import androidx.core.app.ActivityCompat;


public class ActiveFragment extends Fragment implements OnMapReadyCallback {

    private EventViewModel eventViewModel;

    private LocationViewModel locationViewModel;

    private List<EventWithUsers> eventList;

    private List<Location> locationList;

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

        ILocationRepository iLocationRepository =
                ServiceLocator.getInstance().getLocationRepository(requireActivity().getApplication());

        locationViewModel = new ViewModelProvider(
                requireActivity(),
                new LocationViewModelFactory(iLocationRepository)).get(LocationViewModel.class);



        eventList = new ArrayList<>();

        locationList = new ArrayList<>();


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

            locationViewModel.getLocation(selected.getEvent().getEventId()).observe(getViewLifecycleOwner(), result -> {
                if (result.isSuccess()) {
                    this.locationList.clear();
                    this.locationList.add(((Result.LocationSuccess) result).getLocation());
                }});



            MaterialToolbar toolbar = view.findViewById(R.id.appbar_active_frag);
            TextView address = view.findViewById(R.id.active_event_address);
            TextView time = view.findViewById(R.id.active_event_time);
            MaterialButton googleMapsButton = view.findViewById(R.id.googleMapsButton);
            ImageView mapIcon = view.findViewById(R.id.event_map_icon);
            ImageView sfondo = view.findViewById(R.id.sfondo);
            TextView noEvent = view.findViewById(R.id.noActiveEventFound);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.active_map);
            if(selected != null) {
                //mapFragment.getView().setVisibility(View.VISIBLE);
                mapFragment.getMapAsync(this);
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



            }
            else{
                googleMapsButton.setVisibility(View.GONE);
                mapIcon.setVisibility(View.GONE);
                sfondo.setVisibility(View.GONE);
                noEvent.setVisibility(View.VISIBLE);
                mapFragment.getView().setVisibility(View.GONE);
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        String location = selected.getEvent().getLocation();
        String showLocation = Parser.formatLocation(location);
        double[] cord = Parser.getCord(location);
        LatLng place = new LatLng(cord[0], cord[1]);
        googleMap.addMarker(new MarkerOptions()
                .position(place)
                .title(selected.getEvent().getName()));
        // [START_EXCLUDE silent]
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15));

    }
}