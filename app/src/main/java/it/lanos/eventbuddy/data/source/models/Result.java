package it.lanos.eventbuddy.data.source.models;

import java.util.List;

public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        return this instanceof Success;
    }

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