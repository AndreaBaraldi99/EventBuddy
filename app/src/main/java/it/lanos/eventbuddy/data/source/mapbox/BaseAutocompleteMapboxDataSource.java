package it.lanos.eventbuddy.data.source.mapbox;

import it.lanos.eventbuddy.data.source.SuggestionsCallback;

public abstract class BaseAutocompleteMapboxDataSource {
    protected SuggestionsCallback suggestionsCallback;
    public void setSuggestionsCallback(SuggestionsCallback suggestionsCallback) {
        this.suggestionsCallback = suggestionsCallback;
    }
    public abstract void getSuggestions(String query, String sessionKey);

    public abstract void getFeature(String id);
}
