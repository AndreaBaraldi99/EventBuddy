package it.lanos.eventbuddy.data.source.models;

import java.util.ArrayList;
import java.util.List;

import it.lanos.eventbuddy.data.source.models.mapbox.Feature;
import it.lanos.eventbuddy.data.source.models.mapbox.Suggestion;

public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        return this instanceof Success || this instanceof UserSuccess ||this instanceof AuthSuccess || this instanceof SuggestionsSuccess || this instanceof FeatureSuccess;
    }
    public boolean isSuggestionSuccess(){return this instanceof SuggestionsSuccess;}
    public boolean isFeatureSuccess(){return this instanceof FeatureSuccess;}

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class Success extends Result {
        private final List<EventWithUsers> events;
        public Success(List<EventWithUsers> events) {
            this.events = events;
        }
        public List<EventWithUsers> getData() {
            return events;
        }
    }

    public static final class UserSuccess extends Result {
        private final List<User> users;
        public UserSuccess(List<User> users) {
            this.users = users;
        }
        public List<User> getData() {
            return users;
        }
    }

    /**
     * Class that represents a success from Firebase
     */
    public static final class AuthSuccess extends Result {
        private final String message;
        public AuthSuccess(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    public static final class SuggestionsSuccess extends Result {
        private final List<Suggestion> suggestions;
        public SuggestionsSuccess(List<Suggestion> suggestions) {
            this.suggestions = suggestions;
        }
        public List<Suggestion> getData() {
            return suggestions;
        }
    }

    public static final class LocationSuccess extends Result {
        private final Location location;
        public LocationSuccess(Location location){
            this.location = location;
        }
        public Location getLocation(){
            return location;
        }
    }

    public static final class FeatureSuccess extends Result {
        private final ArrayList<Feature> features;
        public FeatureSuccess(ArrayList<Feature> features) {
            this.features = features;
        }
        public ArrayList<Feature> getData() {
            return features;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}