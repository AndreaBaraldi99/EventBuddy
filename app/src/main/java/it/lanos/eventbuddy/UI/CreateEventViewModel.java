package it.lanos.eventbuddy.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.lanos.eventbuddy.data.ISuggestionsRepository;
import it.lanos.eventbuddy.data.SuggestionsRepository;
import it.lanos.eventbuddy.data.source.models.Result;

public class CreateEventViewModel extends ViewModel {
    private SuggestionsRepository suggestionRepository;
    private MutableLiveData<Result> suggestionListLiveData;

    private MutableLiveData<Result> feature;



    public CreateEventViewModel(ISuggestionsRepository iSuggestionsRepository) {
        this.suggestionRepository = (SuggestionsRepository) iSuggestionsRepository;
    }

    public MutableLiveData<Result> attachSuggestions() {
        if(suggestionListLiveData == null)
            suggestionListLiveData = suggestionRepository.attachSuggestions();

        return suggestionListLiveData;
    }

    public MutableLiveData<Result> attachFeature(){
        if(feature == null)
            feature = suggestionRepository.attachFeature();
        return feature;
    }

    public void getFeature(String query){
        suggestionRepository.getFeature(query);
    }

    public void getSuggestions(String query){
        suggestionRepository.getSuggestions(query);
    }



}
