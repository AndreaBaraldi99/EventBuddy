package it.lanos.eventbuddy.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.lanos.eventbuddy.data.ILocationRepository;
import it.lanos.eventbuddy.data.source.models.Location;
import it.lanos.eventbuddy.data.source.models.Result;

public class LocationViewModel extends ViewModel {

    private final ILocationRepository iLocationRepository;


    public LocationViewModel(ILocationRepository iLocationRepository) {
        this.iLocationRepository = iLocationRepository;
    }

    public MutableLiveData<Result> getLocation(String eventId){
        return iLocationRepository.getLocation(eventId);
    }

    public void setLocation(Location location){
        iLocationRepository.setLocation(location);
    }

    public void stopLocationUpdates(String eventId){
        iLocationRepository.stopLocationUpdates(eventId);
    }





}
