package it.lanos.eventbuddy.data.source.firebase.realtimeDB;

import static it.lanos.eventbuddy.util.Constants.DATABASE_URL;
import static it.lanos.eventbuddy.util.Constants.FIREBASE_LOCATION_COLLECTION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.lanos.eventbuddy.data.source.models.Location;

public class LocationRemoteDataSource extends BaseLocationRemoteDataSource {
    private static final String TAG = LocationRemoteDataSource.class.getSimpleName();
    private final DatabaseReference locationReference;
    private ChildEventListener locationValueEventListener;

    public LocationRemoteDataSource(){
        this.locationReference = FirebaseDatabase.getInstance(DATABASE_URL).getReference();
    }

    @Override
    public void setLocation(Location location) {
        locationReference.child(FIREBASE_LOCATION_COLLECTION).child(location.getEventId()).push().setValue(location)
                .addOnFailureListener(e -> locationCallback.onFailureFromRemoteDatabase(e));
    }

    @Override
    public void observeLocation(String eventId) {
        if(locationValueEventListener == null) {
            locationValueEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Location location = snapshot.getValue(Location.class);
                    locationCallback.onReceivedLocationFromRemoteSuccess(location);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Location location = snapshot.getValue(Location.class);
                    locationCallback.onReceivedLocationFromRemoteSuccess(location);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    /*Location location = snapshot.getValue(Location.class);
                    locationCallback.onRecivedLocationFromRemoteSuccess(location);*/
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    /*Location location = snapshot.getValue(Location.class);
                    locationCallback.onRecivedLocationFromRemoteSuccess(location);*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    locationCallback.onFailureFromRemoteDatabase(error.toException());
                }
            };
        }
        locationReference.child(FIREBASE_LOCATION_COLLECTION).child(eventId).addChildEventListener(locationValueEventListener);
    }

    @Override
    public void detachObserver(String eventId) {
        if(locationValueEventListener != null)
            locationReference.child(FIREBASE_LOCATION_COLLECTION).child(eventId).removeEventListener(locationValueEventListener);

    }
}
