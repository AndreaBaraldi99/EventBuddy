package it.lanos.eventbuddy.data.source.mapbox;

import static it.lanos.eventbuddy.util.Constants.ACCESS_KEY;
import static it.lanos.eventbuddy.util.Constants.API_KEY_ERROR;
import static it.lanos.eventbuddy.util.Constants.SESSION_KEY;

import androidx.annotation.NonNull;

import it.lanos.eventbuddy.data.services.MapboxService;
import it.lanos.eventbuddy.data.source.models.mapbox.FeatureApiResponse;
import it.lanos.eventbuddy.data.source.models.mapbox.SuggestionsApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutoCompleteMapboxDataSource extends BaseAutocompleteMapboxDataSource{
    private final MapboxService mapboxService;
    public AutoCompleteMapboxDataSource(MapboxService mapboxService) {
        this.mapboxService = mapboxService;
    }
    @Override
    public void getSuggestions(String query) {
        Call<SuggestionsApiResponse> suggestionsResponseCall = mapboxService.getSuggestions(query, ACCESS_KEY, SESSION_KEY);
        suggestionsResponseCall.enqueue(new Callback<SuggestionsApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuggestionsApiResponse> call, @NonNull Response<SuggestionsApiResponse> response) {
               if(response.body() != null && response.isSuccessful() && !response.body().attribution.equals("error")) {
                   suggestionsCallback.onSuccessFromRemote(response.body());
               } else {
                   suggestionsCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
               }
            }
            @Override
            public void onFailure(@NonNull Call<SuggestionsApiResponse> call, @NonNull Throwable t) {
                suggestionsCallback.onFailureFromRemote(new Exception(t));
            }
        });
    }

    public void getFeature(String id) {
        Call<FeatureApiResponse> suggestionsResponseCall = mapboxService.getFeature(id, ACCESS_KEY, SESSION_KEY);
        suggestionsResponseCall.enqueue(new Callback<FeatureApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<FeatureApiResponse> call, @NonNull Response<FeatureApiResponse> response) {
               if(response.body() != null && response.isSuccessful() && !response.body().attribution.equals("error")) {
                   suggestionsCallback.onFeatureFromRemote(response.body());
               } else {
                   suggestionsCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
               }
            }
            @Override
            public void onFailure(@NonNull Call<FeatureApiResponse> call, @NonNull Throwable t) {
                suggestionsCallback.onFailureFromRemote(new Exception(t));
            }
        });
    }
}