package it.lanos.eventbuddy.UI;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.lanos.eventbuddy.data.ISuggestionsRepository;
import it.lanos.eventbuddy.data.source.models.Result;

public class CreateEventViewModel extends ViewModel {
    private final ISuggestionsRepository iSuggestionRepository;
    private MutableLiveData<Result> suggestionListLiveData;

    private MutableLiveData<Result> feature;


    public CreateEventViewModel(ISuggestionsRepository iSuggestionsRepository) {
        this.iSuggestionRepository =  iSuggestionsRepository;
    }



    public MutableLiveData<Result> getFeature(String query){
        feature = iSuggestionRepository.getFeature(query);
        return feature;
    }

    public MutableLiveData<Result> getSuggestions(String query){
        suggestionListLiveData = iSuggestionRepository.getSuggestions(query);
        return suggestionListLiveData;
    }



}
