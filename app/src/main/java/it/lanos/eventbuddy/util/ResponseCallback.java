package it.lanos.eventbuddy.util;

import java.util.List;

import it.lanos.eventbuddy.data.source.entities.EventWithUsers;

public interface ResponseCallback {
    void onSuccess(List<EventWithUsers> newsList);
    void onFailure(String errorMessage);
}
