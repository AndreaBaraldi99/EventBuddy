package it.lanos.eventbuddy.data;

import androidx.lifecycle.MutableLiveData;

import it.lanos.eventbuddy.data.source.SuggestionsCallback;
import it.lanos.eventbuddy.data.source.mapbox.BaseAutocompleteMapboxDataSource;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.mapbox.FeatureApiResponse;
import it.lanos.eventbuddy.data.source.models.mapbox.SuggestionsApiResponse;

public class SuggestionsRepository implements SuggestionsCallback, ISuggestionsRepository {
    private static final String TAG = "SuggestionsRepository";
    private final MutableLiveData<Result> suggestionsLiveData;
    private final MutableLiveData<Result> featureLiveData;
    private final BaseAutocompleteMapboxDataSource mapboxDataSource;
    public SuggestionsRepository(BaseAutocompleteMapboxDataSource mapboxDataSource) {
        this.mapboxDataSource = mapboxDataSource;
        this.mapboxDataSource.setSuggestionsCallback(this);
        suggestionsLiveData = new MutableLiveData<>();
        featureLiveData = new MutableLiveData<>();
    }

    @Override
    public void onSuccessFromRemote(SuggestionsApiResponse response) {
        Result.SuggestionsSuccess suggestionsSuccess = new Result.SuggestionsSuccess(response.suggestions);
        suggestionsLiveData.postValue(suggestionsSuccess);
    }

    @Override
    public void onFailureFromRemote(Exception error) {
        Result.Error errorResult = new Result.Error(error.getMessage());
        suggestionsLiveData.postValue(errorResult);
    }

    @Override
    public void onFeatureFromRemote(FeatureApiResponse body) {
        Result.FeatureSuccess featureSuccess = new Result.FeatureSuccess(body.features);
        featureLiveData.postValue(featureSuccess);
    }

    @Override
    public MutableLiveData<Result> getSuggestions(String query) {
        mapboxDataSource.getSuggestions(query);
        return suggestionsLiveData;
    }

    @Override
    public MutableLiveData<Result> getFeature(String id) {
        mapboxDataSource.getFeature(id);
        return featureLiveData;
    }
}
