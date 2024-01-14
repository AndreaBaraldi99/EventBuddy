package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.lanos.eventbuddy.util.Constants.SHARED_PREFERENCES_FILE_NAME;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.ILocationRepository;
import it.lanos.eventbuddy.data.source.models.EventWithUsers;
import it.lanos.eventbuddy.data.source.models.Location;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.UserEventCrossRef;
import it.lanos.eventbuddy.util.DataEncryptionUtil;
import it.lanos.eventbuddy.util.DateTimeComparator;
import it.lanos.eventbuddy.util.LocationService;
import it.lanos.eventbuddy.util.Parser;
import it.lanos.eventbuddy.util.ServiceLocator;
import it.lanos.eventbuddy.util.SharedPreferencesUtil;


public class ActiveFragment extends Fragment implements OnMapReadyCallback {

    private EventViewModel eventViewModel;

    private LocationViewModel locationViewModel;

    private List<EventWithUsers> eventList;

    private EventWithUsers selected;

    private GoogleMap googleMap;

    private FusedLocationProviderClient fusedLocationClient;

    private Map<String, MarkerOptions> markers;

    private Map<String, BitmapDescriptor> usersPic;

    private Thread sendPositions;

    private Intent locIntent;

    private User user;


    public ActiveFragment() {
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

        ILocationRepository iLocationRepository =
                ServiceLocator.getInstance().getLocationRepository(requireActivity().getApplication());

        locationViewModel = new ViewModelProvider(
                requireActivity(),
                new LocationViewModelFactory(iLocationRepository)).get(LocationViewModel.class);


        eventList = new ArrayList<>();

        markers = new HashMap<>();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        usersPic = new HashMap<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_active, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

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
                    eventList.sort(new DateTimeComparator());
                    this.selected = pickRightEvent(eventList);
                    //TODO: gestire eccezione
                }
            });


            MaterialToolbar toolbar = view.findViewById(R.id.appbar_active_frag);
            TextView address = view.findViewById(R.id.active_event_address);
            TextView time = view.findViewById(R.id.active_event_time);
            MaterialButton googleMapsButton = view.findViewById(R.id.googleMapsButton);
            ImageView mapIcon = view.findViewById(R.id.event_map_icon);
            ImageView sfondo = view.findViewById(R.id.sfondo);
            TextView noEvent = view.findViewById(R.id.noActiveEventFound);
            TextView trackMeText = view.findViewById(R.id.trackMeText);
            MaterialSwitch trackMeSwitch = view.findViewById(R.id.trackMeSwitch);
            CardView cardView = view.findViewById(R.id.active_card);
            SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(getContext());
            if(sharedPreferencesUtil.readStringData(SHARED_PREFERENCES_FILE_NAME,"track") == null){
                sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME, "track", String.valueOf(trackMeSwitch.isChecked()));
            }

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.active_map);
            if (selected != null) {
                locIntent = new Intent(getContext(), LocationService.class);
                trackMeSwitch.setChecked(Boolean.parseBoolean(sharedPreferencesUtil.readStringData(SHARED_PREFERENCES_FILE_NAME, "track")));
                if(trackMeSwitch.isChecked()){
                    locIntent.setAction(LocationService.START_STICKY);
                    sendPositionLoop();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        requireContext().startForegroundService(locIntent);
                    }
                }
                assert mapFragment != null;
                mapFragment.getMapAsync(this);
                toolbar.setTitle(selected.getEvent().getName());
                String formattedAddress = Parser.formatLocation(selected.getEvent().getLocation());
                address.setText(formattedAddress);
                String formattedTime = Parser.formatTime(selected.getEvent().getDate());
                time.setText(formattedTime);


                trackMeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME, "track", String.valueOf(trackMeSwitch.isChecked()));
                    if(isChecked){
                        locIntent.setAction(LocationService.START_STICKY);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            requireContext().startForegroundService(locIntent);
                        }
                        sendPositionLoop();
                    }
                    else{
                        locIntent.setAction(LocationService.ACTION_STOP);
                        requireContext().stopService(locIntent);
                        sendPositions.interrupt();
                    }

                });

                googleMapsButton.setOnClickListener(v1 -> {
                    String addressToSearch = selected.getEvent().getLocation().split("/")[0];
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(addressToSearch));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    startActivity(mapIntent);
                });




            } else {
                googleMapsButton.setVisibility(View.GONE);
                mapIcon.setVisibility(View.GONE);
                sfondo.setVisibility(View.GONE);
                noEvent.setVisibility(View.VISIBLE);
                assert mapFragment != null;
                mapFragment.requireView().setVisibility(View.GONE);
                trackMeText.setVisibility(View.GONE);
                trackMeSwitch.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);
            }



            return WindowInsetsCompat.CONSUMED;
        });


    }

    private void sendPositionLoop() {
        sendPositions = new Thread(() -> {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            while(true) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(result -> {
                    if (result == null) {
                        return;
                    }

                    Location loc = new Location(result.getLatitude(), result.getLongitude(), selected.getEvent().getEventId());
                    locationViewModel.setLocation(loc);

                });
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        sendPositions.start();
    }

    private void loadPic() throws InterruptedException {
        if (selected != null) {
            Thread thread = new Thread(() -> {
                for (User u : selected.getUsers()) {
                    try {
                        InputStream is = (InputStream) new URL(u.getProfilePictureUrl()).getContent();
                        Drawable d = Drawable.createFromStream(is, "src name");
                        assert d != null;
                        Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) d).getBitmap(), 64, 64, false);
                        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
                        usersPic.put(u.getUserId(), bd);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
            );
            thread.start();
            thread.join();
            setFriendsTracker(googleMap);
        }
    }

    private EventWithUsers pickRightEvent(List<EventWithUsers> eventList) {
        readUser(new DataEncryptionUtil(requireActivity().getApplication()));
        for (EventWithUsers current : eventList) {
            List<UserEventCrossRef> crossList = current.getUserEventCrossRefs();
            for (UserEventCrossRef currentCross : crossList) {
                if (currentCross.getUserId().equals(this.user.getUserId()) && currentCross.getJoined()
                        && current.getEvent().getDateObject().getTime() - System.currentTimeMillis() <= 1800000) {

                    return current;
                }

            }
        }
        return null;
    }

    private void readUser(DataEncryptionUtil dataEncryptionUtil) {
        try {
            this.user = new Gson().fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        String location = selected.getEvent().getLocation();
        double[] cord = Parser.getCord(location);
        LatLng place = new LatLng(cord[0], cord[1]);
        googleMap.addMarker(new MarkerOptions()
                .position(place)
                .title(selected.getEvent().getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15));
        this.googleMap = googleMap;
        try {
            loadPic();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFriendsTracker(GoogleMap googleMap) {
        locationViewModel.getLocation(selected.getEvent().getEventId()).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                Location location = ((Result.LocationSuccess) result).getLocation();
                LatLng place = new LatLng(location.latitude, location.longitude);
                String userName = null;
                for (User user : selected.getUsers()) {
                    if (user.getUserId().equals(location.getUserId())) {
                        userName = user.getUsername();
                        break;
                    }
                }

                MarkerOptions marker = new MarkerOptions()
                        .position(place)
                        .icon(usersPic.get(location.getUserId()))
                        .title(userName);

                googleMap.clear();
                String locationEvent = selected.getEvent().getLocation();
                double[] cord = Parser.getCord(locationEvent);
                LatLng placeEvent = new LatLng(cord[0], cord[1]);
                googleMap.addMarker(new MarkerOptions()
                        .position(placeEvent)
                        .title(selected.getEvent().getName()));

                markers.put(location.getUserId(), marker);

                for(MarkerOptions m : markers.values()){
                    googleMap.addMarker(m);
                }
            }
        });
    }
}