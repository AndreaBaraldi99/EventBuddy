package it.lanos.eventbuddy.data.source;

import it.lanos.eventbuddy.data.source.models.mapbox.FeatureApiResponse;
import it.lanos.eventbuddy.data.source.models.mapbox.SuggestionsApiResponse;

public interface SuggestionsCallback {
    void onSuccessFromRemote(SuggestionsApiResponse body);
    void onFailureFromRemote(Exception error);
    void onFeatureFromRemote(FeatureApiResponse body);
}
