package it.lanos.eventbuddy.data;

import androidx.lifecycle.MutableLiveData;

import it.lanos.eventbuddy.data.source.models.Result;

public interface ISuggestionsRepository {
    MutableLiveData<Result> getSuggestions(String query);
    MutableLiveData<Result> getFeature(String id);
}
