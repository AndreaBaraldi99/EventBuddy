package it.lanos.eventbuddy.UI;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.lanos.eventbuddy.util.Constants.PROFILE_PICTURES_BUCKET_REFERENCE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.UI.authentication.UserViewModel;
import it.lanos.eventbuddy.UI.authentication.UserViewModelFactory;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.ILocationRepository;
import it.lanos.eventbuddy.data.IUserRepository;
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

    private UserViewModel userViewModel;

    private List<EventWithUsers> eventList;

    private EventWithUsers selected;

    private GoogleMap googleMap;

    private LocationCallback locationCallback;

    private FusedLocationProviderClient fusedLocationClient;

    private Map<String, MarkerOptions> markers;

    private Map<String, BitmapDescriptor> usersPic;

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

        IUserRepository iUserRepository =
                ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(iUserRepository)).get(UserViewModel.class);


        eventList = new ArrayList<>();

        markers = new HashMap<>();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (android.location.Location location : locationResult.getLocations()) {
                    Location loc = new Location(location.getLatitude(), location.getLongitude(), selected.getEvent().getEventId());
                    locationViewModel.setLocation(loc);

                }
            }
        };

        usersPic = new HashMap<>();






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
                    if(selected != null) {
                        for (User u : selected.getUsers()) {
                            userViewModel.downloadProfileImage(u.getUserId()).observe(getViewLifecycleOwner(), res -> {
                                if (res.isSuccess()) {
                                    byte[] p = ((Result.ImageSuccess) res).getData();
                                    Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(p, 0, p.length), 64, 64, false);
                                    BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
                                    usersPic.put(u.getUserId(), bd);
                                }
                            });
                        }
                    }
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
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.active_map);
            if (selected != null) {
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
                startLocationUpdates();



            } else {
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
        while (itE.hasNext()) {
            EventWithUsers current = (EventWithUsers) itE.next();
            List<UserEventCrossRef> crossList = current.getUserEventCrossRefs();
            Iterator itC = crossList.iterator();
            while (itC.hasNext()) {
                UserEventCrossRef currentCross = (UserEventCrossRef) itC.next();
                if (currentCross.getUserId().equals(this.user.getUserId()) && currentCross.getJoined()) {
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
        String showLocation = Parser.formatLocation(location);
        double[] cord = Parser.getCord(location);
        LatLng place = new LatLng(cord[0], cord[1]);
        googleMap.addMarker(new MarkerOptions()
                .position(place)
                .title(selected.getEvent().getName()));
        // [START_EXCLUDE silent]
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 15));
        this.googleMap = googleMap;
        setFriendsTracker(googleMap);

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

                markers.put(location.getUserId(), marker);

                for(MarkerOptions m : markers.values()){
                    googleMap.addMarker(m);
                }





            }
        });

    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(false)
                .build();


        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }
}