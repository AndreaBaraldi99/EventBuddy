package it.lanos.eventbuddy.data.source.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "eventId"})
public class UserEventCrossRef {
    @NonNull
    private String userId;
    @NonNull
    @ColumnInfo(index = true)
    private String eventId;
    @NonNull
    private Boolean joined;

    public UserEventCrossRef(@NonNull String userId, @NonNull String eventId, @NonNull Boolean joined) {
        this.userId = userId;
        this.eventId = eventId;
        this.joined = joined;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @NonNull
    public Boolean getJoined() {
        return joined;
    }

    public void setJoined(@NonNull Boolean joined) {
        this.joined = joined;
    }

}
