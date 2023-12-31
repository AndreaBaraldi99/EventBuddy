package it.lanos.eventbuddy.data;

import static it.lanos.eventbuddy.util.Constants.ENCRYPTED_DATA_FILE_NAME;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import it.lanos.eventbuddy.data.source.SuggestionsCallback;
import it.lanos.eventbuddy.data.source.mapbox.BaseAutocompleteMapboxDataSource;
import it.lanos.eventbuddy.data.source.models.Result;
import it.lanos.eventbuddy.data.source.models.User;
import it.lanos.eventbuddy.data.source.models.mapbox.FeatureApiResponse;
import it.lanos.eventbuddy.data.source.models.mapbox.SuggestionsApiResponse;
import it.lanos.eventbuddy.util.DataEncryptionUtil;

public class SuggestionsRepository implements SuggestionsCallback, ISuggestionsRepository {
    private static final String TAG = "SuggestionsRepository";
    private final MutableLiveData<Result> suggestionsLiveData;
    private final MutableLiveData<Result> featureLiveData;
    private final BaseAutocompleteMapboxDataSource mapboxDataSource;
    private User user;
    public SuggestionsRepository(BaseAutocompleteMapboxDataSource mapboxDataSource, DataEncryptionUtil dataEncryptionUtil) {
        this.mapboxDataSource = mapboxDataSource;
        this.mapboxDataSource.setSuggestionsCallback(this);
        suggestionsLiveData = new MutableLiveData<>();
        featureLiveData = new MutableLiveData<>();
        readUser(dataEncryptionUtil);
    }
    private void readUser(DataEncryptionUtil dataEncryptionUtil){
        try {
            this.user = new Gson().fromJson(dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME), User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public MutableLiveData<Result> attachSuggestions() {
        return suggestionsLiveData;
    }

    public MutableLiveData<Result> attachFeature(){return featureLiveData; }

    @Override
    public MutableLiveData<Result> getSuggestions(String query) {
        mapboxDataSource.getSuggestions(query, user.getUserId());
        return suggestionsLiveData;
    }

    @Override
    public MutableLiveData<Result> getFeature(String id) {
        mapboxDataSource.getFeature(id);
        return featureLiveData;
    }
}
